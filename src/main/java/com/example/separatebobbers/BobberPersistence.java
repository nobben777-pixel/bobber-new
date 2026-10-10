package com.example.separatebobbers;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Система сохранения и загрузки данных поплавков в файл.
 * Данные сохраняются в папке мира в формате NBT.
 */
public class BobberPersistence {
    private static final Logger LOGGER = LoggerFactory.getLogger("separatebobbers/persistence");
    private static final String BOBBER_DATA_FILE = "separatebobbers_data.nbt";

    /**
     * Сохраняем все данные о поплавках в файл
     */
    public static void saveBobbers(MinecraftServer server) {
        try {
            File worldDir = server.getWorld(net.minecraft.world.dimension.DimensionTypes.OVERWORLD).getLevelProperties().getDirectory();
            File dataFile = new File(worldDir.getParentFile(), BOBBER_DATA_FILE);

            NbtCompound rootTag = new NbtCompound();
            
            // Получаем все поплавки из трекера
            Map<Integer, BobberTracker.BobberData> allBobbers = BobberTracker.getAllBobbers();
            
            // Сохраняем количество поплавков
            rootTag.putInt("Count", allBobbers.size());
            
            int index = 0;
            for (Map.Entry<Integer, BobberTracker.BobberData> entry : allBobbers.entrySet()) {
                NbtCompound bobberTag = new NbtCompound();
                bobberTag.putInt("BobberId", entry.getKey());
                entry.getValue().writeNbt(bobberTag);
                rootTag.put("Bobber_" + index, bobberTag);
                index++;
            }

            // Записываем в файл
            NbtIo.writeCompressed(rootTag, dataFile);
            LOGGER.info("Saved {} bobbers to {}", allBobbers.size(), dataFile.getAbsolutePath());

        } catch (IOException e) {
            LOGGER.error("Failed to save bobber data", e);
        }
    }

    /**
     * Загружаем все данные о поплавках из файла
     */
    public static void loadBobbers(MinecraftServer server) {
        try {
            File worldDir = server.getWorld(net.minecraft.world.dimension.DimensionTypes.OVERWORLD).getLevelProperties().getDirectory();
            File dataFile = new File(worldDir.getParentFile(), BOBBER_DATA_FILE);

            if (!dataFile.exists()) {
                LOGGER.info("No bobber data file found, starting fresh");
                return;
            }

            NbtCompound rootTag = NbtIo.readCompressed(dataFile);
            int count = rootTag.getInt("Count");
            
            Map<Integer, BobberTracker.BobberData> bobbers = new HashMap<>();
            
            for (int i = 0; i < count; i++) {
                NbtCompound bobberTag = rootTag.getCompound("Bobber_" + i);
                int bobberId = bobberTag.getInt("BobberId");
                BobberTracker.BobberData data = BobberTracker.BobberData.readNbt(bobberTag);
                bobbers.put(bobberId, data);
            }

            // Восстанавливаем поплавки в памяти
            BobberTracker.restoreBobbers(bobbers);
            LOGGER.info("Loaded {} bobbers from {}", bobbers.size(), dataFile.getAbsolutePath());
            
            // Пытаемся восстановить поплавки в мире
            recreateBobbers(server, bobbers);

        } catch (IOException e) {
            LOGGER.error("Failed to load bobber data", e);
        }
    }

    /**
     * Восстанавливаем физические сущности поплавков в мире
     */
    private static void recreateBobbers(MinecraftServer server, Map<Integer, BobberTracker.BobberData> bobbers) {
        int recreated = 0;
        
        for (Map.Entry<Integer, BobberTracker.BobberData> entry : bobbers.entrySet()) {
            BobberTracker.BobberData data = entry.getValue();
            
            // Получаем мир по ID измерения
            ServerWorld world = getWorldByDimension(server, data.dimension);
            if (world == null) {
                LOGGER.warn("World for dimension {} not found, skipping bobber recreation", data.dimension);
                continue;
            }

            // Создаём новый поплавок в нужном месте
            try {
                FishingBobberEntity bobber = new FishingBobberEntity(world, data.x, data.y, data.z);
                world.spawnEntity(bobber);
                
                // Регистрируем его в трекере с правильным ID удочки
                BobberTracker.registerBobber(bobber, data.rodId, data.playerUUID);
                recreated++;
                
            } catch (Exception e) {
                LOGGER.warn("Failed to recreate bobber at {}, {}, {}", data.x, data.y, data.z, e);
            }
        }
        
        LOGGER.info("Recreated {} bobbers in world", recreated);
    }

    /**
     * Получаем мир по ID измерения
     */
    private static ServerWorld getWorldByDimension(MinecraftServer server, int dimensionId) {
        // 0 = Overworld, -1 = Nether, 1 = End
        return switch (dimensionId) {
            case 0 -> server.getWorld(net.minecraft.world.dimension.DimensionTypes.OVERWORLD);
            case -1 -> server.getWorld(net.minecraft.world.dimension.DimensionTypes.THE_NETHER);
            case 1 -> server.getWorld(net.minecraft.world.dimension.DimensionTypes.THE_END);
            default -> server.getWorld(net.minecraft.world.dimension.DimensionTypes.OVERWORLD);
        };
    }
}
