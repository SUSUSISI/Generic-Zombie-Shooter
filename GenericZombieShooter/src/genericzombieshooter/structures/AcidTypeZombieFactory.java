import java.awt.geom.Point2D;

import genericzombieshooter.actors.AcidZombie;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;

public class AcidTypeZombieFactory extends Factory  {
	AcidTypeZombieFactory AcidTypeZombieFactory(Point2D.Double p_) {
		Animation a_ = new Animation(Images.ZOMBIE_ACID, 64, 64, 4, (int)p_.x, (int)p_.y, 200, 0, true);
        AcidZombie z_ = new AcidZombie(p_, 300, 1, 1, 100, a_);
	}
}
