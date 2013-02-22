package com.thecherno.rain;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.input.Keyboard;
import com.thecherno.rain.level.Level;
import com.thecherno.rain.level.RandomLevel;

public class Game extends Canvas implements Runnable {
  
	private static final long serialVersionUID = 1L;
	
	//creates the size of our window, with a 16:9 scale, and renders at 1/3 actual size
	public static int width = 300; 
	public static int height = 168; 
	public static int scale = 3;
	public static String title = "Rain";
	
	//a thread is something that loads thing for us
	private Thread thread; 
	private JFrame frame; 
	
	//imports our Keyboard and key variable
	private Keyboard key;
	
	
	//this loads a level, and only one should be loaded at a time
	private Level level; 
	
	//tells us that we aren't running, until we tell it that we are
	private boolean running = false; 
	
	//creating our screen from our class
	private Screen screen;
	
	//this is Java BufferImage; it's amazing and we will use it a lot
	//we have this image, but we can't modify it currently
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	//we have to make an array to be able to change it
	//array is a bunch of variables within 1 variable
	//the brackets [] represent an array
	//a Raster is a data structure. It's an array of pixels.
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	//this is our actual game/window
	public Game()	{
		//sets the dimension for our window using the numbers above
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		
		//creates our screen in our game variable, giving the screen width and height according to what we have set it for
		//uses the width and height from Game.java to import it to Screen.java to use for both
		screen = new Screen(width, height);
		
		//this is the window we create
		frame = new JFrame();
		
		//adds the variable key as a keyboard
		key = new Keyboard(); 
		
		level = new RandomLevel(64, 64); 
		
		//listens to see when buttons are pressed
		addKeyListener(key); 
	}
	//starts our game
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start(); 
	}
	//I'm not sure what this does, but it's our stop function
	public synchronized void stop() {
		running = false; 
		try {
		thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//this is what runs the game
	//tells it to start updating and rendering
	//because 'game' implements runnable, it automatically runs 'run'
	public void run() {
		long lastTime = System.nanoTime(); //the time when we load
		long timer = System.currentTimeMillis(); 
		final double ns = 1000000000.0 / 60.0; 
		double delta = 0; //change
		int frames = 0; //measures FPS 
		int updates = 0; //measures the amount of times a game updates per second (should be 60)
		requestFocus(); 
		while (running == true) {
			long now = System.nanoTime();  //time when this loads
			delta += (now-lastTime) / ns; 
			lastTime = now; 
			while (delta >= 1) { 
				update();
				updates++; //actually counts 
				delta--; 
			}
			render();
			frames++; //actually counts
			
			//this will run once per second
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + "ups, " + frames + " fps"); 
				frame.setTitle(title + "  |  " + updates + "ups, " + frames + " fps");
				updates = 0;
				frames = 0; 
			}
		}
	}
	
	//Update runs at approx 30-60FPS
	//we create this in order to run our game, and tell it to update what we see and do
	//we have to keep this at a reasonable number, otherwise faster computers would run
	//our game faster
	
	//temp variables to make the tile map move
	int x = 0, y = 0;
	public void update() {
		//time map moving temp
		key.update(); 
		if (key.up) y--;
		if (key.down) y++; 
		if (key.left) x--;
		if (key.right) x++; 
	}
	
	
	//this creates what we are going to see
	//will be as many FPS as your computer can handle
	//that way, it will look better on better computers
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		//if the buffer strategy doesn't exist, we will create it here
		if (bs == null) {
			//we want triple buffers, so we have the real one, a back up, and then another back up
			createBufferStrategy(3);
			return;
		}
		//clears the screen
		screen.clear(); 
		level.render(x, y, screen); 
		
		//and then renders new images
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		//this creates the graphics buffer
		Graphics g = bs.getDrawGraphics();
		//----all your graphics go here----
		//****set the color of the things
		//g.setColor(Color.BLACK);
		//****graphics are filled with a rectangle starting from (0,0) and then moving the the getWidth and Height spot
		//g.fillRect(0, 0, getWidth(), getHeight());
		//draws our buffered image to the screen
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		//this gets rid of your graphics when you are done, moving onto new graphics
		//removes frames as they should be removed, to make room for new ones
		g.dispose();
		//VERY important. You have to show the buffer. Buffer swaping.
		//buffer is a place for TEMP storage only; it should only be there for a few mili-seconds
		bs.show();
	}
	
	
	
	
	public static void main(String[] args) {
		//creates our Game
		Game game = new Game();
		//prevents our Frame from being resizeable
		game.frame.setResizable(false);
		//the title of our frame
		game.frame.setTitle(Game.title);
		//what content is in the frame
		game.frame.add(game);
		//I'm not sure what pack is
		game.frame.pack();
		//Exits (and closes all operations) when you hit the X)
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//sets the location of the window to the middle of our screen
		game.frame.setLocationRelativeTo(null);
		//makes it so that we can actually see the window
		game.frame.setVisible(true);
		
		game.start();
	}
	
}
