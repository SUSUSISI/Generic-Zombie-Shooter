package genericzombieshooter.structures;

import genericzombieshooter.actors.AberrationBoss;
import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.RegularZombie;
import genericzombieshooter.actors.TinyZombie;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.actors.ZombieMatron;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.ZombieWave;
import genericzombieshooter.structures.items.HealthPack;
import genericzombieshooter.structures.weapons.Handgun;
import genericzombieshooter.structures.weapons.WeaponStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class ZombieWaveTest {
	ZombieWave wave;
 	ItemFactory itemFactory = new ItemFactory();
	Player player = new Player(((Globals.W_WIDTH / 2) - 24), ((Globals.W_HEIGHT / 2) - 24), 48, 48);
    
	
	/*
     * Purpose : Check to make the right zombie wave
     * Input : Zombie waves from 1 to 100
     * Expected:
     * 		
     * 		This game generates a boss every 15 waves.
     * 		So, normal zombies should be produced in wave, not multiple of 15,
     * 		only boss zombies should be produced in wave, which is multiple of wave.
     * 		
     * 		If violate this, a message appears.
    
    */
	@Test
	public void ZombieWaveTest() {
		
		//for Check normal wave
		for(int i = 1; i <= 100; i++) {
			if(i%15 == 0) continue;
			wave = new ZombieWave(i);

			List<Zombie> recentZombiewave = wave.getUnbornZombies();
			
			for(int j = 0; j < recentZombiewave.size(); j++) {
				assertFalse("This wave is not a boss wave",recentZombiewave.get(j).getType() == Globals.ZOMBIE_BOSS_ABERRATION_TYPE);
				assertFalse("This wave is not a boss wave",recentZombiewave.get(j).getType() == Globals.ZOMBIE_BOSS_ZOMBAT_TYPE);
				assertFalse("This wave is not a boss wave",recentZombiewave.get(j).getType() == Globals.ZOMBIE_BOSS_STITCHES_TYPE);
			}
		}
		
		//for Check boss wave
		for(int i = 15; i <= 100; i+=15) {
			wave = new ZombieWave(i);
			List<Zombie> recentZombiewave = wave.getUnbornZombies();
			for(int j = 0; j < recentZombiewave.size(); j++) {
				assertFalse("This wave is a boss wave", recentZombiewave.get(j).getType() == Globals.ZOMBIE_REGULAR_TYPE);
				assertFalse("This wave is a boss wave", recentZombiewave.get(j).getType() == Globals.ZOMBIE_DOG_TYPE);
				assertFalse("This wave is a boss wave", recentZombiewave.get(j).getType() == Globals.ZOMBIE_ACID_TYPE);
				assertFalse("This wave is a boss wave", recentZombiewave.get(j).getType() == Globals.ZOMBIE_POISONFOG_TYPE);
				assertFalse("This wave is a boss wave", recentZombiewave.get(j).getType() == Globals.ZOMBIE_MATRON_TYPE);
			}
		}

	}
	/*
     * Purpose : To Check that zombies are created correctly at each spawn time within one wave
     * Input : Zombie wave 1
     * Expected:
     * 		
     * 		When a new wave starts, it saves the zombies that will be created in the UnbornZombie list, 
     * 		and checks to see if each zombie is created after the sponsor time.
    
    */
	@Test
	public void updateTest() {
		
		wave = new ZombieWave(1);

		List<Zombie> UnbornZombie = wave.getUnbornZombies();
        assertEquals(3, UnbornZombie.size());
		
        Globals.gameTime.setElapsedMillis(Globals.gameTime.getElapsedMillis()+ wave.getnextZombieSpawn());
		wave.update(player, itemFactory);
		
		List<Zombie> UnbornZombie2 = wave.getUnbornZombies();
		List<Zombie> AliveZombie = wave.getZombies();
		
        
        assertEquals(2, UnbornZombie2.size()); 
		assertEquals(1, AliveZombie.size());
	}
        
}
