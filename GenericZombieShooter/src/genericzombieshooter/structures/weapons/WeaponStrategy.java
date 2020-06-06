package genericzombieshooter.structures.weapons;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.Particle;

public interface WeaponStrategy {
	public abstract int getWeaponPrice();
    public abstract int getAmmoPrice();
    public abstract int getAmmoPackAmount();
    public abstract void updateWeapon(List<Zombie> zombies);
    public abstract void drawAmmo(Graphics2D g2d);
    public abstract void fire(double theta, Point2D.Double pos, Player player);
    public abstract int checkForDamage(Rectangle2D.Double rect);
    public abstract String getName();
    public abstract int getKey();
    public abstract BufferedImage getImage();
    public abstract int getAmmoLeft();
    public abstract int getMaxAmmo();
    public abstract boolean isAutomatic();
    public abstract boolean hasFired();
    public abstract void resetFire();
    public abstract double getCooldownPercentage();
    public abstract void resetCooldown();
    public abstract void cool();
	public abstract boolean canFire();
	public abstract boolean ammoFull();
    public abstract void addAmmo(int amount);
	public abstract void resetAmmo();
	public abstract void consumeAmmo();
	public abstract List<Particle> getParticles();
    public abstract List<LightSource> getLights();
}