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
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.Particle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used to deploy laser terminals. When two are deployed, a laser is drawn
 * between them that damages enemies that pass through it. Only one laser
 * wire can be deployed at a time.
 * @author Darin Beaudreau
 */
public class LaserWire implements WeaponStrategy {
    // Final Variables
    private static final int WEAPON_PRICE = 1500;
    private static final int AMMO_PRICE = 400;
    private static final int DEFAULT_AMMO = 1;
    private static final int DAMAGE_BY_LASER = 100;
    private static final long LASER_COOLDOWN = 500;
    private static final int PARTICLE_LIFE = 2 * 60 * 1000;
    private static final int LASER_LIFE = 30 * 1000;
    private static final int MAX_LASER_DIST = 300;
    
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
    private List<Line2D.Double> lasers;
    private long lastDamageDone;
    
    public LaserWire(String name, int key, String filename, int ammoLeft, int maxAmmo, int ammoPerUse, int cooldown, boolean automatic) {
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
        this.lasers = Collections.synchronizedList(new ArrayList<Line2D.Double>());
        this.lastDamageDone = 0;
    }
    
    @Override
    public int getWeaponPrice() { return LaserWire.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return LaserWire.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() { return LaserWire.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() {
    	synchronized(this.particles) { this.particles.clear(); }
        synchronized(this.lasers) { this.lasers.clear(); }
        this.ammoLeft = LaserWire.DEFAULT_AMMO;
    }
    
    private boolean checkFire() {
    	boolean isAmmoLeft = (this.ammoLeft >= this.ammoPerUse);
        boolean isCoolDown = (this.cooldown != 0);
        boolean canFire = this.automatic || (!this.automatic && !this.fired);
        return (isAmmoLeft) && (!isCoolDown) && (canFire); 
    }
    
    @Override
    public boolean canFire() {
        boolean lessThanTwoTerminals = this.particles.size() < 2;
        return checkFire() && lessThanTwoTerminals;
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        { // Update particles.
            synchronized(this.particles) {
                if(!this.particles.isEmpty()) {
                    Iterator<Particle> particleIterator = this.particles.iterator();
                    while(particleIterator.hasNext()) {
                        Particle particle = particleIterator.next();
                        particle.update();
                        
                        if(!particle.isAlive()) {
                            particleIterator.remove();
                            continue;
                        }
                    }
                } else {
                    synchronized(this.lasers) {
                        if(!this.lasers.isEmpty()) this.lasers.clear();
                    }
                }
                
                // Check if there are exactly two terminals. If so, create a laser.
                if((this.particles.size() == 2) && this.lasers.isEmpty()) {
                    Point2D.Double particleA = this.particles.get(0).getPos();
                    Point2D.Double particleB = this.particles.get(1).getPos();
                    /* If the distance between the two terminals is too far,
                       refund the player's ammo and delete the two terminals placed. */
                    double particleX = particleA.x - particleB.x;
                    double particleY = particleA.y - particleB.y;
                    if((Math.sqrt((particleX * particleX) + (particleY * particleY))) >= LaserWire.MAX_LASER_DIST) {
                        this.particles.clear();
                        this.ammoLeft = LaserWire.DEFAULT_AMMO;
                    } else {
                        this.lasers.add(new Line2D.Double(particleA, particleB));
                        int newLife = LaserWire.LASER_LIFE / (int)Globals.SLEEP_TIME;
                        this.particles.get(0).setLife(newLife);
                        this.particles.get(1).setLife(newLife);
                        this.lastDamageDone = Globals.gameTime.getElapsedMillis();
                    }
                }
            }
        } // End particle updates.
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        { // Draw particles.
            synchronized(this.particles) {
                if(!this.particles.isEmpty()) {
                    Iterator<Particle> particleIterator = this.particles.iterator();
                    while(particleIterator.hasNext()) {
                        Particle particle = particleIterator.next();
                        if(particle.isAlive()) particle.draw(g2d);
                    }
                }
                if(this.particles.size() == 1) {
                    Point2D.Double pos = this.particles.get(0).getPos();
                    g2d.setColor(new Color(191, 74, 99));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval((int)(pos.x - LaserWire.MAX_LASER_DIST), (int)(pos.y - LaserWire.MAX_LASER_DIST), 
                                 (LaserWire.MAX_LASER_DIST * 2), (LaserWire.MAX_LASER_DIST * 2));
                }
            }
        } // End drawing particles.
        { // Draw lasers.
            synchronized(this.lasers) {
                if(!this.lasers.isEmpty()) {
                    g2d.setColor(new Color(191, 74, 99));
                    g2d.setStroke(new BasicStroke(2));
                    Iterator<Line2D.Double> laserIterator = this.lasers.iterator();
                    while(laserIterator.hasNext()) {
                        Line2D.Double line = laserIterator.next();
                        g2d.draw(line);
                    }
                }
            }
        } // End drawing lasers.
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        synchronized(this.particles) {
            if(this.canFire()) {
                Particle particle = createLaserTerminal(theta, pos);
                this.particles.add(particle);
                if(this.particles.size() == 2) this.consumeAmmo();
                this.resetCooldown();
                this.fired = true;
            }
        }
    }
    
    private Particle createLaserTerminal(double theta, Point2D.Double pos) {
        Particle particle = new Particle(theta, 0.0, 0.0, (LaserWire.PARTICLE_LIFE / (int)Globals.SLEEP_TIME),
                                  pos, new Dimension(16, 16), Images.LASER_TERMINAL) {
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
        synchronized(this.lasers) {
            int damage = 0;
            if(Globals.gameTime.getElapsedMillis() >= (this.lastDamageDone + LaserWire.LASER_COOLDOWN)) {
                if(!this.lasers.isEmpty()) {
                    Iterator<Line2D.Double> laserIterator = this.lasers.iterator();
                    while(laserIterator.hasNext()) {
                        Line2D.Double laser = laserIterator.next();
                        if(rect.intersectsLine(laser)) {
                            damage += LaserWire.DAMAGE_BY_LASER;
                            this.lastDamageDone = Globals.gameTime.getElapsedMillis();
                        }
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
