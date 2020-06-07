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
import genericzombieshooter.misc.Sounds;
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
 * Used to drop turrets on the field.
 * @author Darin Beaudreau
 */
public class TurretWeapon implements WeaponStrategy {
    // Final Variables
    private static final int WEAPON_PRICE = 4000;
    private static final int AMMO_PRICE = 1200;
    private static final int DEFAULT_AMMO = 1;
    private static final long TURRET_LIFE = 60 * 1000;
    
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
    private List<Turret> turrets;
    
    public TurretWeapon(String name, int key, String filename, int ammoLeft, int maxAmmo, int ammoPerUse, int cooldown, boolean automatic) {
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
        this.turrets = Collections.synchronizedList(new ArrayList<Turret>());
    }
    
    @Override
    public int getWeaponPrice() { return TurretWeapon.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return TurretWeapon.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() { return TurretWeapon.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() {
        synchronized(this.turrets) {
            Iterator<Turret> it = this.turrets.iterator();
            while(it.hasNext()) {
                Turret t = it.next();
                t.reset();
            }
            this.turrets.clear(); 
        }
        this.ammoLeft = TurretWeapon.DEFAULT_AMMO;
    }
    
    private boolean checkFire() {
    	boolean isAmmoLeft = (this.ammoLeft >= this.ammoPerUse);
        boolean isCoolDown = (this.cooldown != 0);
        boolean canFire = this.automatic || (!this.automatic && !this.fired);
        return (isAmmoLeft) && (!isCoolDown) && (canFire); 
    }
    
    @Override
    public boolean canFire() {
        boolean ableFire = checkFire();
        boolean turretsEmpty = this.turrets.isEmpty();
        //System.out.println("SuperBool: " + superBool + ", TurretsEmpty: " + turretsEmpty);
        return ableFire && turretsEmpty; 
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        synchronized(this.turrets) {
            if(!this.turrets.isEmpty()) {
                Iterator<Turret> turretIterator = this.turrets.iterator();
                while(turretIterator.hasNext()) {
                    Turret turret = turretIterator.next();
                    if(turret.isAlive()) turret.update(zombies);
                    else {
                        turretIterator.remove();
                        continue;
                    }
                }
            }
        }
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        synchronized(this.turrets) {
            if(!this.turrets.isEmpty()) {
                Iterator<Turret> turretIterator = this.turrets.iterator();
                while(turretIterator.hasNext()) {
                    Turret turret = turretIterator.next();
                    if(turret.isAlive()) turret.draw(g2d);
                }
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        if(this.canFire()) {
            this.turrets.add(new Turret(pos, TurretWeapon.TURRET_LIFE));
            this.consumeAmmo();
            this.resetCooldown();
            Sounds.LANDMINE_ARMED.play();
        }
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        int damage = 0;
        synchronized(this.turrets) {
            if(!this.turrets.isEmpty()) {
                Iterator<Turret> turretIterator = this.turrets.iterator();
                while(turretIterator.hasNext()) {
                    Turret tururet = turretIterator.next();
                    if(tururet.isAlive()) damage += tururet.checkForDamage(rect);
                }
            }
        }
        return damage;
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
