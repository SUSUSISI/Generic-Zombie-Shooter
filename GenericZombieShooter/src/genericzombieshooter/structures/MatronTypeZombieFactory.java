import java.awt.geom.Point2D;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.actors.ZombieMatron;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;

public class MatronTypeZombieFactory extends Factory {
	MatronTypeZombieFactory MatronTypeZombieFactory(Point2D.Double p_) {
		Animation a_ = new Animation(Images.ZOMBIE_MATRON, 64, 64, 4, (int)p_.x, (int)p_.y, 200, 0, true);
        ZombieMatron zm_ = new ZombieMatron(p_, 500, 1, 1, 350, a_);
	}
}
