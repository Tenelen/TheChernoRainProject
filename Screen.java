package com.thecherno.rain.graphics;

import java.util.Random;

import com.thecherno.rain.level.tile.Tile;

//this controls what is entered onto the screen
public class Screen {

  // because they are both private int, we can create 2 variables with a coma
	public int width, height;
	// the array that contains our pixel data
	public int[] pixels;
	public final int MAP_SIZE = 64;
	public final int MAP_SIZE_MASK = MAP_SIZE - 1;
	// sets up a 64x64 tile map
	public int[] tiles = new int[MAP_SIZE * MAP_SIZE];

	public int xOffset, yOffset; 
	
	// random number generator for our tile colors
	private Random random = new Random();

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		// creates an array of pixels the size of our screen
		// 1 integer for each pixel in our game
		pixels = new int[width * height]; // 50,400 pixels

		// generates the tile map with each tile being a random hexcode color
		// TILE MAP
		for (int i = 0; i < MAP_SIZE * MAP_SIZE; i++) {
			tiles[i] = random.nextInt(0xffffff);
		}
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}


	public void renderTile(int xp, int yp, Tile tile) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < tile.sprite.SIZE; y++) {
			// ya = y absolute; yp = y position
			int ya = y + yp;
			for (int x = 0; x < tile.sprite.SIZE; x++) {
				// xa = x absolute (in relation to the world); xp = x position
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= width) break;
				pixels[xa + ya * width] = tile.sprite.pixels[x + y * tile.sprite.SIZE]; 
			}
		}
		
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset; 
	}

}
