package genericzombieshooter.structures.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.Point2D;

import org.junit.Test;

import genericzombieshooter.actors.Player;
import kuusisto.tinysound.TinySound;

public class FlareTest {
	private static Flare flare;
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
	
	/**
	*Purpose: Constructor test
	*Input: Flare() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		flare = new Flare();
		assertNotNull(flare);
	}
	
	/**
	*Purpose: Check weapon price of Flare
	*Input: getWeaponPrice()
	*Expected:
	*	Return 400
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = flare.getWeaponPrice();
		assertEquals(400, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of Flare
	*Input: getAmmoPrice()
	*Expected:
	*	Return 200
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = flare.getAmmoPrice();
		assertEquals(200, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of Flare
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 1
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = flare.getAmmoPackAmount();
		assertEquals(1, ammoPackAmount);
	}
	
	/**
	*Purpose: Check default ammo of Flare
	*Input: resetAmmo()
	*Expected:
	*	Return 1
	*/
	@Test
	public void resetAmmoTest() {
		flare.resetAmmo();
		assertEquals(1, flare.getAmmoLeft());
	}
}
