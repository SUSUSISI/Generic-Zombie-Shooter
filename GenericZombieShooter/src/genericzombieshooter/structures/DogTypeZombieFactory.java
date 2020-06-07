import java.awt.geom.Point2D;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;

public class DogTypeZombieFactory extends Factory {
	DogTypeZombieFactory DogTypeZombieFactory(Point2D.Double p_) {
		Animation a_ = new Animation(Images.ZOMBIE_DOG, 48, 48, 4, (int)p_.x, (int)p_.y, 80, 0, true);
        Zombie z_ = new Zombie(p_, Globals.ZOMBIE_DOG_TYPE, 100, 1, 2, 50, 30, a_);
	}
}
