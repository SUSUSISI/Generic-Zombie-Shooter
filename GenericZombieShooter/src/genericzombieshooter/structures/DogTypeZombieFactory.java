package genericzombieshooter.structures;

import java.awt.geom.Point2D;
import genericzombieshooter.actors.*;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;

public class DogTypeZombieFactory extends Factory {
	public DogTypeZombieFactory(Point2D.Double p_) {
		a_ = new Animation(Images.ZOMBIE_DOG, 48, 48, 4, (int)p_.x, (int)p_.y, 80, 0, true);
        zombie = new DogZombie(p_,100, 1, 2, 50, 30, a_);
	}
}
