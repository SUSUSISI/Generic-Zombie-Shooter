package genericzombieshooter.structures.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.Point2D;

import org.junit.Test;

import genericzombieshooter.actors.Player;
import kuusisto.tinysound.TinySound;

public class LandmineTest {
	private static Landmine landmine;
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
	
	/**
	*Purpose: Constructor test
	*Input: Landmine() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		landmine = new Landmine();
		assertNotNull(landmine);
	}
	
	/**
	*Purpose: Check weapon price of Landmine
	*Input: getWeaponPrice()
	*Expected:
	*	Return 750
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = landmine.getWeaponPrice();
		assertEquals(750, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of Landmine
	*Input: getAmmoPrice()
	*Expected:
	*	Return 400
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = landmine.getAmmoPrice();
		assertEquals(400, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of Landmine
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 1
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = landmine.getAmmoPackAmount();
		assertEquals(1, ammoPackAmount);
	}
	
	/**
	*Purpose: Check default ammo of Landmine
	*Input: resetAmmo()
	*Expected:
	*	Return 1
	*/
	@Test
	public void resetAmmoTest() {
		landmine.resetAmmo();
		assertEquals(1, landmine.getAmmoLeft());
	}
	
	/**
	*Purpose: Test Landmine can fire more than ammoLeft
	*Input: cool(), fire()
	*Expected:
	*	Landmine default ammo = 1
	*	fire 1 time, and then check Landmine whether can fired
	*	return false
	*/
	@Test
	public void fireTest() {
		TinySound.init();
		for(int i = 0; i < 50; i++) landmine.cool();
		assertEquals(true, landmine.canFire());
		landmine.fire((double)48, pos, player);
		assertEquals(false, landmine.canFire());
	}
}
