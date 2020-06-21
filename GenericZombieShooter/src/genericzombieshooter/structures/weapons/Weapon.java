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
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.Particle;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Generic class to be extended for all weapons.
 * @author Darin Beaudreau
 */
public abstract class Weapon {
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
    protected CheckDamageStrategy checkDamageStrategy;
    protected DrawAmmoStrategy drawAmmoStrategy;
    protected UpdateStrategy updateStrategy;
    
    public void setCheckDamageStrategy(CheckDamageStrategy checkDamageStrategy) {
    	this.checkDamageStrategy = checkDamageStrategy;
    }
    
    public void setDrawAmmoStrategy(DrawAmmoStrategy drawAmmoStrategy) {
    	this.drawAmmoStrategy = drawAmmoStrategy;
    }
    
    public void setUpdateStrategy(UpdateStrategy updateStrategy) {
    	this.updateStrategy = updateStrategy;
    }
    
    public Weapon(String name, int key, String filename, int ammoLeft, int maxAmmo, int ammoPerUse, int cooldown, boolean automatic) {
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
    
    public String getName() { return this.name; }
    public int getKey() { return this.key; }
    public BufferedImage getImage() { return this.image; } 
    public int getAmmoLeft() { return this.ammoLeft; }
    public int getMaxAmmo() { return this.maxAmmo; }    
    public abstract int getWeaponPrice();
    public abstract int getAmmoPrice();
    public boolean isAutomatic() { return this.automatic; }
    public boolean hasFired() { return this.fired; }
    public void resetFire() { this.fired = false; }
    public double getCooldownPercentage() { return ((double)cooldown / (double)coolPeriod); }
    public void resetCooldown() { this.cooldown = this.coolPeriod; }
    public void cool() { if(this.cooldown > 0) this.cooldown--; }
    
    public boolean canFire() {
        boolean isAmmoLeft = (this.ammoLeft >= this.ammoPerUse);
        boolean isCoolDown = (this.cooldown != 0);
        boolean canFire = this.automatic || (!this.automatic && !this.fired);
        return (isAmmoLeft) && (!isCoolDown) && (canFire); 
    }
    public boolean ammoFull() { return this.ammoLeft == this.maxAmmo; }
    
    public void setammoLeft(int bullet) {
    	this.ammoLeft = bullet;
    }
    
    public void addAmmo(int amount) { 
        if((this.ammoLeft + amount) > this.maxAmmo) this.ammoLeft = this.maxAmmo;
        else this.ammoLeft += amount;
    }
    public abstract int getAmmoPackAmount();
    public void resetAmmo() {
        synchronized(this.particles) { this.particles.clear(); }
    }
    
    public List<Particle> getParticles() { return this.particles; }
    
    public List<LightSource> getLights() { return null; }
    
    public abstract void updateWeapon(List<Zombie> zombies);
    
    public abstract void drawAmmo(Graphics2D g2d);
    
    public abstract void fire(double theta, Point2D.Double pos, Player player);
    
    public abstract int checkForDamage(Rectangle2D.Double rect);
    
    public void consumeAmmo() {
        this.ammoLeft -= this.ammoPerUse;
    }
}
