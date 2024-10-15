package ru.SmrItems.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import ru.SmrItems.SmMain;

import java.util.List;

public class VanillaCase extends Item {
    public VanillaCase(String name, String texture, int maxStackSize) {
        this.setUnlocalizedName(name);
        this.setTextureName("smritems:" + texture);
        this.setCreativeTab(SmMain.tabsmritems);
        this.maxStackSize = maxStackSize;
        GameRegistry.registerItem(this, name);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltip) {
        list.add(EnumChatFormatting.DARK_PURPLE + "Содержит редкие предметы из Minecraft");
    }
}
