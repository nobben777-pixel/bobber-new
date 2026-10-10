package com.example.separatebobbers.mixin;

import com.example.separatebobbers.BobberTracker;
import com.example.separatebobbers.RodIds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin перехватывает момент использования удочки и регистрирует поплавок
 */
@Mixin(FishingRodItem.class)
public abstract class FishingRodItemMixin {

    /**
     * Перехватываем использование удочки (когда нажимают ПКМ)
     */
    @Inject(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
        ),
        cancellable = false
    )
    private void onFishingRodUse(World world, PlayerEntity player, Hand hand, 
                                  CallbackInfoReturnable<Object> cir) {
        
        // Это работает только на сервере
        if (world.isClient) {
            return;
        }

        ItemStack rodStack = player.getStackInHand(hand);
        
        // Получаем или создаём ID для удочки
        int rodId = RodIds.getOrCreateRodId(rodStack, player);
        
        if (rodId == -1) {
            return;
        }

        // Мы не можем напрямую получить FishingBobberEntity отсюда,
        // но дальше он будет создан и зарегистрирован в нашем tracker
    }
}
