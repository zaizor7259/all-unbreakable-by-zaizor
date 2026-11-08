package net.zaizor.unbreakable.mixin;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.zaizor.unbreakable.UnbreakableConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
abstract class AnvilBlockMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private static void allUnbreakable$preventAnvilDamage(
            BlockState state,
            CallbackInfoReturnable<BlockState> cir
    ) {
        if (UnbreakableConfig.areAnvilsProtected()) {
            cir.setReturnValue(state);
        }
    }
}
