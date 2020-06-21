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

public class ShotgunTest {
	private static Shotgun shotgun;
	private static GunUpdateStrategy gunUpdateStrategy;
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
	
	@BeforeClass
	public static void setUp() {
		shotgun = new Shotgun();
		gunUpdateStrategy = new GunUpdateStrategy();
		shotgun.setUpdateStrategy(gunUpdateStrategy);
	}
	/**
	*Purpose: Constructor test
	*Input: Shotgun() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		shotgun = new Shotgun();
		assertNotNull(shotgun);
	}
	
	/**
	*Purpose: Check weapon price of Shotgun
	*Input: getWeaponPrice()
	*Expected:
	*	Return 1200
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = shotgun.getWeaponPrice();
		assertEquals(1200, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of Shotgun
	*Input: getAmmoPrice()
	*Expected:
	*	Return 200
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = shotgun.getAmmoPrice();
		assertEquals(200, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of Shotgun
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 24
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = shotgun.getAmmoPackAmount();
		assertEquals(24, ammoPackAmount);
	}
	
	/**
	*Purpose: Check default ammo of Shotgun
	*Input: resetAmmo()
	*Expected:
	*	Return 24
	*/
	@Test
	public void resetAmmoTest() {
		shotgun.resetAmmo();
		assertEquals(24, shotgun.getAmmoLeft());
	}
}
