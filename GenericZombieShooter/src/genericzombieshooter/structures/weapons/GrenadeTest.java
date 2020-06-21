package genericzombieshooter.structures.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.Point2D;

import org.junit.Test;

import genericzombieshooter.actors.Player;
import kuusisto.tinysound.TinySound;

public class GrenadeTest {
	private static Grenade grenade;
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
	
	/**
	*Purpose: Constructor test
	*Input: Grenade() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		grenade = new Grenade();
		assertNotNull(grenade);
	}
	
	/**
	*Purpose: Check weapon price of Grenade
	*Input: getWeaponPrice()
	*Expected:
	*	Return 800
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = grenade.getWeaponPrice();
		assertEquals(800, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of Grenade
	*Input: getAmmoPrice()
	*Expected:
	*	Return 500
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = grenade.getAmmoPrice();
		assertEquals(500, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of Grenade
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 1
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = grenade.getAmmoPackAmount();
		assertEquals(1, ammoPackAmount);
	}
	
	/**
	*Purpose: Check default ammo of Grenade
	*Input: resetAmmo()
	*Expected:
	*	Return 1
	*/
	@Test
	public void resetAmmoTest() {
		grenade.resetAmmo();
		assertEquals(1, grenade.getAmmoLeft());
	}
	
	/**
	*Purpose: Test Grenade can fire more than ammoLeft
	*Input: 1 
	*Expected: cool(), fire()
	*	Grenade default ammo = 1
	*	fire 1 time, and then check Grenade whether can fired
	*	return false
	*/
	@Test
	public void fireTest() {
		TinySound.init();
		for(int i = 0; i < 100; i++) grenade.cool();
		assertEquals(true, grenade.canFire());
		grenade.fire((double)48, pos, player);
		assertEquals(false, grenade.canFire());
	}
}
