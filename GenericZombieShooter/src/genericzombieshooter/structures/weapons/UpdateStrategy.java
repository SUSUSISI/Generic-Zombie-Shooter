package genericzombieshooter.structures.weapons;

import java.util.List;

import genericzombieshooter.structures.Particle;

public interface UpdateStrategy {
	public abstract void updateWeawpon(List<Particle> particles);
}
