package com.leafia.contents.machines.elevators;

import com.hbm.api.energymk2.IEnergyReceiverMK2;
import com.leafia.contents.machines.elevators.car.ElevatorEntity;
import com.leafia.contents.machines.elevators.weight.EvWeightEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class EvPulleyTE extends TileEntity implements IEnergyReceiverMK2, ITickable {
	public ElevatorEntity elevator;
	public EvWeightEntity counterweight;

	@Override
	public void update() {
		if (!world.isRemote) {
			if (elevator != null) {
				if (!elevator.isEntityAlive())
					elevator = null;
			}
		}
	}

	@Override
	public void setPower(long power) {

	}

	@Override
	public long getPower() {
		return 0;
	}

	@Override
	public long getMaxPower() {
		return 0;
	}

	boolean loaded = true;

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		loaded = false;
	}
}
