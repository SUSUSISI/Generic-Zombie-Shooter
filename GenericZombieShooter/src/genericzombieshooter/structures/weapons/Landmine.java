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
import genericzombieshooter.structures.Explosion;
import genericzombieshooter.structures.Particle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used to represent a landmine weapon.
 * @author Darin Beaudreau
 */
public class Landmine extends Weapon {
    // Final Variables
    private static final int WEAPON_PRICE = 750;
    private static final int AMMO_PRICE = 400;
    private static final int DEFAULT_AMMO = 1;
    private static final int MAX_AMMO = 3;
    private static final int AMMO_PER_USE = 1;
    private static final int DAMAGE_PER_EXPLOSION = (500 / (int)Globals.SLEEP_TIME);
    private static final int PARTICLE_LIFE = 3 * 60 * 1000;
    
    // Member variables.
    private List<Explosion> explosions;
    
    public Landmine() {
        super("Flip-Flop", KeyEvent.VK_6, "/resources/images/GZS_FlipFlop.png",
              Landmine.DEFAULT_AMMO, Landmine.MAX_AMMO, Landmine.AMMO_PER_USE, 50, false);
        this.explosions = Collections.synchronizedList(new ArrayList<Explosion>());
    }
    
    @Override
    public int getWeaponPrice() { return Landmine.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return Landmine.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() { return Landmine.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() {
        super.resetAmmo();
        synchronized(this.explosions) { this.explosions.clear(); }
        this.ammoLeft = Landmine.DEFAULT_AMMO;
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        { // Update particles.
            synchronized(this.particles) {
                Iterator<Particle> particleIterator = this.particles.iterator();
                while(particleIterator.hasNext()) {
                    Particle particle = particleIterator.next();
                    particle.update();

                    boolean collision = false;
                    Iterator<Zombie> zombieIterator = zombies.iterator();
                    while(zombieIterator.hasNext()) {
                        Zombie zombie = zombieIterator.next();
                        double width = zombie.getImage().getWidth();
                        double height = zombie.getImage().getHeight();
                        Rectangle2D.Double rect = new Rectangle2D.Double((zombie.x - (width / 2)), (zombie.y - (height / 2)), width, height);
                        if(particle.checkCollision(rect)) collision = true;
                    }
                    if(!particle.isAlive() || collision) {
                        this.explosions.add(new Explosion(Images.EXPLOSION_SHEET, particle.getPos()));
                        Sounds.EXPLOSION.play();
                        particleIterator.remove();
                        continue;
                    }
                }
            }
        } // End particle updates.
        { // Update explosions.
            synchronized(this.explosions) {
                Iterator<Explosion> explosionIterator = this.explosions.iterator();
                while(explosionIterator.hasNext()) {
                    Explosion explosion = explosionIterator.next();
                    explosion.getImage().update();
                    if(!explosion.getImage().isActive()) {
                        explosionIterator.remove();
                        continue;
                    }
                }
            }
        } // End explosion updates.
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        synchronized(this.particles) {
            if(!this.particles.isEmpty()) {
                Iterator<Particle> particleIterator = this.particles.iterator();
                while(particleIterator.hasNext()) {
                    Particle particle = particleIterator.next();
                    if(particle.isAlive()) particle.draw(g2d);
                }
            }
        }
        synchronized(this.explosions) {
            if(!this.explosions.isEmpty()) {
                Iterator<Explosion> explosionIterator = this.explosions.iterator();
                while(explosionIterator.hasNext()) {
                    Explosion explosion = explosionIterator.next();
                    if(explosion.getImage().isActive()) explosion.draw(g2d);
                }
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        synchronized(this.particles) {
            if(this.canFire()) {
                Particle particle = createLandmineParticle(theta, pos);
                this.particles.add(particle);
                this.consumeAmmo();
                this.resetCooldown();
                this.fired = true;
                Sounds.LANDMINE_ARMED.play();
            }
        }
    }
    
    private Particle createLandmineParticle(double theta, Point2D.Double pos) {
        Particle particle = new Particle(theta, 0.0, 0.0, (Landmine.PARTICLE_LIFE / (int)Globals.SLEEP_TIME),
                                  pos, new Dimension(24, 24), Images.LANDMINE_PARTICLE) {
            @Override
            public void update() {
                if(this.isAlive()) this.life--;
            }
            
            @Override
            public void draw(Graphics2D g2d) {
                double x = this.pos.x - (this.size.width / 2);
                double y = this.pos.y - (this.size.height / 2);
                g2d.drawImage(this.image, (int)x, (int)y, null);
            }
        };
        
        return particle;
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        synchronized(this.explosions) {
            int damage = 0;
            if(!this.explosions.isEmpty()) {
                Iterator<Explosion> explosionIterator = this.explosions.iterator();
                while(explosionIterator.hasNext()) {
                    Explosion explosion = explosionIterator.next();
                    if(explosion.getImage().isActive()) {
                        Rectangle2D.Double explosionRectangle = new Rectangle2D.Double((explosion.x - (explosion.getSize().width / 2)), (explosion.y - (explosion.getSize().height / 2)),
                                                                             explosion.getSize().width, explosion.getSize().height);
                        if(rect.intersects(explosionRectangle)) {
                            damage += Landmine.DAMAGE_PER_EXPLOSION;
                        }
                    }
                }
            }
            return damage;
        }
    }
}
