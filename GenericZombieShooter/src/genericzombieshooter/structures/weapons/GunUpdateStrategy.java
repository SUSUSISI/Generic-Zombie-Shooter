package genericzombieshooter.structures.weapons;

import java.util.Iterator;
import java.util.List;

import genericzombieshooter.structures.Particle;

public class GunUpdateStrategy implements UpdateStrategy {
	@Override
	public void updateWeawpon(List<Particle> particles) {
		synchronized(particles) {
            if(!particles.isEmpty()) {
                Iterator<Particle> particleIterator = particles.iterator();
                while(particleIterator.hasNext()) {
                    Particle particle = particleIterator.next();
                    particle.update();
                    if(!particle.isAlive() || particle.outOfBounds()) {
                        particleIterator.remove();
                        continue;
                    }
                }
            }
        }
	}
}
