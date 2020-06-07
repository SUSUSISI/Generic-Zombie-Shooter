package genericzombieshooter.structures.weapons;

import java.awt.geom.Rectangle2D.Double;
import java.util.Iterator;
import java.util.List;

import genericzombieshooter.structures.Explosion;
import genericzombieshooter.structures.Particle;

public class GunCheckDamageStrategy implements CheckDamageStrategy {

	@Override
	public int gunCheckForDamage(Double rect, List<Particle> particles, int gunDamage) {
		synchronized(particles) {
			int damage = 0;
            if(!particles.isEmpty()) {
                // Check all particles for collisions with the target rectangle.
                Iterator<Particle> particleIterator = particles.iterator();
                while(particleIterator.hasNext()) {
                    Particle particle = particleIterator.next();
                    // If the particle is still alive and has collided with the target.
                    if(particle.isAlive() && particle.checkCollision(rect)) {
                        // Add the damage of the particle and remove it from the list.
                        damage += gunDamage;
                        particleIterator.remove();
                    }
                }
            }
            return damage;
        }
	}

	@Override
	public int explosionCheckForDamage(Double rect, List<Explosion> explosions, int explosionDamage) {
		// TODO Auto-generated method stub
		return 0;
	}

}
