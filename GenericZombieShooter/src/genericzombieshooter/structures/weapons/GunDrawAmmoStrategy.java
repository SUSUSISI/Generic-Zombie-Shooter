package genericzombieshooter.structures.weapons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;

import genericzombieshooter.structures.Particle;

public class GunDrawAmmoStrategy implements DrawAmmoStrategy {

	@Override
	public void drawAmmo(Graphics2D g2d, List<Particle> particles, Color color) {
		synchronized(particles) {
            // Draw all particles whose life has not yet expired.
            if(!particles.isEmpty()) {
                g2d.setColor(color);
                Iterator<Particle> particleIterator = particles.iterator();
                while(particleIterator.hasNext()) {
                    Particle particle = particleIterator.next();
                    if(particle.isAlive()) particle.draw(g2d);
                }
            }
        }		
	}

}
