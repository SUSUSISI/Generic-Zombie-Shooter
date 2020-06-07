/**
    This file is part of Generic Zombie Shooter.

    Generic Zombie Shooter is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Generic Zombie Shooter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Generic Zombie Shooter.  If not, see <http://www.gnu.org/licenses/>.
 **/
package genericzombieshooter.structures.weapons;

import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Particle;
import genericzombieshooter.structures.items.UnlimitedAmmo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

/**
 * Used to represent the Flamethrower weapon.
 * @author Darin Beaudreau
 */
public class Flamethrower extends Weapon {
    // Final Variables
    private static final int WEAPON_PRICE = 3000;
    private static final int AMMO_PRICE = 500;
    private static final int DEFAULT_AMMO = 100;
    private static final int MAX_AMMO = 300;
    private static final int AMMO_PER_USE = 1;
    private static final int PARTICLES_PER_USE = 8;
    private static final int DAMAGE_PER_PARTICLE = 3;
    private static final double PARTICLE_SPREAD = 15.0;
    private static final int PARTICLE_LIFE_MIN = 1000;
    private static final int PARTICLE_LIFE_MAX = 1400;
    
    private GunUpdateStrategy gunUpdateStrategy = new GunUpdateStrategy();
    private GunDrawAmmoStrategy gunDrawAmmoStrategy = new GunDrawAmmoStrategy();
    private GunCheckDamageStrategy gunCheckDamageStrategy = new GunCheckDamageStrategy();
    
    public Flamethrower() {
        super("The Flammenwerfer", KeyEvent.VK_4, "/resources/images/GZS_Flammenwerfer.png", 
              Flamethrower.DEFAULT_AMMO, Flamethrower.MAX_AMMO, Flamethrower.AMMO_PER_USE, 0, true);
    }
    
    @Override
    public int getWeaponPrice() { return Flamethrower.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return Flamethrower.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() {
        return Flamethrower.DEFAULT_AMMO;
    }
    
    @Override
    public void resetAmmo() {
        super.resetAmmo();
        this.ammoLeft = Flamethrower.DEFAULT_AMMO;
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
    	this.setUpdateStrategy(gunUpdateStrategy);
    	this.updateStrategy.updateWeawpon(this.particles);
    	this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
    	this.setDrawAmmoStrategy(gunDrawAmmoStrategy);
        this.drawAmmoStrategy.drawAmmo(g2d, this.particles, Color.ORANGE);
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        synchronized(this.particles) {
            // If there is enough ammo left...
            if(this.canFire()) {
                // Generate new particles and add them to the list.
                for(int i = 0; i < Flamethrower.PARTICLES_PER_USE; i++) {
                    int life = Flamethrower.PARTICLE_LIFE_MIN + (int)(Globals.r.nextInt((Flamethrower.PARTICLE_LIFE_MAX - Flamethrower.PARTICLE_LIFE_MIN) + 1));
                    int size = Globals.r.nextInt(8) + 1;
                    Particle particle = new Particle(theta, Flamethrower.PARTICLE_SPREAD, 4.0,
                                          (life / (int)Globals.SLEEP_TIME), new Point2D.Double(pos.x, pos.y),
                                           new Dimension(size, size), Images.FIRE_PARTICLE) {
                        @Override
                        public void draw(Graphics2D g2d) {
                            try {
                                AffineTransform saved = g2d.getTransform();
                                AffineTransform at = AffineTransform.getRotateInstance(this.theta, this.pos.x, this.pos.y).createInverse();
                                double x = this.pos.x - (this.size.width / 2);
                                double y = this.pos.y - (this.size.height / 2);
                                g2d.setTransform(at);
                                if(this.image == null) {
                                    double width = this.size.width;
                                    double height = this.size.height;
                                    Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
                                    g2d.fill(at.createTransformedShape(rect));
                                } else {
                                    g2d.drawImage(this.image, (int)x, (int)y, null);
                                }
                                g2d.setTransform(saved);
                            } catch(NoninvertibleTransformException nte) {}
                        }
                    };
                    this.particles.add(particle);
                }
                // Use up ammo.
                if(!player.hasEffect(UnlimitedAmmo.EFFECT_NAME)) this.consumeAmmo();
                this.resetCooldown();
                if(!Sounds.FLAMETHROWER.getAudio().playing()) Sounds.FLAMETHROWER.play();
            }
        }
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
    	this.setCheckDamageStrategy(gunCheckDamageStrategy);
        return this.gunCheckDamageStrategy.gunCheckForDamage(rect, this.particles, DAMAGE_PER_PARTICLE);
    }
}
