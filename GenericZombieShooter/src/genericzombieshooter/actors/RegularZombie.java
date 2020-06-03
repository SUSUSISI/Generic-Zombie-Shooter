package genericzombieshooter.actors;


import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Animation;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;



public class RegularZombie extends Zombie{

	public RegularZombie(Point2D.Double p_, int health_, int damage_, double speed_, int cash_, int exp_,
			Animation animation_) {
		super(p_, Globals.ZOMBIE_REGULAR_TYPE, health_, damage_, speed_, cash_, exp_, animation_);
		
	}

	@Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        
    }

	@Override
	protected void soundMoan(double gain) {
        Sounds.MOAN1.play(gain);
		
	}
}

