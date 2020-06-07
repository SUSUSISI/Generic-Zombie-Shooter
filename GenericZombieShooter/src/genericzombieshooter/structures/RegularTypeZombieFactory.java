import java.awt.geom.Point2D;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;

public class RegularTypeZombieFactory extends Factory {
	RegularTypeZombieFactory RegularTypeZombieFactory(Point2D.Double p_) {
		Animation a_ = new Animation(Images.ZOMBIE_REGULAR, 48, 48, 4, (int)p_.x, (int)p_.y, 200, 0, true);
        Zombie z_ = new Zombie(p_, Globals.ZOMBIE_REGULAR_TYPE, 250, 1, 1, 25, 20, a_);
	}
}
