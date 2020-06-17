package genericzombieshooter.structures.weapons;

import java.awt.geom.Rectangle2D;
import java.util.List;

import genericzombieshooter.structures.Explosion;
import genericzombieshooter.structures.Particle;

public interface CheckDamageStrategy {
	public abstract int gunCheckForDamage(Rectangle2D.Double rect, List<Particle> particles, int gunDamage);
	public abstract int explosionCheckForDamage(Rectangle2D.Double rect, List<Explosion> explosions, int explosionDamage);
}
