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

import genericzombieshooter.GZSFramework;
import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.Particle;
import genericzombieshooter.structures.items.UnlimitedAmmo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used to represent the Assault Rifle weapon.
 * @author Darin Beaudreau
 */
public class AssaultRifle implements WeaponStrategy {
    // Final Variables
    private static final int WEAPON_PRICE = 1000;
    private static final int AMMO_PRICE = 200;
    private static final int DEFAULT_AMMO = 60;
    private static final int DAMAGE_PER_PARTICLE = 100;
    private static final double PARTICLE_SPREAD = 5.0;
    private static final int PARTICLE_LIFE = 2000;
    
    private String name;
    private int key;
    private BufferedImage image;
    protected int ammoLeft;
    private int maxAmmo;
    private int ammoPerUse;
    private boolean automatic; // Indicates if the weapon can be fired continuously.
    protected boolean fired; // Used with automatic to determine if the weapon needs to be fired again.
    private int cooldown;
    private int coolPeriod;
    protected List<Particle> particles;
    
    public AssaultRifle(String name, int key, String filename, int ammoLeft, int maxAmmo, int ammoPerUse, int cooldown, boolean automatic) {
        this.name = name;
        this.key = key;
        
        this.image = GZSFramework.loadImage(filename);
        
        this.ammoLeft = ammoLeft;
        this.maxAmmo = maxAmmo;
        this.ammoPerUse = ammoPerUse;
        
        this.automatic = automatic;
        this.fired = false;
        this.cooldown = cooldown;
        this.coolPeriod = cooldown;
        
        this.particles = Collections.synchronizedList(new ArrayList<Particle>());
    }
    
    @Override
    public int getWeaponPrice() { return AssaultRifle.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return AssaultRifle.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() {
        return AssaultRifle.DEFAULT_AMMO;
    }
    
    @Override
    public void resetAmmo() {
    	synchronized(this.particles) { this.particles.clear(); }
        this.ammoLeft = AssaultRifle.DEFAULT_AMMO;
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
    	synchronized(this.particles) {
            if(!this.particles.isEmpty()) {
                Iterator<Particle> particleIterator = this.particles.iterator();
                while(particleIterator.hasNext()) {
                    Particle particle = particleIterator.next();
                    particle.update();
                    if(!particle.isAlive() || particle.outOfBounds()) {
                        particleIterator.remove();
                        continue;
                    }
                }
            }
        }
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        synchronized(this.particles) {
            // Draw all particles whose life has not yet expired.
            if(!this.particles.isEmpty()) {
                g2d.setColor(Color.ORANGE);
                Iterator<Particle> particleIterator = this.particles.iterator();
                while(particleIterator.hasNext()) {
                    Particle particle = particleIterator.next();
                    if(particle.isAlive()) particle.draw(g2d);
                }
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        // If there is enough ammo left...
        if(this.canFire()) {
            // Create a new bullet and add it to the list.
            int width = 4;
            int height = 10;
            Particle particle = new Particle(theta, AssaultRifle.PARTICLE_SPREAD, 8.0,
                          (AssaultRifle.PARTICLE_LIFE / (int)Globals.SLEEP_TIME), new Point2D.Double(pos.x, pos.y),
                           new Dimension(width, height), Images.RTPS_BULLET);
            this.particles.add(particle);
            // Use up ammo.
            if(!player.hasEffect(UnlimitedAmmo.EFFECT_NAME)) this.consumeAmmo();
            this.resetCooldown();
            Sounds.RTPS.play();
        }
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        synchronized(this.particles) {
            int damage = 0;
            if(!this.particles.isEmpty()) {
                // Check all particles for collisions with the target rectangle.
                Iterator<Particle> particleIterator = this.particles.iterator();
                while(particleIterator.hasNext()) {
                    Particle particle = particleIterator.next();
                    // If the particle is still alive and has collided with the target.
                    if(particle.isAlive() && particle.checkCollision(rect)) {
                        // Add the damage of the particle and remove it from the list.
                        damage += AssaultRifle.DAMAGE_PER_PARTICLE;
                        particleIterator.remove();
                    }
                }
            }
            return damage;
        }
    }

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getKey() {
		return this.key;
	}

	@Override
	public BufferedImage getImage() {
		return this.image;
	}

	@Override
	public int getAmmoLeft() {
		return this.ammoLeft;
	}

	@Override
	public int getMaxAmmo() {
		return this.maxAmmo;
	}

	@Override
	public boolean isAutomatic() {
		return this.automatic;
	}

	@Override
	public boolean hasFired() {
		return this.fired;
	}

	@Override
	public void resetFire() {
		this.fired = false;
	}

	@Override
	public double getCooldownPercentage() {
		return ((double)cooldown / (double)coolPeriod);
	}

	@Override
	public void resetCooldown() {
		this.cooldown = this.coolPeriod;		
	}

	@Override
	public void cool() {
		if(this.cooldown > 0) this.cooldown--;
	}

	@Override
	public boolean canFire() {
		boolean isAmmoLeft = (this.ammoLeft >= this.ammoPerUse);
        boolean isCoolDown = (this.cooldown != 0);
        boolean canFire = this.automatic || (!this.automatic && !this.fired);
        return (isAmmoLeft) && (!isCoolDown) && (canFire); 
	}

	@Override
	public boolean ammoFull() {
		return this.ammoLeft == this.maxAmmo;
	}

	@Override
	public void addAmmo(int amount) {
		if((this.ammoLeft + amount) > this.maxAmmo) this.ammoLeft = this.maxAmmo;
        else this.ammoLeft += amount;
	}

	@Override
	public void consumeAmmo() {
		this.ammoLeft -= this.ammoPerUse;
	}

	@Override
	public List<Particle> getParticles() {
		return this.particles;
	}

	@Override
	public List<LightSource> getLights() {
		return null;
	}
}
