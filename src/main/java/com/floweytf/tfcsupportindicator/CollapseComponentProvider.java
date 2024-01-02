package com.floweytf.tfcsupportindicator;

import net.dries007.tfc.util.Support;
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

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(!accessor.getBlockState().is(CAN_START_COLLAPSE.get()))
            return;

        tooltip.add(Component.translatable(Support.isSupported(accessor.getLevel(), accessor.getPosition()) ?
            "tfc_support_indicator.supported" : "tfc_support_indicator.not_supported" ));
    }

    @Override
    public ResourceLocation getUid() {
        return COLLAPSE_INDICATOR;
    }
}
