package genericzombieshooter.structures.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.Point2D;

import org.junit.Test;

import genericzombieshooter.actors.Player;
import kuusisto.tinysound.TinySound;

public class FlamethrowerTest {
	private static Flamethrower flamethrower;
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
	/**
	*Purpose: Constructor test
	*Input: Flamethrower() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		flamethrower = new Flamethrower();
		assertNotNull(flamethrower);
	}
	
	/**
	*Purpose: Check weapon price of Flamethrower
	*Input: getWeaponPrice()
	*Expected:
	*	Return 3000
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = flamethrower.getWeaponPrice();
		assertEquals(3000, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of Flamethrower
	*Input: getAmmoPrice()
	*Expected:
	*	Return 500
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = flamethrower.getAmmoPrice();
		assertEquals(500, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of Flamethrower
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 100
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = flamethrower.getAmmoPackAmount();
		assertEquals(100, ammoPackAmount);
	}
	
	/**
	*Purpose: Check default ammo of Flamethrower
	*Input: resetAmmo()
	*Expected:
	*	Return 100
	*/
	@Test
	public void resetAmmoTest() {
		flamethrower.resetAmmo();
		assertEquals(100, flamethrower.getAmmoLeft());
	}
}
