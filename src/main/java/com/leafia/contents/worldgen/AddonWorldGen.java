package com.leafia.contents.worldgen;

import com.custom_hbm.util.LCETuple.Pair;
import com.hbm.main.MainRegistry;
import com.leafia.contents.worldgen.NTMStructBuffer.StructData;
import com.leafia.contents.worldgen.NTMStructBuffer.StructLoader;
import com.leafia.contents.worldgen.lib.SellacityRoadChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddonWorldGen implements IWorldGenerator {
	Random rand = new Random();
	// stolen from NTMWorldGenerator
	private void setRandomSeed(World world, int chunkX, int chunkZ) {
		rand.setSeed(world.getSeed() + world.provider.getDimension());
		final long i = rand.nextLong() / 2L * 2L + 1L;
		final long j = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed((long) chunkX * i + (long) chunkZ * j ^ world.getSeed());
	}
	int pmod(int x,int y) {
		return (y+(x%y))%y;
	}
	public static int roadWidth = 7;
	Map<Pair<Integer,Integer>,SellacityRoadChunk> chunks = new HashMap<>();
	public void genByRoadCoords(int rx,int rz,int cx,int cz,World world) {
		//world.setBlockState(world.getHeight(new BlockPos(rx,0,rz)),ModBlocks.fissure_bomb.getDefaultState());

		// get snake noise
		int nx = Math.floorDiv(rx/7,SellacityRoadChunk.size);
		int nz = Math.floorDiv(rz/7,SellacityRoadChunk.size);
		int nox = Math.floorMod(rx/7,SellacityRoadChunk.size);
		int noz = Math.floorMod(rz/7,SellacityRoadChunk.size);
		Pair<Integer,Integer> chunkPos = new Pair<>(nx,nz);
		setRandomSeed(world,cx,cz);
		if (!chunks.containsKey(chunkPos))
			chunks.put(chunkPos,new SellacityRoadChunk(rand));
		setRandomSeed(world,cx,cz);
		SellacityRoadChunk chunk = chunks.get(chunkPos);

		// check if theres road here
		if (chunk.data[chunk.index(nox,noz)] == 1) {
			StructData road = StructLoader.structs.get("roads/road_cross");
			NTMStructBuffer.fromMetadata(road).build(
					world,
					world.getHeight(new BlockPos(rx+roadWidth/2,65,rz+roadWidth/2))
			);
		}
	}
	@Override
	public void generate(Random random,int chunkX,int chunkZ,World world,IChunkGenerator chunkGenerator,IChunkProvider chunkProvider) {
		try {
			int minX = chunkX*16;
			int minZ = chunkZ*16;
			//world.setBlockState(world.getHeight(new BlockPos(minX+5,15,minZ+5)),ModBlocks.fissure_bomb.getDefaultState(), 2 | 16);
			if (minX > -200 && minX < 200 && minZ > -200 && minZ < 200) {
				int lx = (16+pmod(minX,roadWidth)-roadWidth)/roadWidth;
				int lz = (16+pmod(minZ,roadWidth)-roadWidth)/roadWidth;
				int sx = minX-pmod(minX,roadWidth);
				int sz = minZ-pmod(minZ,roadWidth);
				for (int xo = 0; xo <= lx; xo++) {
					for (int zo = 0; zo <= lz; zo++)
						genByRoadCoords(sx+xo*roadWidth,sz+zo*roadWidth,chunkX,chunkZ,world);
				}
			}
		} catch (Throwable t) {
			MainRegistry.logger.error("LCA Worldgen Error", t);
		}
	}
}
