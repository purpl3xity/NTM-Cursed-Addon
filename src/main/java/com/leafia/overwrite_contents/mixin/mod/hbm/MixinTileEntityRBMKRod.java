package com.leafia.overwrite_contents.mixin.mod.hbm;

import com.hbm.tileentity.machine.rbmk.RBMKDials.RBMKKeys;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKBase;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKRod;
import com.leafia.overwrite_contents.interfaces.IMixinTileEntityRBMKBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityRBMKRod.class)
public abstract class MixinTileEntityRBMKRod extends TileEntityRBMKBase {
	@Redirect(method = "update",at = @At(value = "INVOKE", target = "Lcom/hbm/tileentity/machine/rbmk/TileEntityRBMKRod;maxHeat()D",remap = false),require = 1)
	double onGetMaxHeat(TileEntityRBMKRod instance) {
		if (((IMixinTileEntityRBMKBase)this).leafia$getDamage() > IMixinTileEntityRBMKBase.maxDamage)
			return -100; // fuck you
		if (world.getGameRules().getBoolean(RBMKKeys.KEY_DISABLE_MELTDOWNS.keyString))
			return maxHeat();
		return Double.POSITIVE_INFINITY;
	}
	@Redirect(method = "update",at = @At(value = "INVOKE", target = "Lcom/hbm/tileentity/machine/rbmk/TileEntityRBMKRod;meltdown()V",remap = false),require = 1)
	void onMeltdown(TileEntityRBMKRod instance) {
		// do nothing
	}
}
