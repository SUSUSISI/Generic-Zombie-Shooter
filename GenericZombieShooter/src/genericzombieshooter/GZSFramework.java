/**
    This file is part of Generic Zombie Shooter.

    Generic Zombie Shooter is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Generic Zombie Shooter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Generic Zombie Shooter.  If not, see <http://www.gnu.org/licenses/>.
 **/
package genericzombieshooter;

import genericzombieshooter.actors.Player;
import genericzombieshooter.listeners.CanvasListener;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.misc.progress.*;
import genericzombieshooter.structures.GameTime;
import genericzombieshooter.structures.ItemFactory;
import genericzombieshooter.structures.Message;
import genericzombieshooter.structures.ZombieWave;
import genericzombieshooter.structures.components.ErrorWindow;
import genericzombieshooter.structures.components.LevelScreen;
import genericzombieshooter.structures.components.StoreWindow;
import genericzombieshooter.structures.components.WeaponsLoadout;
import genericzombieshooter.structures.items.NightVision;
import genericzombieshooter.structures.weapons.WeaponStrategy;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import kuusisto.tinysound.TinySound;

/**
 * Contains many of the methods used to update game objects and handles the game
 * thread.
 *
 * @author packetpirate
 */
public class GZSFramework {
    // Member variables.
    public JFrame frame;
    public GZSCanvas canvas;
    private StoreWindow store;
    private LevelScreen levelScreen;
    private CanvasListenerManager canvasListenerManager;
    
    // Game objects.
    private Player player; // The player character.
    public Player getPlayer() { return player; }
    private int currentWave;
    private ZombieWave wave;
    public ZombieWave getWave() { return this.wave; }
    
    private WeaponsLoadout loadout;
    public WeaponsLoadout getLoadout() { return this.loadout; }
    
    private ItemFactory itemFactory;
    public ItemFactory getItemFactory() { return this.itemFactory; }

    
    private void resetObjects() {
    	currentWave = 1;
    	player.reset();
    	player.resetStatistics();
    	wave = new ZombieWave(currentWave);
    	levelScreen.resetLevels();
    	loadout.setCurrentWeapon();
    }
    
    private void resetGame() {
    	Globals.resetState();
    	Globals.resetInput();
    	Globals.resetWeapons();
    	resetObjects();
    }
    
   
    public GZSFramework(JFrame frame_) {
        frame = frame_;
        store = new StoreWindow();
        levelScreen = new LevelScreen();
        canvas = new GZSCanvas(this, store, levelScreen);
        
        currentWave = 1;
        player = new Player(((Globals.W_WIDTH / 2) - 24), ((Globals.W_HEIGHT / 2) - 24), 48, 48);
        wave = new ZombieWave(currentWave);
        loadout = new WeaponsLoadout(player);
        itemFactory = new ItemFactory();
        
        
        
        resetGame();
        initializeListeners();
        Sounds.init();
        initializeThread();
    }

    /**
     * Updates the game objects in the animation loop.
     **/
       
    public void update() {
        // Update the game time.
    	if(Globals.progress.isPaused()) 
    		Globals.gameTime.increaseOffset();
    	else if(Globals.progress.compareWith(Screen.field)) {
        	Globals.gameTime.update();
            try {
                player.update();
                itemFactory.update(player);

                waveUpdate();
                checkPlayerLife();
                weaponUpdate();
                checkMessage();

            } catch(Exception e) {
                createErrorWindow(e);
            }
        }        
    }
    
    private void createWave() {
        try {
    		System.out.println(Globals.progress.getStatus());
    		System.out.println(Globals.progress.getScreen());
    		System.out.println("--------------------------------");
            this.wave = new ZombieWave(this.currentWave);
            this.currentWave++;
            Globals.progress.setStatus(Status.waveInProgress);
            
        } catch(Exception e) {
            createErrorWindow(e);
        }
    }
    
