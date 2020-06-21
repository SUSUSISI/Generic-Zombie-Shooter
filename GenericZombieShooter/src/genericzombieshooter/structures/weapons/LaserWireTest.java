package genericzombieshooter.structures.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.Point2D;

import org.junit.Test;

import genericzombieshooter.actors.Player;
import kuusisto.tinysound.TinySound;

public class LaserWireTest {
	private static LaserWire laserWire;
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
	
	/**
	*Purpose: Constructor test
	*Input: LaserWire() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		laserWire = new LaserWire();
		assertNotNull(laserWire);
	}
	
	/**
	*Purpose: Check weapon price of LaserWire
	*Input: getWeaponPrice()
	*Expected:
	*	Return 1500
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = laserWire.getWeaponPrice();
		assertEquals(1500, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of LaserWire
	*Input: getAmmoPrice()
	*Expected:
	*	Return 400
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = laserWire.getAmmoPrice();
		assertEquals(400, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of LaserWire
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 1
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = laserWire.getAmmoPackAmount();
		assertEquals(1, ammoPackAmount);
	}
	
	/**
	*Purpose: Check default ammo of LaserWire
	*Input: resetAmmo()
	*Expected:
	*	Return 1
	*/
	@Test
	public void resetAmmoTest() {
		laserWire.resetAmmo();
		assertEquals(1, laserWire.getAmmoLeft());
	}
}
