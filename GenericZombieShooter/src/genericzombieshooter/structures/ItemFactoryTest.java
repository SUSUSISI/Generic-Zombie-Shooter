package genericzombieshooter.structures;

import genericzombieshooter.structures.ItemFactory;
import genericzombieshooter.structures.components.LevelScreen;
import genericzombieshooter.structures.components.StoreWindow;
import genericzombieshooter.structures.components.WeaponsLoadout;
import genericzombieshooter.structures.items.Ammo;
import genericzombieshooter.structures.items.HealthPack;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before; 
import org.junit.Test;

import genericzombieshooter.CanvasListenerManager;
import genericzombieshooter.GZSCanvas;
import genericzombieshooter.GZSFramework;
import genericzombieshooter.actors.Player;
import genericzombieshooter.misc.Globals;

public class ItemFactoryTest {
	 	ItemFactory itemFactory = new ItemFactory();
	    Player player = new Player(((Globals.W_WIDTH / 2) - 24), ((Globals.W_HEIGHT / 2) - 24), 48, 48);
	    
	    
	    /*
	     * Purpose : Make HealthPack Item and insert into itemsDropped List
	     * Input : HealthPack item spwan time
	     * Expected:
	     * 		return HealthPackitem
	     * 		
	     * 		If the item in itemsDropped is HealthPack item, 
	     * 		the id of recent Dropped item is 1, so return true
	    
	    */
	    @Test
	    public void TestcreateItemToHealthPack() {
	    	
	    	itemFactory.reset();
	    	Globals.gameTime.setElapsedMillis(Globals.gameTime.getElapsedMillis()+ HealthPack.SPAWN_TIME+1);
	    	itemFactory.update(player);
	    	
	    	assertEquals(itemFactory.recentDroppeditem().getId(), HealthPack.ID);
	    	
	    }
	    
	    /*
	     * Purpose : MakeAmmoCrate Item and insert into itemsDropped List
	     * Input : Ammo item spwan time
	     * Expected:
	     * 		Retrun True
	     * 		
	     * 		If the item in itemsDropped is AmmoCrate item, 
	     * 		the id of recent Dropped item is 2, so return true
	    
	    */
	    @Test
	    public void TestcreateItemToAmmoCrate() {
	    	player.reset();
	    	itemFactory.reset();
	    	
	    	Globals.gameTime.setElapsedMillis(Globals.gameTime.getElapsedMillis() + Ammo.SPAWN_TIME+1);
	    	itemFactory.update(player);
	    	
	    	assertEquals(itemFactory.recentDroppeditem().getId(), Ammo.ID);
	    	
	    	//if itemFactory is not empty
	    	itemFactory.update(player);
	    }
	    
	    
	    
	    
}
