package net.zaizor.unbreakable.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.zaizor.unbreakable.UnbreakableConfig;

public final class UnbreakableConfigScreen extends Screen {
    private final Screen parent;

    public UnbreakableConfigScreen(Screen parent) {
        super(Component.translatable("screen.all_unbreakable_by_zaizor.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = Math.min(300, width - 24);
        int left = (width - buttonWidth) / 2;
        int top = height / 2 - 10;

        Button.Builder toggle = Button.builder(toggleLabel(), button -> {
            UnbreakableConfig.setEnabled(!UnbreakableConfig.isEnabled());
            button.setMessage(toggleLabel());
        }).bounds(left, top, buttonWidth, 20);

        if (UnbreakableConfig.isServerUnsupported()) {
            toggle.tooltip(Tooltip.create(Component.translatable(
                    "screen.all_unbreakable_by_zaizor.server_unsupported.tooltip"
            )));
        }

        Button toggleButton = toggle.build();
        toggleButton.active = !UnbreakableConfig.isServerUnsupported();
        addRenderableWidget(toggleButton);

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(left, top + 30, buttonWidth, 20)
                .build());
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().gui.setScreen(parent);
    }

    private static Component toggleLabel() {
        return Component.translatable(UnbreakableConfig.isEnabled()
                ? "screen.all_unbreakable_by_zaizor.enabled"
                : "screen.all_unbreakable_by_zaizor.disabled");
    }
}
