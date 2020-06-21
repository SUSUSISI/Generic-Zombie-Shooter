import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

import genericzombieshooter.actors.AberrationBoss;
import genericzombieshooter.actors.AcidZombie;
import genericzombieshooter.actors.DogZombie;
import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.PoisonFogZombie;
import genericzombieshooter.actors.RegularZombie;
import genericzombieshooter.actors.StitchesBoss;
import genericzombieshooter.actors.TinyZombie;
import genericzombieshooter.actors.ZombatBoss;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.actors.ZombieMatron;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;
import kuusisto.tinysound.TinySound;

class ZombieActorsTest {
	/**
	 * purpose : Move player to up, left, down, right and invalid direction
	 * input :  move(0) => y -= 2
	 * 			move(1) => x -= 2
	 * 			move(2) => y += 2
	 * 			move(3) => x += 2
	 * 			move(4) => nothing
	 * Expected:
	 * 			x = 100, y =  98
	 * 			x =  98, y =  98
	 * 			x =  98, y = 100
	 * 			x = 100, y = 100
	 * 			x = 100, y = 100
	 */
	@Test
	public void testPlayerMove() {
		Player player;
        player = new Player(100, 100, 48, 48);

        player.move(0);
        assertEquals(100,player.x);
        assertEquals(98,player.y);
        
        player.move(1);
        assertEquals(98,player.x);
        assertEquals(98,player.y);
        
        player.move(2);
        assertEquals(98,player.x);
        assertEquals(100,player.y);
        
        player.move(3);
        assertEquals(100,player.x);
        assertEquals(100,player.y);
        
        player.move(4);
        assertEquals(100,player.x);
        assertEquals(100,player.y);
	}
	/**
	 * Purpose : Make sure the player is alive
	 * Input :  takeDamage 149
	 * 		    takeDamage 2
	 * Expected :
	 * 			if damage is less than health
	 * 				return true
	 * 			else
	 * 				return false
	 */
	@Test
	public void testPlayerisAlive() {
		Player player;
        player = new Player(100, 100, 48, 48);
		
		player.takeDamage(149);
        assertEquals(true,player.isAlive());
        player.takeDamage(2);
        assertEquals(false,player.isAlive());
	}
	/**
	 * Purpose : Count the number that the player kill
	 * Input : addkill, addkill
	 * Expected :
	 * 			killCount = 2
	 * 
	 */
	@Test
	public void testPlayerkillCount() {
		Player player;
        player = new Player(100, 100, 48, 48);
        
        player.addKill();
        player.addKill();
        assertEquals(2, player.killCount);
	}
	/**
	 * Purpose : decrease, reset or increase the life of the player
	 * Input : die(), die(), die(), reset(), addLife()
	 * Expected : 
	 * 			Lives = 2
	 * 			Lives = 3
	 * 			Lives = 4
	 */
	@Test
	public void testPlayerLives() {
		Player player;
        player = new Player(100, 100, 48, 48);
        
        player.die();
        assertEquals(2, player.getLives());
        
        player.die();
        player.die();
        player.reset();
        assertEquals(3, player.getLives());
        
        player.addLife();
        assertEquals(4, player.getLives());
	}
	/**
	 * Purpose : Add the health of player
	 * Input :	addHealth(50), addHealth(1000)
	 * Expected :
	 * 			if the addHealth of player is less than the maxHealth of player, Add all extra health
	 * 			else Add health to maxHealth
	 * 				player health = 100
	 * 				player health = 150
	 */
	@Test
	public void testPlayerAddHealth() {
		Player player;
        player = new Player(100, 100, 48, 48);
        
		player.takeDamage(100);
		player.addHealth(50);
       
		assertEquals(100, player.getHealth());
		player.addHealth(1000);
		assertEquals(150, player.getHealth());
	}
	/**
	 * Purpose : Add or remove the Cash of player
	 * Input :	addCash(100), removeCash(50)
	 * Expected :
	 * 				player Cash = 100
	 * 				player Cash = 50
	 */
	@Test
	public void testPlayerCash() {
		Player player;
        player = new Player(100, 100, 48, 48);
        
		player.addCash(100);
		assertEquals(100, player.getCash());
		
		player.removeCash(50);
		assertEquals(50, player.getCash());
		
	}
	/**
	 * Purpose : add experience and player level up
	 * Input : addExp 1200 , checkLevel()
	 * Expected :
	 * 				Exp  0 -> 1200
	 * 			player level up
	 * 				Level  		1 -> 2
	 * 				Experience	1200 -> 200
	 * 				SkillPoints 0 -> 1
	 * 
	 */
	@Test
	public void testPlayerExp() {
		Player player;
        player = new Player(100, 100, 48, 48);
        
        player.addExp(1200);
        assertEquals(1200, player.getExp());
        player.checkLevel();
        assertEquals(2, player.getLevel());
        assertEquals(200, player.getExp());
        assertEquals(1, player.getSkillPoints());
        
	}
	/**
	 * Purpose : reset Statistics
	 * Input : ammoCratesUsed = 5
	 * 		   medkitsUsed = 3
     *         killCount = 3
	 * Expected :
	 * 			ammoCratesUsed 5 -> 0
	 * 			medkitsUsed    3 -> 0
     *          killCount      3 -> 0
	 */
	@Test
	public void testPlayerResetStatistics() {
		Player player;
        player = new Player(100, 100, 48, 48);
        
        player.ammoCratesUsed =5;
        player.medkitsUsed = 3;
        player.killCount = 3;
        player.resetStatistics();
        assertEquals(0, player.ammoCratesUsed);
        assertEquals(0, player.medkitsUsed);
        assertEquals(0, player.killCount);
        
	}
	

}
