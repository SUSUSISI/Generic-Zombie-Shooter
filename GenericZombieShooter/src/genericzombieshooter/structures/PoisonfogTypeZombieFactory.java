package genericzombieshooter.structures;

import java.awt.geom.Point2D;

import genericzombieshooter.actors.PoisonFogZombie;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;

public class PoisonfogTypeZombieFactory extends Factory {
	public PoisonfogTypeZombieFactory(Point2D.Double p_) {
		a_ = new Animation(Images.ZOMBIE_POISONFOG, 48, 48, 4, (int)p_.x, (int)p_.y, 100, 0, true);
         zombie = new PoisonFogZombie(p_, 250, 1, 2, 200, a_);
	}

}
