package genericzombieshooter.actors;


import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Animation;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class DogZombie extends Zombie{

	public DogZombie(Point2D.Double p_, int type_, int health_, int damage_, double speed_, int cash_, int exp_,
			Animation animation_) {
		super(p_, Globals.ZOMBIE_DOG_TYPE, health_, damage_, speed_, cash_, exp_, animation_);
		
	}
	@Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        
    }
    
	@Override
    public void moan(Player player) {
        if(!this.moaned) {
            if(Globals.gameTime.getElapsedMillis() >= this.nextMoan) {
                double xD = player.getCenterX() - this.x;
                double yD = player.getCenterY() - this.y;
                double dist = Math.sqrt((xD * xD) + (yD * yD));
                double gain = 1.0 - (dist / Player.AUDIO_RANGE);
                Sounds.MOAN2.play(gain);
                this.moaned = true;
            }
        }
    }
}
