package genericzombieshooter.structures.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.Point2D;

import org.junit.Test;

import genericzombieshooter.actors.Player;
import kuusisto.tinysound.TinySound;

public class TurretWeaponTest {
	private static TurretWeapon turretWeapon;
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
	
	/**
	*Purpose: Constructor test
	*Input: TurretWeapon() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		turretWeapon = new TurretWeapon();
		assertNotNull(turretWeapon);
	}
	
	/**
	*Purpose: Check weapon price of TurretWeapon
	*Input: getWeaponPrice()
	*Expected:
	*	Return 4000
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = turretWeapon.getWeaponPrice();
		assertEquals(4000, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of TurretWeapon
	*Input: getAmmoPrice()
	*Expected:
	*	Return 1200
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = turretWeapon.getAmmoPrice();
		assertEquals(1200, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of TurretWeapon
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 1
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = turretWeapon.getAmmoPackAmount();
		assertEquals(1, ammoPackAmount);
	}
	
	/**
	*Purpose: Check default ammo of TurretWeapon
	*Input: resetAmmo()
	*Expected:
	*	Return 1
	*/
	@Test
	public void resetAmmoTest() {
		turretWeapon.resetAmmo();
		assertEquals(1, turretWeapon.getAmmoLeft());
	}
	
	/**
	*Purpose: Test TurretWeapon can fire more than ammoLeft
	*Input: cool(), fire()
	*Expected:
	*	TurretWeapon default ammo = 1
	*	fire 1 time, and then check canFire
	*	return false
	*/
	@Test
	public void fireTest() {
		TinySound.init();
		for(int i = 0; i < 50; i++) turretWeapon.cool();
		assertEquals(true, turretWeapon.canFire());
		turretWeapon.fire((double)48, pos, player);
		assertEquals(false, turretWeapon.canFire());
	}
}
