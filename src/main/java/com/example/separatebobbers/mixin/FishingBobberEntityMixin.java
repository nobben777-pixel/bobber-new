package com.example.separatebobbers.mixin;

import com.example.separatebobbers.BobberTracker;
import com.example.separatebobbers.RodIds;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin перехватывает создание поплавка и регистрирует его с ID удочки
 */
@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

    /**
     * Перехватываем инициализацию поплавка
     */
    @Inject(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;IIZ)V", at = @At("TAIL"))
    private void onBobberCreate(PlayerEntity player, net.minecraft.world.World world, 
                                int piercingLevel, int lureLevel, boolean z, CallbackInfo ci) {
        
        // Это работает только на сервере
        if (world.isClient) {
            return;
        }

        // Ищем удочку в руке игрока
        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getOffHandStack();
        ItemStack rodStack = null;
        
        if (mainHand != null && mainHand.getItem() instanceof net.minecraft.item.FishingRodItem) {
            rodStack = mainHand;
        } else if (offHand != null && offHand.getItem() instanceof net.minecraft.item.FishingRodItem) {
            rodStack = offHand;
        }

        if (rodStack == null) {
            return;
        }

        // Получаем ID удочки
        int rodId = RodIds.getOrCreateRodId(rodStack, player);
        
        if (rodId == -1) {
            return;
        }

        // Регистрируем поплавок с ID удочки
        FishingBobberEntity bobber = (FishingBobberEntity) (Object) this;
        BobberTracker.registerBobber(bobber, rodId, player.getUuidAsString());
    }

    /**
     * Перехватываем удаление поплавка
     */
    @Inject(method = "remove", at = @At("HEAD"))
    private void onBobberRemove(net.minecraft.entity.Entity.RemovalReason reason, CallbackInfo ci) {
        FishingBobberEntity bobber = (FishingBobberEntity) (Object) this;
        BobberTracker.removeBobber(bobber.getId());
    }
}
