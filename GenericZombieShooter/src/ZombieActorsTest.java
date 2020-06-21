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
	/**
	 * Purpose : health level up, damage level up, speed level up and invalid level up
	 * Input :  spendPoint (1(health) ,currentLevel(4, 6))  // current level 5 = Max level
	 * 			spendPoint (2(damage) ,currentLevel(4, 6))	// current level 5 = Max level
	 * 			spendPoint (3(speed)  ,currentLevel(4, 6))	// current level 5 = Max level
	 * Expected :
	 * 			invalid level up => return 0 -> false
	 * 			Max Health +50 = 200
	 * 			DamageBounus +0.2 = 0.2
	 * 			speed +0.2 = 2.2
	 */
	@Test
	public void testPlayerSkillLevelup() {
		Player player;
        player = new Player(100, 100, 48, 48);
        
        // 체력 레벨업
        int currentLevel = 4;
        
        player.setSkillPoints(5);
        player.spendPoint(1, currentLevel);
        player.spendPoint(1, currentLevel);
        assertEquals(200, player.getMaxHealth());
        assertEquals(0, player.getSkillPoints());
        
        
        currentLevel = 6;
        
        assertEquals(0, player.spendPoint(1, currentLevel));
        player.setSkillPoints(10);
        assertEquals(0, player.spendPoint(1, currentLevel));

        
        
        // 데미지 레벨업
        currentLevel = 4;

        player.setSkillPoints(5);
        player.spendPoint(2, currentLevel);
        assertEquals(0.2, player.getDamageBonus());
        
        currentLevel = 6;
        
        assertEquals(0, player.spendPoint(2, currentLevel));
        player.setSkillPoints(10);
        assertEquals(0, player.spendPoint(2, currentLevel));

        
        
        // 스피드 레벨업
        currentLevel = 4;

        player.setSkillPoints(5);
        player.spendPoint(3, currentLevel);
        assertEquals(2.2, player.getSpeed());
        
        currentLevel = 6;
        
        assertEquals(0, player.spendPoint(3, currentLevel));
        player.setSkillPoints(10);
        assertEquals(0, player.spendPoint(3, currentLevel));


        
        //유효하지 않는 값을 레벨업
        assertEquals(0, player.spendPoint(5, currentLevel));

	}
	/**
	 * Purpose : Move zombies to player
	 * Input : move theta(Math.atan2(100, 100) + Math.PI / 2)
	 * Expected :
	 * 			if the speed of the zombies is 1
	 * 				x = 0.7071067811865476
	 * 				y = 0.7071067811865475
	 * 			else if the speed of the zombies is 2
	 * 				x = 1.4142135623730951
	 * 				y = 1.414213562373095
	 */
	@Test
	public void testMoveZombies() {
		
    	double x = 0;
        double y = 0;
        double theta = Math.atan2(100, 100) + Math.PI / 2;
        
        Point2D.Double p_ = new Point2D.Double(x, y);

        Animation a1 = new Animation(Images.ZOMBIE_REGULAR, 48, 48, 4, (int)p_.x, (int)p_.y, 200, 0, true);
		RegularZombie zombie1 = new RegularZombie(p_, 250, 1, 1, 25, 20, a1);	
		zombie1.move(theta);
		assertEquals(0.7071067811865476, zombie1.x);
		assertEquals(0.7071067811865475, zombie1.y);
		
		Animation a2 = new Animation(Images.BOSS_ABERRATION, 128, 128, 4, (int)p_.x, (int)p_.y, 150, 0, true);        
        AberrationBoss zombie2 = new AberrationBoss(p_, 10000, 1, 1, 1000, a2);
		zombie2.move(theta);
		assertEquals(0.7071067811865476, zombie2.x);
		assertEquals(0.7071067811865475, zombie2.y);
		
		Animation a3 = new Animation(Images.ZOMBIE_ACID, 64, 64, 4, (int)p_.x, (int)p_.y, 200, 0, true);
        AcidZombie zombie3 = new AcidZombie(p_, 300, 1, 1, 100, a3);
		zombie3.move(theta);
		assertEquals(0.7071067811865476, zombie3.x);
		assertEquals(0.7071067811865475, zombie3.y);
		
		Animation a4 = new Animation(Images.ZOMBIE_DOG, 48, 48, 4, (int)p_.x, (int)p_.y, 80, 0, true);        
		DogZombie zombie4 = new DogZombie(p_, 100, 1, 2, 50, 30, a4);
		zombie4.move(theta);		
		assertEquals(1.4142135623730951, zombie4.x);
		assertEquals(1.414213562373095, zombie4.y);
		
        Animation a5 = new Animation(Images.ZOMBIE_POISONFOG, 48, 48, 4, (int)p_.x, (int)p_.y, 100, 0, true);
        PoisonFogZombie zombie5 = new PoisonFogZombie(p_, 250, 1, 2, 200, a5);
		zombie5.move(theta);
		assertEquals(1.4142135623730951, zombie5.x);
		assertEquals(1.414213562373095, zombie5.y);
		
        Animation a6 = new Animation(Images.BOSS_STITCHES, 128, 128, 4, (int)p_.x, (int)p_.y, 150, 0, true);
        StitchesBoss zombie6 = new StitchesBoss(p_, 15000, 4, 1, 3000, a6);
		zombie6.move(theta);
		assertEquals(0.7071067811865476, zombie6.x);
		assertEquals(0.7071067811865475, zombie6.y);
		
		Animation a7 = new Animation(Images.ZOMBIE_TINY, 20, 20, 2, (int)p_.x, (int)p_.y, 100, 0, true);
        TinyZombie zombie7 = new TinyZombie(p_, 100, 1, 2, 50, (ZombieMatron.EXP_VALUE / 10), a7);
		zombie7.move(theta);
		assertEquals(1.4142135623730951, zombie7.x);
		assertEquals(1.414213562373095, zombie7.y); 
		
		
		Animation a8 = new Animation(Images.BOSS_ZOMBAT, 64, 64, 4, (int)p_.x, (int)p_.y, 50, 0, true);
        
        ZombatBoss zombie8 = new ZombatBoss(p_, 2000, 1, 2, 500, a8);
		zombie8.move(theta);
		assertEquals(1.4142135623730951, zombie8.x);
		assertEquals(1.414213562373095, zombie8.y);
		
        Animation a9 = new Animation(Images.ZOMBIE_MATRON, 64, 64, 4, (int)p_.x, (int)p_.y, 200, 0, true);
        
        Zombie zombie9 = new ZombieMatron(p_, 500, 1, 1, 350, a9);
		zombie9.move(theta);
		assertEquals(0.7071067811865476, zombie9.x);
		assertEquals(0.7071067811865475, zombie9.y);
	}
	

}
