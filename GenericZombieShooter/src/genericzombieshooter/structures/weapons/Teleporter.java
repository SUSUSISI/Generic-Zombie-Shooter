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
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.Particle;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Used to teleport the player to a random point on the screen.
 * @author Darin Beaudreau
 */
public class Teleporter implements WeaponStrategy {
    // Final Variables
    private static final int WEAPON_PRICE = 4000;
    private static final int AMMO_PRICE = 500;
    private static final int DEFAULT_AMMO = 1;
    private static double MIN_TELEPORT_DISTANCE = 300;
    
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
    
    public Teleporter(String name, int key, String filename, int ammoLeft, int maxAmmo, int ammoPerUse, int cooldown, boolean automatic) {
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
    public int getWeaponPrice() { return Teleporter.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return Teleporter.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() { return Teleporter.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() { this.ammoLeft = Teleporter.DEFAULT_AMMO; }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        this.cool();
    }
    
    public void fire(double theta, Point2D.Double pos, Player player) {
        if(this.canFire()) {
            Point2D.Double newPos = this.getTeleportLocation(player);
            player.move(newPos);
            this.consumeAmmo();
            this.resetCooldown();
            Sounds.TELEPORT.play();
        }
    }
    
    private Point2D.Double getTeleportLocation(Player player) {
        Point2D.Double particle = new Point2D.Double();
        boolean validPoint = false;
        while(!validPoint) {
            particle.x = Globals.r.nextDouble() * (Globals.W_WIDTH - player.width);
            particle.y = Globals.r.nextDouble() * (Globals.W_HEIGHT - player.height);
            double xD = particle.x - player.x;
            double yD = particle.y - player.y;
            double dist = Math.sqrt((xD * xD) + (yD * yD));
            if(dist >= Teleporter.MIN_TELEPORT_DISTANCE) validPoint = true;
        }
        return particle;
    }

	@Override
	public void drawAmmo(Graphics2D g2d) {
		// TODO Auto-generated method stub
		
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
	public List<LightSource> getLights() {
		return null;
	}
}
