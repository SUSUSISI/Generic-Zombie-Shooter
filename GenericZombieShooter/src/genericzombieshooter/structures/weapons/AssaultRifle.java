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
import genericzombieshooter.structures.items.UnlimitedAmmo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Used to represent the Assault Rifle weapon.
 * @author Darin Beaudreau
 */
public class AssaultRifle extends Weapon {
    // Final Variables
    private static final int WEAPON_PRICE = 1000;
    private static final int AMMO_PRICE = 200;
    private static final int DEFAULT_AMMO = 60;
    private static final int MAX_AMMO = 300;
    private static final int AMMO_PER_USE = 1;
    private static final int DAMAGE_PER_PARTICLE = 100;
    private static final double PARTICLE_SPREAD = 5.0;
    private static final int PARTICLE_LIFE = 2000;
    
    private GunUpdateStrategy gunUpdateStrategy = new GunUpdateStrategy();
    private GunDrawAmmoStrategy gunDrawAmmoStrategy = new GunDrawAmmoStrategy();
    private GunCheckDamageStrategy gunCheckDamageStrategy = new GunCheckDamageStrategy();
    
    public AssaultRifle() {
        super("RTPS", KeyEvent.VK_2, "/resources/images/GZS_RTPS.png", 
              AssaultRifle.DEFAULT_AMMO, AssaultRifle.MAX_AMMO, AssaultRifle.AMMO_PER_USE, 
              10, true);
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
        super.resetAmmo();
        this.ammoLeft = AssaultRifle.DEFAULT_AMMO;
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
        this.drawAmmoStrategy.drawAmmo(g2d, this.particles, Color.ORANGE);
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
    	this.setCheckDamageStrategy(gunCheckDamageStrategy);
        return this.gunCheckDamageStrategy.gunCheckForDamage(rect, this.particles, DAMAGE_PER_PARTICLE);
    }
}
