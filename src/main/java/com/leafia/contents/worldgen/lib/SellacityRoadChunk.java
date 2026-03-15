package com.leafia.contents.worldgen.lib;

import java.util.Random;

public class SellacityRoadChunk {
	public static final int size = 100;
	public final int[] data = new int[size*size];
	public int x(int index) { return index%size; }
	public int y(int index) { return index/size; }
	public int index(int x,int y) { return x+y*size; }
	boolean checkAdjacent(int x,int y,int ignore) {
		if (x > 0 && (index(x-1,y) != ignore) && data[index(x-1,y)] == 1)
			return true;
		if (x < size-1 && (index(x+1,y) != ignore) && data[index(x+1,y)] == 1)
			return true;
		if (y > 0 && (index(x,y-1) != ignore) && data[index(x,y-1)] == 1)
			return true;
		if (y < size-1 && (index(x,y+1) != ignore) && data[index(x,y+1)] == 1)
			return true;
		return false;
	}

	public static final int density = 300;
	long debugTime = System.currentTimeMillis();
	void printTime(String message) {
		long newTime = System.currentTimeMillis();
		System.out.println("Took "+((newTime-debugTime)/1000f)+"s for "+message);
		debugTime = newTime;
	}
	void algorithmV1(Random rand) { // Rating: 0/10
		// Create a full sized cross of roads (extending all the way to the edges)
		for (int x = 0; x < size; x++)
			data[index(x,size/2)] = 1;
		for (int y = 0; y < size; y++)
			data[index(size/2,y)] = 1;
		printTime("cross generation");

		boolean wasHoriz = false;
		// Generate snake noises
		for (int attempt = 0; attempt < density; attempt++) {
			int x = rand.nextInt(size);
			int y = rand.nextInt(size);

			// if current position is already occupied, continue
			if (data[index(x,y)] == 1) continue;

			// if current position has any adjacent roads, continue
			if (checkAdjacent(x,y,-1)) continue;

			// calculate direction
			int xo = (x >= size/2) ? -1 : 1;
			int yo = (y >= size/2) ? -1 : 1;
			// choose to move vertically or horizontally
			if (wasHoriz) xo = 0; else yo = 0;
			wasHoriz = !wasHoriz;

			int lastIndex = -1;
			while (true) {
				// we don't do out of bounds check here because it's theoretically impossible to go out of bounds
				// because we are always heading towards the center cross we generated earlier
				data[index(x,y)] = 1;
				if (checkAdjacent(x,y,lastIndex)) break;
				lastIndex = index(x,y);
				x += xo;
				y += yo;
			}
		}
		printTime("snake generation");
	}
	public SellacityRoadChunk(Random rand) {
		algorithmV1(rand);
	}
}
