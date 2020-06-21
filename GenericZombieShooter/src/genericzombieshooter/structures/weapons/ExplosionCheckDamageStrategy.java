package genericzombieshooter.structures.weapons;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Iterator;
import java.util.List;

import genericzombieshooter.structures.Explosion;
import genericzombieshooter.structures.Particle;

public class ExplosionCheckDamageStrategy implements CheckDamageStrategy{

	@Override
	public int gunCheckForDamage(Double rect, List<Particle> particles, int gunDamage) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int explosionCheckForDamage(Double rect, List<Explosion> explosions, int explosionDamage) {
		synchronized(explosions) {
            /* The grenade particle itself does nothing. Upon contact with a zombie,
               it stops moving, and once its timer goes off, it explodes. */
            int damage = 0;
            if(!explosions.isEmpty()) {
                Iterator<Explosion> explosionIterator = explosions.iterator();
                while(explosionIterator.hasNext()) {
                    Explosion explosion = explosionIterator.next();
                    if(explosion.getImage().isActive()) {
                        Rectangle2D.Double explosionRectangle = new Rectangle2D.Double((explosion.x - (explosion.getSize().width / 2)), (explosion.y - (explosion.getSize().height / 2)),
                                                                             explosion.getSize().width, explosion.getSize().height);
                        if(rect.intersects(explosionRectangle)) damage += explosionDamage;
                    }
                }
            }
            return damage;
        }
	}

}
