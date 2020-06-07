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
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.Particle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used to create a Flare, which acts as a light source once deployed.
 * @author Darin Beaudreau
 */
public class Flare implements WeaponStrategy {
    // Final Variables
    private static final int WEAPON_PRICE = 400;
    private static final int AMMO_PRICE = 200;
    private static final int DEFAULT_AMMO = 1;
    private static final int PARTICLE_LIFE = 30 * 1000;
    
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
    private List<Animation> flares;
    private List<LightSource> lights;
    
    public Flare(String name, int key, String filename, int ammoLeft, int maxAmmo, int ammoPerUse, int cooldown, boolean automatic) {
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

    	this.flares = Collections.synchronizedList(new ArrayList<Animation>());
        this.lights = Collections.synchronizedList(new ArrayList<LightSource>());
    }
    
    @Override
    public int getWeaponPrice() { return Flare.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return Flare.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() { return Flare.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() {
        synchronized(this.flares) { this.flares.clear(); }
        synchronized(this.lights) { this.lights.clear(); }
        this.ammoLeft = Flare.DEFAULT_AMMO;
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        { // Update particles.
            synchronized(this.flares) {
                if(!this.flares.isEmpty()) {
                    Iterator<Animation> animationIterator = this.flares.iterator();
                    while(animationIterator.hasNext()) {
                        Animation animation = animationIterator.next();
                        animation.update();
                        if(!animation.isActive()) {
                            animationIterator.remove();
                            continue;
                        }
                    }
                }
            }
        } // End particle updates.
        { // Update light sources.
            synchronized(this.lights) {
                if(!this.lights.isEmpty()) {
                    Iterator<LightSource> lightSourceIterator = this.lights.iterator();
                    while(lightSourceIterator.hasNext()) {
                        LightSource lightSource = lightSourceIterator.next();
                        if(!lightSource.isAlive()) {
                            lightSourceIterator.remove();
                            continue;
                        }
                    }
                }
            }
        } // End light source updates.
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        synchronized(this.flares) {
            if(!this.flares.isEmpty()) {
                Iterator<Animation> animationIterator = this.flares.iterator();
                while(animationIterator.hasNext()) {
                    Animation animation = animationIterator.next();
                    if(animation.isActive()) animation.draw(g2d);
                }
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        if(this.canFire()) {
            synchronized(this.flares) {
                Animation animation = new Animation(Images.FLARE_PARTICLE, 32, 32, 3, (int)pos.x, (int)pos.y, 
                                            10, 0, Flare.PARTICLE_LIFE, true);
                this.flares.add(animation);
            }
            synchronized(this.lights) {
                LightSource lightSource = new LightSource(new Point2D.Double(pos.x, pos.y), Flare.PARTICLE_LIFE, 150.0f,
                                                 new float[]{0.0f, 0.6f, 0.8f, 1.0f},
                                                 new Color[]{new Color(0.0f, 0.0f, 0.0f, 0.0f),
                                                             new Color(0.0f, 0.0f, 0.0f, 0.75f),
                                                             new Color(0.0f, 0.0f, 0.0f, 0.9f),
                                                             Color.BLACK});
                this.lights.add(lightSource);
            }
            this.consumeAmmo();
            this.resetCooldown();
            this.fired = true;
        }
    }

	@Override
	public int checkForDamage(Double rect) {
		// TODO Auto-generated method stub
		return 0;
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
    public List<LightSource> getLights() { return this.lights; }
}