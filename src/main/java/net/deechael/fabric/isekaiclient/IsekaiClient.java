package net.deechael.fabric.isekaiclient;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsekaiClient implements ClientModInitializer {

	public static Minecraft MC = Minecraft.getInstance();

	public static final Logger LOGGER = LoggerFactory.getLogger("isekaiclient");

	private static IsekaiConfig config;

	@Override
	public void onInitializeClient() {
		AutoConfig.register(IsekaiConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(IsekaiConfig.class).getConfig();
	}

	public static IsekaiConfig getConfig() {
		return config;
	}

}
