package net.zaizor.unbreakable.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.zaizor.unbreakable.UnbreakableConfig;

public final class UnbreakableClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
                UnbreakableConfig.setServerUnsupported(client.getSingleplayerServer() == null));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                UnbreakableConfig.setServerUnsupported(false));
    }
}
