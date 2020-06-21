package genericzombieshooter.structures.weapons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import genericzombieshooter.structures.Particle;

public interface DrawAmmoStrategy {
	public abstract void drawAmmo(Graphics2D g2d, List<Particle> particles, Color color);
}
