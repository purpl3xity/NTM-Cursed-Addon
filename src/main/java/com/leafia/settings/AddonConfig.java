package com.leafia.settings;

import com.hbm.config.GeneralConfig;
import com.leafia.dev.LeafiaDebug;

public class AddonConfig {
	public static boolean useLeafiaTorex = true;
	public static boolean enableHealthMod = true;
    public static int dfcComponentRange = 50;
	public static boolean enableFirestorm = LeafiaDebug.isDevEnv;
	public static boolean enableWackySplashes = true;
	public static boolean enableAcidRainRender = true;
	public static class ConfigOverrides {
		public static boolean blockReplacement = true;
		public static void applyGeneralConfig() {
			GeneralConfig.enableBlockReplcement = blockReplacement;
		}
	}
	public static void loadFromConfig(){
		_ConfigBuilder builder = new _ConfigBuilder();
		builder.createEmptyFile();
		builder.loadConfig();
		builder._separator();
		builder._category("ASM");
		{
			enableWackySplashes = builder._boolean("enableWackySplashes",true);
			enableAcidRainRender = builder._boolean("enableAcidRainRender",true);
		}
		builder._separator();
		builder._category("OVERRIDE");
		{
			// I do not care about performance. This addon is aimed for newer playerbase.
			ConfigOverrides.blockReplacement = builder._boolean("ovr_enableBlockReplacement",true);
		}
		builder._separator();
		builder._category("GENERAL");
		{
			builder._comment("Replaces item radiations with LCE radiations");
			enableHealthMod = builder._boolean("enableRadClassification",true);

			builder._comment("How far DFC components can reach");
			dfcComponentRange = builder._integer("dfcComponentRange",50);
		}
		builder._separator();
		builder._category("CLIENT");
		{

		}
		builder._separator();
		builder.saveConfig();
	}
	static {
		loadFromConfig();
	}
}
