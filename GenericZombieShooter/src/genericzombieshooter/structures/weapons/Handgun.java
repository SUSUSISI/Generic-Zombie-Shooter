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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Used to handle the Handgun weapon.
 * @author Darin Beaudreau
 */
public class Handgun extends Weapon{
    // Final Variables
    private static final int WEAPON_PRICE = 0;
    private static final int AMMO_PRICE = 0;
    private static final int DEFAULT_AMMO = 0;
    private static final int MAX_AMMO = 0;
    private static final int AMMO_PER_USE = 0;
    private static final int DAMAGE_PER_PARTICLE = 75;
    private static final double PARTICLE_SPREAD = 3.0;
    private static final int PARTICLE_LIFE = 1000;
    
    private GunUpdateStrategy gunUpdateStrategy = new GunUpdateStrategy();
    private GunDrawAmmoStrategy gunDrawAmmoStrategy = new GunDrawAmmoStrategy();
    private GunCheckDamageStrategy gunCheckDamageStrategy = new GunCheckDamageStrategy();
    
    public Handgun() {
        super("Popgun", KeyEvent.VK_1, "/resources/images/GZS_Popgun.png", 
              Handgun.DEFAULT_AMMO, Handgun.MAX_AMMO, Handgun.AMMO_PER_USE, 10, false);
    }
    
    @Override
    public int getWeaponPrice() { return Handgun.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return Handgun.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() {
        return Handgun.DEFAULT_AMMO;
    }
    
    @Override
    public void resetAmmo() {
        super.resetAmmo();
        this.ammoLeft = Handgun.DEFAULT_AMMO;
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
    	this.setUpdateStrategy(gunUpdateStrategy);
    	this.updateStrategy.updateWeawpon(this.particles);
    	this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        // Draw all particles whose life has not yet expired.
    	this.setDrawAmmoStrategy(gunDrawAmmoStrategy);
        this.drawAmmoStrategy.drawAmmo(g2d, this.particles, Color.ORANGE);
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        // If there is enough ammo left...
        if(this.canFire()) {
            // Create a new bullet and add it to the list.
            int width = 4;
            int height = 10;
            Particle particle = new Particle(theta, Handgun.PARTICLE_SPREAD, 8.0,
                          (Handgun.PARTICLE_LIFE / (int)Globals.SLEEP_TIME), new Point2D.Double(pos.x, pos.y),
                           new Dimension(width, height), Images.POPGUN_BULLET);
            this.particles.add(particle);
            // Use up ammo.
            this.consumeAmmo();
            this.resetCooldown();
            this.fired = true;
            Sounds.POPGUN.play();
        }
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        this.setCheckDamageStrategy(gunCheckDamageStrategy);
        return this.gunCheckDamageStrategy.gunCheckForDamage(rect, this.particles, DAMAGE_PER_PARTICLE);
    }
}
