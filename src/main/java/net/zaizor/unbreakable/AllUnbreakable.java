package net.zaizor.unbreakable;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AllUnbreakable implements ModInitializer {
    public static final String MOD_ID = "all_unbreakable_by_zaizor";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        UnbreakableConfig.load();
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            UnbreakableConfig.setServerUnsupported(server.isDedicatedServer());
            if (server.isDedicatedServer()) {
                LOGGER.warn("Dedicated servers are not supported yet; All Unbreakable is disabled.");
            }
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!UnbreakableConfig.isRepairEnabled()) {
                return;
            }

            boolean requested = UnbreakableConfig.consumeRepairRequest();
            if (!requested && server.getTickCount() % 20 != 0) {
                return;
            }

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                repairPlayerItems(player);
            }
        });
        LOGGER.info("All Unbreakable by Zaizor loaded.");
    }

    public static void repairPlayerItems(ServerPlayer player) {
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            repair(stack);
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            repair(player.getItemBySlot(slot));
        }
        for (int slot = 0; slot < player.getEnderChestInventory().getContainerSize(); slot++) {
            repair(player.getEnderChestInventory().getItem(slot));
        }
    }

    private static void repair(ItemStack stack) {
        if (!stack.isEmpty() && stack.isDamaged()) {
            stack.setDamageValue(0);
        }
    }
}
