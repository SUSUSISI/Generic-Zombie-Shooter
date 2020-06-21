package genericzombieshooter.structures;

import java.awt.geom.Point2D;
import genericzombieshooter.actors.*;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;

public class RegularTypeZombieFactory extends Factory {
	public RegularTypeZombieFactory(Point2D.Double p_) {
		a_ = new Animation(Images.ZOMBIE_REGULAR, 48, 48, 4, (int)p_.x, (int)p_.y, 200, 0, true);
        zombie = new RegularZombie(p_, 250, 1, 1, 25, 20, a_);
	}
}