    public static BufferedImage loadImage(String filename) {
        try {
            // Create a new BufferedImage from the file that supports transparency.
            BufferedImage bi = ImageIO.read(GZSFramework.class.getResource(filename));
            BufferedImage buffer = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D)buffer.createGraphics();
            g2d.drawImage(bi, 0, 0, null);
            g2d.dispose();
            return buffer;
        } catch(IOException io) {
            System.out.println(io.getMessage());
            System.out.println("Error reading file: " + filename);
            return null;
        }
    }
    
    public void createErrorWindow(Exception e) {
        ErrorWindow error = new ErrorWindow(e);
        Globals.progress.setStatus(Status.crashed);
        frame.getContentPane().removeAll();
        frame.add(error);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void initializeThread() {
        Globals.animation = new Runnable() {
            @Override
            public void run() {
                Globals.progress.setStatus(Status.running);
                while (Globals.progress.compareWith(Status.running)) {
                    try {
                        update();
                        canvas.repaint();
                        Thread.sleep(Globals.SLEEP_TIME);
                    } catch (InterruptedException ie) {
                        System.out.println("Error occurred in main thread...");
                        createErrorWindow(ie);
                    } catch(Exception e) {
                        createErrorWindow(e);
                    }
                }
                TinySound.shutdown();
                System.exit(0);
            }
        };
        
        Globals.mainThread = new Thread(Globals.animation);
    }
    
    public void startThread() {
        Globals.mainThread.start();
    }
    
    
    private void waveUpdate() {
    	if(Globals.progress.compareWith(Status.intermission)) {
            // If the player is in between waves, check if the countdown has reached zero.
            if(Globals.gameTime.getElapsedMillis() >= Globals.nextWave) createWave();
        } else if (Globals.progress.compareWith(Status.waveInProgress)){
        	this.wave.update(player, itemFactory);
        	this.wave.checkPlayerDamage(player);
        	if(this.wave.waveFinished()) {
        		Globals.progress.setStatus(Status.intermission);
                Globals.nextWave = Globals.gameTime.getElapsedMillis() + (10 * 1000);                    		
        	}
        } else if(Globals.progress.compareWith(Status.running)) {
    		Globals.progress.setStatus(Status.intermission);
    	}
    	
    }
    
    private void checkPlayerLife() {
    	// Check to see if the player is still alive. If not, take away a life and reset.
        if(!player.isAlive()) {
            player.die();
            if(player.getLives() == 0) {
                // Show death screen and reset player.
                Globals.progress.setScreen(Screen.death);
                synchronized(Globals.GAME_MESSAGES) { Globals.GAME_MESSAGES.clear(); }
            }
            Sounds.FLAMETHROWER.getAudio().stop();
        }
    }
    
    private void weaponUpdate() {
        { // Begin weapon updates.
            Iterator<WeaponStrategy> it = this.player.getWeaponsMap().values().iterator();
            while(it.hasNext()) {
            	WeaponStrategy w = it.next();
                w.updateWeapon(this.wave.getZombies());
            }
        } // End weapon updates.    	
    }
    
    private void checkMessage() {
        { // Delete expired messages.
            synchronized(Globals.GAME_MESSAGES) {
                Iterator<Message> it = Globals.GAME_MESSAGES.iterator();
                while(it.hasNext()) {
                    Message m = it.next();
                    if(!m.isAlive()) {
                        it.remove();
                        continue;
                    }
                }
            }
        } // End deleting expired messages.
    }
    
    
    private void initializeListeners() {
    	canvasListenerManager = new CanvasListenerManager(canvas,Globals.progress);
    	
    	canvasListenerManager.setBaseListener(new CanvasListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Globals.mousePos.x = e.getX() + (Images.CROSSHAIR.getWidth() / 2);
                Globals.mousePos.y = e.getY() + (Images.CROSSHAIR.getHeight() / 2);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				Globals.mousePos.x = e.getX() + (Images.CROSSHAIR.getWidth() / 2);
				Globals.mousePos.y = e.getY() + (Images.CROSSHAIR.getHeight() / 2);
            }
    	});
    	canvasListenerManager.setListener(Screen.start, new CanvasListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("clicked");
				if((e.getButton() == MouseEvent.BUTTON1)) {
                        Globals.progress.setScreen(Screen.field);
                        Globals.gameTime.reset();
                }

			}
    	});
    	canvasListenerManager.setListener(Screen.field, new CanvasListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
                switch(key) {
            		case KeyEvent.VK_W : Globals.keys[0] = true; break;
            		case KeyEvent.VK_A : Globals.keys[1] = true; break;
            		case KeyEvent.VK_S : Globals.keys[2] = true; break;
            		case KeyEvent.VK_D : Globals.keys[3] = true; break;
                }
			}
			@Override
			public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                
                switch(key) {
                	case KeyEvent.VK_W : Globals.keys[0] = false; break;
                	case KeyEvent.VK_A : Globals.keys[1] = false; break;
                	case KeyEvent.VK_S : Globals.keys[2] = false; break;
                	case KeyEvent.VK_D : Globals.keys[3] = false; break;
                	case KeyEvent.VK_P :
                		if(Globals.progress.isPaused())
                			Globals.progress.resume(); 
    	                else
    	                	Globals.progress.pause();
                		break;
                	case KeyEvent.VK_B : Globals.progress.setScreen(Screen.store); break;
                	case KeyEvent.VK_T : Globals.progress.setScreen(Screen.level); break;
                }
                
                if (key == Globals.HANDGUN.getKey()) {
                    int r = player.setWeapon(Globals.HANDGUN.getName());
                    if(r == 1) loadout.setCurrentWeapon();
                }
                if (key == Globals.ASSAULT_RIFLE.getKey()) {
                    int r = player.setWeapon(Globals.ASSAULT_RIFLE.getName());
                    if(r == 1) loadout.setCurrentWeapon();

                }
                if (key == Globals.SHOTGUN.getKey()) {
                    int r = player.setWeapon(Globals.SHOTGUN.getName());
                    if(r == 1) loadout.setCurrentWeapon();
                }
                if (key == Globals.FLAMETHROWER.getKey()) {
                    int r = player.setWeapon(Globals.FLAMETHROWER.getName());
                    if(r == 1) loadout.setCurrentWeapon();
                }
                if (key == Globals.GRENADE.getKey()) {
                    int r = player.setWeapon(Globals.GRENADE.getName());
                    if(r == 1) loadout.setCurrentWeapon();
                }
                if (key == Globals.LANDMINE.getKey()) {
                    int r = player.setWeapon(Globals.LANDMINE.getName());
                    if(r == 1) loadout.setCurrentWeapon();
                }
                if (key == Globals.FLARE.getKey()) {
                    int r = player.setWeapon(Globals.FLARE.getName());
                    if(r == 1) loadout.setCurrentWeapon();
                }
                if (key == Globals.LASERWIRE.getKey()) {
                    int r = player.setWeapon(Globals.LASERWIRE.getName());
                    if(r == 1) loadout.setCurrentWeapon();
                }
                if (key == Globals.TURRETWEAPON.getKey()) {
                    int r = player.setWeapon(Globals.TURRETWEAPON.getName());
                    if(r == 1) loadout.setCurrentWeapon();
                }
                if (key == Globals.TELEPORTER.getKey()) {
                    int r = player.setWeapon(Globals.TELEPORTER.getName());
                    if(r == 1) loadout.setCurrentWeapon();
                }
                
                	
			}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) Globals.buttons[0] = true;
                if (e.getButton() == MouseEvent.BUTTON3) Globals.buttons[1] = true;
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
                    Globals.buttons[0] = false;
                    if(player.getCurrentWeaponName().equals(Globals.FLAMETHROWER.getName())) {
                        Sounds.FLAMETHROWER.getAudio().stop();
                    }
                    
                    // Reset non-automatic weapons.
                    Iterator<WeaponStrategy> it = player.getWeaponsMap().values().iterator();
                    while(it.hasNext()) {
                        WeaponStrategy w = it.next();
                        if(!w.isAutomatic() && w.hasFired()) w.resetFire(); 
                    }
                }
				else if (e.getButton() == MouseEvent.BUTTON3) Globals.buttons[1] = false;
			}
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(player.getWeaponsMap().size() > 1) {
                    int notches = e.getWheelRotation();
                    String [] weaponNames = {Globals.HANDGUN.getName(), Globals.ASSAULT_RIFLE.getName(),
                                             Globals.SHOTGUN.getName(), Globals.FLAMETHROWER.getName(),
                                             Globals.GRENADE.getName(), Globals.LANDMINE.getName(),
                                             Globals.FLARE.getName(), Globals.LASERWIRE.getName(),
                                             Globals.TURRETWEAPON.getName(), Globals.TELEPORTER.getName()};
                    if(notches < 0) { // Wheel scrolled up.
                        // Move weapon selection to the right.
                        String name = Globals.HANDGUN.getName();
                        for(int i = 0; i < weaponNames.length; i++) {
                            if(weaponNames[i].equals(player.getWeapon().getName())) {
                                if((i + 1) > weaponNames.length) name = Globals.HANDGUN.getName();
                                else {
                                    // Find the next weapon the player has and set it.
                                    for(int j = (i + 1); j < weaponNames.length; j++) {
                                        if(player.hasWeapon(weaponNames[j])) {
                                            name = weaponNames[j];
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        player.setWeapon(name);
                        loadout.setCurrentWeapon();
                    } else { // Wheel scrolled down.
                        // Move weapon selection to the left.
                        String name = Globals.HANDGUN.getName();
                        for(int i = 0; i < weaponNames.length; i++) {
                            if(weaponNames[i].equals(player.getWeapon().getName())) {
                                if((i - 1) < 0) {
                                    // Get the last weapon the player has.
                                    for(int j = 9; j >= 0; j--) {
                                        if(player.hasWeapon(weaponNames[j])) {
                                            name = weaponNames[j];
                                            break;
                                        }
                                    }
                                    break;
                                } else {
                                    for(int j = (i - 1); j >= 0; j--) {
                                        if(player.hasWeapon(weaponNames[j])) {
                                            name = weaponNames[j];
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        player.setWeapon(name);
                        loadout.setCurrentWeapon();
                    }
                }
			}
    	});
    	canvasListenerManager.setListener(Screen.store, new CanvasListener() {
    		@Override
    		public void keyReleased(KeyEvent e) {
    			int key = e.getKeyCode();
    			switch(key) {
            		case KeyEvent.VK_B : Globals.progress.setScreen(Screen.field); break;
    			}
    		}
    		
			@Override
			public void mouseClicked(MouseEvent e) {
				if((e.getButton() == MouseEvent.BUTTON1))
                    store.click(e, player);
			}
    	});
    	canvasListenerManager.setListener(Screen.level, new CanvasListener() {
    		@Override
    		public void keyReleased(KeyEvent e) {
    			int key = e.getKeyCode();
    			switch(key) {
            		case KeyEvent.VK_T : Globals.progress.setScreen(Screen.field); break;
    			}
    		}
			@Override
			public void mouseClicked(MouseEvent e) {
				if((e.getButton() == MouseEvent.BUTTON1))
                    levelScreen.click(e, player);	
			}
    	});
    	canvasListenerManager.setListener(Screen.death, new CanvasListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if((e.getButton() == MouseEvent.BUTTON1))
                    resetGame();
			}
    	});

    	
    }
    
}
