package genericzombieshooter.structures.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Particle;
import kuusisto.tinysound.TinySound;

public class AssaultRifleTest {
	private static AssaultRifle assaultRifle;
	private static GunUpdateStrategy gunUpdateStrategy;
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
	
	@BeforeClass
	public static void setUp() {
		assaultRifle = new AssaultRifle();
		gunUpdateStrategy = new GunUpdateStrategy();
		assaultRifle.setUpdateStrategy(gunUpdateStrategy);
	}
	
	/**
	*Purpose: Constructor test
	*Input: AssualtRifle() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		assaultRifle = new AssaultRifle();
		assertNotNull(assaultRifle);
	}
	
	/**
	*Purpose: Check weapon price of AssualtRifle
	*Input: getWeaponPrice()
	*Expected:
	*	Return 1000
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = assaultRifle.getWeaponPrice();
		assertEquals(1000, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of AssaultRifle
	*Input: getAmmoPrice()
	*Expected:
	*	Return 200
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = assaultRifle.getAmmoPrice();
		assertEquals(200, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of AssaultRifle
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 60
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = assaultRifle.getAmmoPackAmount();
		assertEquals(60, ammoPackAmount);
	}
	
	/**
	*Purpose: Check default ammo of AssaultRifle
	*Input: resetAmmo()
	*Expected:
	*	Return 60
	*/
	@Test
	public void resetAmmoTest() {
		assaultRifle.resetAmmo();
		assertEquals(60, assaultRifle.getAmmoLeft());
	}
	
	/**
	*Purpose: AssaultRifle fire test. fire one bullet and check particle(bullet) is exists
	*Input: cool(), fire()
	*Expected:
	*	before fire -> particle list of AssaultRifle is empty
	*	after fire -> particle list of AssaultRifle is not empty
	*	return false
	*/
	@Test
	public void fireTest() {
		TinySound.init();
		for(int i = 0; i < 10; i++) assaultRifle.cool();
		assertEquals(true, assaultRifle.canFire());
		assertEquals(true, assaultRifle.getParticles().isEmpty());
		assaultRifle.fire((double)48, pos, player);
		assertEquals(false, assaultRifle.getParticles().isEmpty());
	}
}
