package net.zaizor.unbreakable.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.DataComponentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.zaizor.unbreakable.UnbreakableConfig;

/** Stops vanilla and modded item durability from being consumed. */
@Mixin(ItemStack.class)
abstract class ItemStackMixin {
    @Shadow
    public abstract int getDamageValue();

    @Inject(method = "hurtAndBreak", at = @At("HEAD"), cancellable = true)
    private void allUnbreakable$cancelDurabilityDamage(CallbackInfo ci) {
        if (UnbreakableConfig.isItemProtectionEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method = "setDamageValue", at = @At("HEAD"), cancellable = true)
    private void allUnbreakable$cancelDirectDamage(int requestedDamage, CallbackInfo ci) {
        if (UnbreakableConfig.isItemProtectionEnabled() && requestedDamage > this.getDamageValue()) {
            ci.cancel();
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(
            method = "set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;",
            at = @At("HEAD"),
            cancellable = true
    )
    private <T> void allUnbreakable$cancelDamageComponent(
            DataComponentType<T> type,
            T value,
            CallbackInfoReturnable<T> cir
    ) {
        if (UnbreakableConfig.isItemProtectionEnabled()
                && type == DataComponents.DAMAGE
                && value instanceof Integer requestedDamage
                && requestedDamage > this.getDamageValue()) {
            cir.setReturnValue((T) Integer.valueOf(this.getDamageValue()));
        }
    }

    @Inject(method = "isBarVisible", at = @At("HEAD"), cancellable = true)
    private void allUnbreakable$hideDurabilityBar(CallbackInfoReturnable<Boolean> cir) {
        if (UnbreakableConfig.areDurabilityBarsHidden()) {
            cir.setReturnValue(false);
        }
    }
}
