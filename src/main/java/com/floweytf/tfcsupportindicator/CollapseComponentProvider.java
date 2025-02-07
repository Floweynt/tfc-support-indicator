package com.floweytf.tfcsupportindicator;

import net.dries007.tfc.common.recipes.CollapseRecipe;
import net.dries007.tfc.util.Support;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CollapseComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation COLLAPSE_INDICATOR = new ResourceLocation("tfc_support_indicator:support_indicator");
    private static final Lazy<TagKey<Block>> CAN_START_COLLAPSE =
        Lazy.of(() -> TagKey.create(Registries.BLOCK, new ResourceLocation("tfc", "can_start_collapse")));
    private static final Component SELF_SUPPORTED = Component.translatable("tfc_support_indicator.self_supported").withStyle(ChatFormatting.DARK_GREEN);
    private static final Component SELF_UNSUPPORTED = Component.translatable("tfc_support_indicator.self_unsupported").withStyle(ChatFormatting.GOLD);
    private static final Component WONT_TRIGGER_COLLAPSE = Component.translatable("tfc_support_indicator.wont_trigger_collapse").withStyle(ChatFormatting.DARK_GREEN);
    private static final Component MIGHT_TRIGGER_COLLAPSE = Component.translatable("tfc_support_indicator.might_trigger_collapse").withStyle(ChatFormatting.RED);


    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
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

    @Override
    public ResourceLocation getUid() {
        return COLLAPSE_INDICATOR;
    }
}
