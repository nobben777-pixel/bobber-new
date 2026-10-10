package com.example.separatebobbers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Система управления уникальными ID удочек.
 * Каждой удочке присваивается свой ID, чтобы отслеживать её поплавки.
 */
public class RodIds {
    private static final String ROD_ID_TAG = "SeparateBobber_RodId";
    private static final String ROD_COUNTER_TAG = "SeparateBobber_Counter";
    
    // Кэш для игрока: счётчик ID для его удочек
    private static final Map<String, Integer> playerCounters = new ConcurrentHashMap<>();
    private static int globalCounter = 0;

    /**
     * Получаем или создаём ID для удочки
     * Если у удочки уже есть ID, возвращаем существующий
     * Если нет - генерируем новый
     */
    public static int getOrCreateRodId(ItemStack rodStack, PlayerEntity player) {
        if (rodStack == null || !rodStack.canHaveNbt()) {
            return -1;
        }

        NbtCompound nbt = rodStack.getOrCreateNbt();
        
        // Если ID уже есть - возвращаем его
        if (nbt.contains(ROD_ID_TAG, NbtElement.INT_TYPE)) {
            return nbt.getInt(ROD_ID_TAG);
        }

        // Генерируем новый ID
        int newId = generateNewRodId(player);
        nbt.putInt(ROD_ID_TAG, newId);
        
        SeparateBobbers.LOGGER.debug("Assigned rod ID {} to rod in player {}'s inventory", newId, player.getName().getString());
        
        return newId;
    }

    /**
     * Генерируем уникальный ID для новой удочки
     */
    private static int generateNewRodId(PlayerEntity player) {
        String playerUUID = player.getUuidAsString();
        
        // Увеличиваем счётчик для конкретного игрока
        int playerSpecificId = playerCounters.getOrDefault(playerUUID, 0) + 1;
        playerCounters.put(playerUUID, playerSpecificId);
        
        // Комбинируем глобальный счётчик и счётчик для игрока
        globalCounter++;
        return globalCounter * 100000 + playerSpecificId;
    }

    /**
     * Получаем ID удочки если он существует
     */
    public static int getRodId(ItemStack rodStack) {
        if (rodStack == null || !rodStack.canHaveNbt()) {
            return -1;
        }

        NbtCompound nbt = rodStack.getNbt();
        if (nbt != null && nbt.contains(ROD_ID_TAG, NbtElement.INT_TYPE)) {
            return nbt.getInt(ROD_ID_TAG);
        }
        
        return -1;
    }

    /**
     * Проверяем, есть ли у удочки ID
     */
    public static boolean hasRodId(ItemStack rodStack) {
        if (rodStack == null || !rodStack.canHaveNbt()) {
            return false;
        }

        NbtCompound nbt = rodStack.getNbt();
        return nbt != null && nbt.contains(ROD_ID_TAG, NbtElement.INT_TYPE);
    }

    /**
     * Копируем ID при копировании удочки
     */
    public static void copyRodId(ItemStack source, ItemStack target) {
        if (source == null || target == null) {
            return;
        }

        int rodId = getRodId(source);
        if (rodId != -1) {
            NbtCompound targetNbt = target.getOrCreateNbt();
            targetNbt.putInt(ROD_ID_TAG, rodId);
        }
    }

    /**
     * Очищаем ID (если удочка сломалась/использовалась)
     */
    public static void clearRodId(ItemStack rodStack) {
        if (rodStack == null || !rodStack.canHaveNbt()) {
            return;
        }

        NbtCompound nbt = rodStack.getNbt();
        if (nbt != null) {
            nbt.remove(ROD_ID_TAG);
        }
    }
}
