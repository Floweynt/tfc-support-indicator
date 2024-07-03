package com.floweytf.tfcsupportindicator;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.dries007.tfc.common.recipes.CollapseRecipe;
import net.dries007.tfc.util.Support;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;

public enum CollapseComponentProvider implements IComponentProvider {
    INSTANCE;

    private static final Lazy<TagKey<Block>> CAN_START_COLLAPSE =
        Lazy.of(() -> TagKey.create(Registry.BLOCK.key(), new ResourceLocation("tfc", "can_start_collapse")));
    private static final Component SELF_SUPPORTED = new TranslatableComponent("tfc_support_indicator.self_supported").withStyle(ChatFormatting.DARK_GREEN);
    private static final Component SELF_UNSUPPORTED = new TranslatableComponent("tfc_support_indicator.self_unsupported").withStyle(ChatFormatting.GOLD);
    private static final Component WONT_TRIGGER_COLLAPSE = new TranslatableComponent("tfc_support_indicator.wont_trigger_collapse").withStyle(ChatFormatting.DARK_GREEN);
    private static final Component MIGHT_TRIGGER_COLLAPSE = new TranslatableComponent("tfc_support_indicator.might_trigger_collapse").withStyle(ChatFormatting.RED);

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {

        if (!accessor.getBlockState().is(CAN_START_COLLAPSE.get()))
            return;

        var level = accessor.getLevel();
        var pos = accessor.getPosition();

        // the max collapse check radius is 4x2x4
        boolean isSupported = Support.isSupported(level, accessor.getPosition());
        boolean mightTriggerCollapse = Support.findUnsupportedPositions(
                level,
                pos.offset(-4, -2, -4),
                pos.offset(4, 2, 4)
            ).stream()
            .anyMatch(u -> CollapseRecipe.canStartCollapse(level, u));

        tooltip.add(isSupported ? SELF_SUPPORTED : SELF_UNSUPPORTED);
        tooltip.add(mightTriggerCollapse ? MIGHT_TRIGGER_COLLAPSE : WONT_TRIGGER_COLLAPSE);
    }
}
