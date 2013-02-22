package com.thecherno.rain.level;

import java.util.Random;

//extending gets all of the properties from Level
public class RandomLevel extends Level{
  
	private static final Random random = new Random(); 
	
	public RandomLevel(int width, int height) {
		//super refers to the extending; sends the variable back to where it was from (Level.java)
		super(width, height);
	}
	
	protected void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width;  x++) {
				//this will generate an array of numbers from 0-3 and then generate tiles into those locations based on the number
				tiles[x + y * width] = random.nextInt(4);
			}
		}
	}

}
