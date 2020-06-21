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
	

}
