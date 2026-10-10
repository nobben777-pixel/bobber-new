package com.example.separatebobbers;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Отслеживает какому игроку и какой удочке принадлежит каждый поплавок.
 * Использует Thread-safe структуру для синхронизации с сервером.
 */
public class BobberTracker {
    // Ключ: ID поплавка, Значение: BobberData (информация о поплавке)
    private static final Map<Integer, BobberData> BOBBER_MAP = new ConcurrentHashMap<>();

    public static class BobberData {
        public final int playerId;          // UUID игрока в виде числа
        public final int rodId;             // Уникальный ID удочки из RodIds
        public final String playerUUID;     // UUID игрока строкой для надёжности
        public double x, y, z;              // Координаты поплавка
        public int dimension;               // ID измерения (0 = Overworld, -1 = Nether и т.д.)

        public BobberData(int playerId, String playerUUID, int rodId, double x, double y, double z, int dimension) {
            this.playerId = playerId;
            this.playerUUID = playerUUID;
            this.rodId = rodId;
            this.x = x;
            this.y = y;
            this.z = z;
            this.dimension = dimension;
        }

        // Сохранение в NBT
        public NbtCompound writeNbt(NbtCompound nbt) {
            nbt.putInt("PlayerId", playerId);
            nbt.putString("PlayerUUID", playerUUID);
            nbt.putInt("RodId", rodId);
            nbt.putDouble("X", x);
            nbt.putDouble("Y", y);
            nbt.putDouble("Z", z);
            nbt.putInt("Dimension", dimension);
            return nbt;
        }

        // Загрузка из NBT
        public static BobberData readNbt(NbtCompound nbt) {
            return new BobberData(
                nbt.getInt("PlayerId"),
                nbt.getString("PlayerUUID"),
                nbt.getInt("RodId"),
                nbt.getDouble("X"),
                nbt.getDouble("Y"),
                nbt.getDouble("Z"),
                nbt.getInt("Dimension")
            );
        }
    }

    /**
     * Регистрируем новый поплавок с информацией о удочке
     */
    public static void registerBobber(FishingBobberEntity bobber, int rodId, String playerUUID) {
        int bobberId = bobber.getId();
        World world = bobber.getWorld();
        int dimension = world.getRegistryKey().getValue().getNamespace().equals("minecraft") ? 
            (world.getRegistryKey().getValue().getPath().equals("the_nether") ? -1 : 0) : 0;

        BobberData data = new BobberData(
            bobber.getOwner() != null ? bobber.getOwner().getId() : -1,
            playerUUID,
            rodId,
            bobber.getX(),
            bobber.getY(),
            bobber.getZ(),
            dimension
        );

        BOBBER_MAP.put(bobberId, data);
        SeparateBobbers.LOGGER.debug("Registered bobber {} for player {} with rod {}", bobberId, playerUUID, rodId);
    }

    /**
     * Получаем ID удочки по ID поплавка
     */
    public static int getRodIdForBobber(int bobberId) {
        BobberData data = BOBBER_MAP.get(bobberId);
        return data != null ? data.rodId : -1;
    }

    /**
     * Получаем все поплавки конкретного игрока
     */
    public static Collection<Integer> getBobbersForPlayer(String playerUUID) {
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, BobberData> entry : BOBBER_MAP.entrySet()) {
            if (entry.getValue().playerUUID.equals(playerUUID)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    /**
     * Удаляем информацию о поплавке
     */
    public static void removeBobber(int bobberId) {
        BOBBER_MAP.remove(bobberId);
    }

    /**
     * Очищаем все данные (при перезагрузке)
     */
    public static void clear() {
        BOBBER_MAP.clear();
    }

    /**
     * Получаем все данные для сохранения
     */
    public static Map<Integer, BobberData> getAllBobbers() {
        return new HashMap<>(BOBBER_MAP);
    }

    /**
     * Восстанавливаем данные из карты
     */
    public static void restoreBobbers(Map<Integer, BobberData> bobbers) {
        BOBBER_MAP.putAll(bobbers);
    }

    /**
     * Проверяем, есть ли поплавок в памяти
     */
    public static boolean hasBobber(int bobberId) {
        return BOBBER_MAP.containsKey(bobberId);
    }

    /**
     * Получаем все данные о поплавке
     */
    public static BobberData getBobberData(int bobberId) {
        return BOBBER_MAP.get(bobberId);
    }

    /**
     * Обновляем координаты поплавка
     */
    public static void updateBobberPosition(int bobberId, double x, double y, double z) {
        BobberData data = BOBBER_MAP.get(bobberId);
        if (data != null) {
            data.x = x;
            data.y = y;
            data.z = z;
        }
    }
}
