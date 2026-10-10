package com.example.separatebobbers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeparateBobbers implements ModInitializer {
    public static final String MOD_ID = "separatebobbers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Separate Bobbers mod!");
        
        // Загружаем поплавки при старте сервера
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);
        
        // Сохраняем поплавки периодически (каждые 5 минут = 6000 тиков)
        ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);
        
        // Сохраняем при остановке сервера
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStop);
    }

    private void onServerStart(MinecraftServer server) {
        LOGGER.info("Server started, loading bobbers data...");
        BobberPersistence.loadBobbers(server);
    }

    private void onServerTick(MinecraftServer server) {
        // Сохраняем каждые 6000 тиков (5 минут)
        if (server.getTicks() % 6000 == 0) {
            BobberPersistence.saveBobbers(server);
        }
    }

    private void onServerStop(MinecraftServer server) {
        LOGGER.info("Server stopping, saving bobbers data...");
        BobberPersistence.saveBobbers(server);
    }
}
