package com.thecherno.rain.level;

import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.level.tile.Tile;

//our primary level class
//this will be used as a 'template' for our levels to call variables over to them
//
public class Level {
  
	protected int width, height; 
	protected int[] tiles; 
	
	public Level(int width, int height) {
		this.width = width; 
		this.height = height; 
		tiles = new int[width * height]; 
		generateLevel(); 
	}
	
	//loads level from file
	public Level(String path){ 
		loadLevel(path);
	}
	
	
//this the random generate level tool that will be used later
	protected void generateLevel() {
	}
	
	//the path for our level file
	private void loadLevel(String path) {
	}
	
	//updates the level. Ex: AI positionings
	public void update() {
		//will set for 60FPS
	}
	
	//maybe allow some time for going day/night?
	private void time() {
	}
	
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4; //>>5 = /32
		int x1 = (xScroll + screen.width) >> 4; 
		int y0 = yScroll >> 4; 
		int y1 = (yScroll + screen.height) >> 4;
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
				
			}
		}
		
	}
	
	//this generates tiles based on the random numbers from 0-3 and places them into places
	public Tile getTile(int x, int y) {
		//we will render based on what this returns
		if (tiles [x + y * width] == 0) return Tile.grass;
		return Tile.voidTile; 
	}
	
	

}
