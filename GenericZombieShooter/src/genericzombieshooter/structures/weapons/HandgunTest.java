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

public class HandgunTest {
	private static Handgun handgun;
	private static GunUpdateStrategy gunUpdateStrategy;
	
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
	
	@BeforeClass
	public static void setUp() {
		handgun = new Handgun();
		gunUpdateStrategy = new GunUpdateStrategy();
		handgun.setUpdateStrategy(gunUpdateStrategy);
	}
	
	/**
	*Purpose: Constructor test
	*Input: Handgun() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		assertNotNull(handgun);
	}
	
	/**
	*Purpose: Check weapon price of Handgun
	*Input: getWeaponPrice()
	*Expected:
	*	Return 0
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = handgun.getWeaponPrice();
		assertEquals(0, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of Handgun
	*Input: getAmmoPrice()
	*Expected:
	*	Return 0
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = handgun.getAmmoPrice();
		assertEquals(0, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of Handgun
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 0
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = handgun.getAmmoPackAmount();
		assertEquals(0, ammoPackAmount);
	}
	
	/**
	*Purpose: Check default ammo of Handgun
	*Input: resetAmmo()
	*Expected:
	*	Return 0
	*/
	@Test
	public void resetAmmoTest() {
		handgun.resetAmmo();
		assertEquals(0, handgun.getAmmoLeft());
	}
}
