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
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Particle;
import genericzombieshooter.structures.items.UnlimitedAmmo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Used to represent the Shotgun weapon.
 * @author Darin Beaudreau
 */
public class Shotgun extends Weapon {
    // Final Variables
    private static final int WEAPON_PRICE = 1200;
    private static final int AMMO_PRICE = 200;
    private static final int DEFAULT_AMMO = 24;
    private static final int MAX_AMMO = 64;
    private static final int AMMO_PER_USE = 1;
    private static final int PARTICLES_PER_USE = 8;
    private static final int DAMAGE_PER_PARTICLE = 50;
    private static final double PARTICLE_SPREAD = 20.0;
    private static final int PARTICLE_LIFE = 1000;
    
    private GunUpdateStrategy gunUpdateStrategy = new GunUpdateStrategy();
    private GunDrawAmmoStrategy gunDrawAmmoStrategy = new GunDrawAmmoStrategy();
    private GunCheckDamageStrategy gunCheckDamageStrategy = new GunCheckDamageStrategy();
    
    public Shotgun() {
        super("Boomstick", KeyEvent.VK_3, "/resources/images/GZS_Boomstick.png", 
              Shotgun.DEFAULT_AMMO, Shotgun.MAX_AMMO, Shotgun.AMMO_PER_USE, 40, false);
    }
    
    @Override
    public int getWeaponPrice() { return Shotgun.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return Shotgun.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() {
        return Shotgun.DEFAULT_AMMO;
    }
    
    @Override
    public void resetAmmo() {
        super.resetAmmo();
        this.ammoLeft = Shotgun.DEFAULT_AMMO;
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
        this.drawAmmoStrategy.drawAmmo(g2d, this.particles, Color.YELLOW);
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        synchronized(this.particles) {
            // If there is enough ammo left...
            if(this.canFire()) {
                // Create new particles and add them to the list.
                for(int i = 0; i < Shotgun.PARTICLES_PER_USE; i++) {
                    Particle particle = new Particle(theta, Shotgun.PARTICLE_SPREAD, 6.0,
                                              (Shotgun.PARTICLE_LIFE / (int)Globals.SLEEP_TIME), new Point2D.Double(pos.x, pos.y),
                                               new Dimension(5, 5));
                    this.particles.add(particle);
                }
                // Use up ammo.
                if(!player.hasEffect(UnlimitedAmmo.EFFECT_NAME)) this.consumeAmmo();
                this.resetCooldown();
                this.fired = true;
                Sounds.BOOMSTICK.play();
            }
        }
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
    	this.setCheckDamageStrategy(gunCheckDamageStrategy);
        return this.gunCheckDamageStrategy.gunCheckForDamage(rect, this.particles, DAMAGE_PER_PARTICLE);
    }
}
