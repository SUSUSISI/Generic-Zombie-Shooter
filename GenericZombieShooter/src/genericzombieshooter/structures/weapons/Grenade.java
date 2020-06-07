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
import genericzombieshooter.structures.Explosion;
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.Particle;
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
 * Used to represent the grenade weapon.
 * @author Darin Beaudreau
 */
public class Grenade implements WeaponStrategy {
    // Final Variables
    private static final int WEAPON_PRICE = 800;
    private static final int AMMO_PRICE = 500;
    private static final int DEFAULT_AMMO = 1;
    private static final int DAMAGE_PER_EXPLOSION = (500 / (int)Globals.SLEEP_TIME);
    private static final double PARTICLE_SPREAD = 5.0;
    private static final int THROWING_DISTANCE = 1000;
    
    // Member Variables
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
    private List<Explosion> explosions;
    public List<Explosion> getExplosions() { return this.explosions; }
    private ExplosionCheckDamageStrategy explosionCheckDamageStrategy = new ExplosionCheckDamageStrategy();
    
    public Grenade(String name, int key, String filename, int ammoLeft, int maxAmmo, int ammoPerUse, int cooldown, boolean automatic) {
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
    	this.explosions = Collections.synchronizedList(new ArrayList<Explosion>());
    }
    
    @Override
    public int getWeaponPrice() { return Grenade.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return Grenade.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() {
        return Grenade.DEFAULT_AMMO;
    }
    
    
    @Override
    public void resetAmmo() {
    	synchronized(this.particles) { this.particles.clear(); }
        this.ammoLeft = Grenade.DEFAULT_AMMO;
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
                        Rectangle2D.Double zombieRectangle = new Rectangle2D.Double((zombie.x - (width / 2)), (zombie.y - (height / 2)), width, height);
                        if(particle.checkCollision(zombieRectangle)) collision = true;
                    }
                    if(!particle.isAlive() || collision) {
                        this.explosions.add(new Explosion(Images.EXPLOSION_SHEET, particle.getPos()));
                        Sounds.EXPLOSION.play();
                        particleIterator.remove();
                        continue;
                    }
                    if(particle.outOfBounds()) {
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
                Iterator<Particle> particleterator = this.particles.iterator();
                while(particleterator.hasNext()) {
                    Particle particle = particleterator.next();
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
                Particle particle = createGrenadeParticle(theta, pos);
                this.particles.add(particle);
                this.consumeAmmo();
                this.resetCooldown();
                this.fired = true;
                Sounds.THROW.play();
            }
        }
    }
    
    public Particle createGrenadeParticle(double theta, Point2D.Double pos) {
        Particle particle = new Particle(theta, Grenade.PARTICLE_SPREAD, 5.0, 
                                 (Grenade.THROWING_DISTANCE / (int)Globals.SLEEP_TIME),
                                  pos, new Dimension(16, 16), Images.GRENADE_PARTICLE) {
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
    	this.setCheckDamageStrategy(explosionCheckDamageStrategy);
        return this.explosionCheckDamageStrategy.explosionCheckForDamage(rect, this.explosions, DAMAGE_PER_EXPLOSION);
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
