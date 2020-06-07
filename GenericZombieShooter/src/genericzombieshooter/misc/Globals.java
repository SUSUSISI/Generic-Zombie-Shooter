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
package genericzombieshooter.misc;

import genericzombieshooter.misc.progress.Progress;
import genericzombieshooter.structures.GameTime;
import genericzombieshooter.structures.Message;
import genericzombieshooter.structures.weapons.AssaultRifle;
import genericzombieshooter.structures.weapons.Flamethrower;
import genericzombieshooter.structures.weapons.Flare;
import genericzombieshooter.structures.weapons.Grenade;
import genericzombieshooter.structures.weapons.Handgun;
import genericzombieshooter.structures.weapons.Landmine;
import genericzombieshooter.structures.weapons.LaserWire;
import genericzombieshooter.structures.weapons.Shotgun;
import genericzombieshooter.structures.weapons.Teleporter;
import genericzombieshooter.structures.weapons.TurretWeapon;
import genericzombieshooter.structures.weapons.WeaponStrategy;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains publicly accessible variables for the entire project.
 * @author Darin Beaudreau
 */
public class Globals {
    // Game Information
    public static final String VERSION = "1.0";
    public static final int W_WIDTH = 800; // The width of the game window.
    public static final int W_HEIGHT = 640; // The height of the game window.
    public static final long SLEEP_TIME = 20; // The sleep time of the animation thread.
    public static final long WAVE_BREAK_TIME = 30 * 1000;
    public static final Random r = new Random();
    public static List<Message> GAME_MESSAGES = new ArrayList<Message>();
    
    // Zombie Information
    public static final int ZOMBIE_REGULAR_TYPE = 1;
    public static final long ZOMBIE_REGULAR_SPAWN = 1000;
    public static final int ZOMBIE_DOG_TYPE = 2;
    public static final long ZOMBIE_DOG_SPAWN = 10000;
    public static final int ZOMBIE_ACID_TYPE = 3;
    public static final long ZOMBIE_ACID_SPAWN = 30000;
    public static final int ZOMBIE_POISONFOG_TYPE = 4;
    public static final long ZOMBIE_POISONFOG_SPAWN = 20000;
    public static final int ZOMBIE_MATRON_TYPE = 5;
    public static final long ZOMBIE_MATRON_SPAWN = 50000;
    public static final int ZOMBIE_TINY_TYPE = 6;
    
    // Boss Information
    public static final int ZOMBIE_BOSS_ABERRATION_TYPE = 7;
    public static final int ZOMBIE_BOSS_ZOMBAT_TYPE = 8;
    public static final int ZOMBIE_BOSS_STITCHES_TYPE = 9;
    
    // Game-State Related
    public static Thread mainThread;
    public static Runnable animation; // The primary animation thread.
    public static GameTime gameTime = new GameTime(); // Used to keep track of the time.
    
    


   
    
    
    
    public static long nextWave;
    public static Progress progress = new Progress();
    
    public static void resetState() {
    	gameTime.reset();
        progress.reset();
        nextWave = Globals.gameTime.getElapsedMillis() + 3000;
    }
    
    // Input Related
    public static boolean [] keys; // The state of the game key controls.
    public static boolean [] buttons; // The state of the game mouse button controls.
    public static Point mousePos; // The current position of the mouse on the screen.
    
    public static void resetInput() {
    	Globals.keys = new boolean[4];
        for (boolean k : Globals.keys) k = false;
        Globals.buttons = new boolean[2];
        for (boolean b : Globals.buttons) b = false;

        Globals.mousePos = new Point(0, 0);
    }
    
    
    // Static Weapons
    public static Handgun HANDGUN = new Handgun("Popgun", KeyEvent.VK_1, "/resources/images/GZS_Popgun.png", 
            0, 0, 0, 10, false);
    public static AssaultRifle ASSAULT_RIFLE = new AssaultRifle("RTPS", KeyEvent.VK_2, "/resources/images/GZS_RTPS.png", 
            60, 300, 1, 
            10, true);
    public static Shotgun SHOTGUN = new Shotgun("Boomstick", KeyEvent.VK_3, "/resources/images/GZS_Boomstick.png", 
            24, 64, 1, 40, false);
    public static Flamethrower FLAMETHROWER = new Flamethrower("The Flammenwerfer", KeyEvent.VK_4, "/resources/images/GZS_Flammenwerfer.png", 
            100, 300, 1, 0, true);
    public static Grenade GRENADE = new Grenade("Hand Egg", KeyEvent.VK_5, "/resources/images/GZS_HandEgg.png", 
            1, 3, 1, 100, false);
    public static Landmine LANDMINE = new Landmine("Flip-Flop", KeyEvent.VK_6, "/resources/images/GZS_FlipFlop.png",
            1, 3, 1, 50, false);
    public static Flare FLARE = new Flare("Shiny Stick", KeyEvent.VK_7, "/resources/images/GZS_Flare.png",
            1, 3, 1, 100, false);
    public static LaserWire LASERWIRE = new LaserWire("Laser Wire", KeyEvent.VK_8, "/resources/images/GZS_LaserWire.png",
            1, 1, 1, 50, false);
    public static TurretWeapon TURRETWEAPON = new TurretWeapon("Sentry Gun", KeyEvent.VK_9, "/resources/images/GZS_Turret.png",
            1, 1, 1, 50, false);
    public static Teleporter TELEPORTER = new Teleporter("Big Red Button", KeyEvent.VK_0, "/resources/images/GZS_BigRedButton.png",
            1, 1, 1, (int)((60 * 1000) / Globals.SLEEP_TIME), false);
    
    public static WeaponStrategy getWeaponByName(String name) {
        if(name.equals(Globals.HANDGUN.getName())) return Globals.HANDGUN;
        else if(name.equals(Globals.ASSAULT_RIFLE.getName())) return Globals.ASSAULT_RIFLE;
        else if(name.equals(Globals.SHOTGUN.getName())) return Globals.SHOTGUN;
        else if(name.equals(Globals.FLAMETHROWER.getName())) return Globals.FLAMETHROWER;
        else if(name.equals(Globals.GRENADE.getName())) return Globals.GRENADE;
        else if(name.equals(Globals.LANDMINE.getName())) return Globals.LANDMINE;
        else if(name.equals(Globals.FLARE.getName())) return Globals.FLARE;
        else if(name.equals(Globals.LASERWIRE.getName())) return Globals.LASERWIRE;
        else if(name.equals(Globals.TURRETWEAPON.getName())) return Globals.TURRETWEAPON;
        else if(name.equals(Globals.TELEPORTER.getName())) return Globals.TELEPORTER;
        else return null;
    }
    
    public static void resetWeapons() {
        Globals.HANDGUN.resetAmmo();
        Globals.ASSAULT_RIFLE.resetAmmo();
        Globals.SHOTGUN.resetAmmo();
        Globals.FLAMETHROWER.resetAmmo();
        Globals.GRENADE.resetAmmo();
        Globals.LANDMINE.resetAmmo();
        Globals.FLARE.resetAmmo();
        Globals.LASERWIRE.resetAmmo();
        Globals.TURRETWEAPON.resetAmmo();
        Globals.TELEPORTER.resetAmmo();
    }
    
    public static WeaponStrategy weaponStrategy = Globals.HANDGUN;
    
    public static WeaponStrategy getWeaponStrategy(String name) {
    	return getWeaponByName(name);
    }
    public static void setWeaponStrategy(String name) {
    	Globals.weaponStrategy =  getWeaponByName(name);
    }
}
