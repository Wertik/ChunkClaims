package me.wertik.chunkclaims.handlers;

import me.wertik.chunkclaims.ConfigLoader;
import me.wertik.chunkclaims.Main;
import me.wertik.chunkclaims.nbt.NBTEditor;
import me.wertik.chunkclaims.nbt.NBTUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ToolHandler implements Listener {

    public ToolHandler() {
    }

    public ItemStack createTool(int uses) {

        ConfigLoader configLoader = Main.getInstance().getConfigLoader();

        String name = configLoader.getCFString("tool.name");

        List<String> lore = configLoader.parseList(configLoader.getCFList("tool.lore"), null, uses);

        Material type;

        try {
            type = Material.valueOf(configLoader.config.getString("tool.item-type"));
        } catch (IllegalArgumentException e) {
            Main.getInstance().getLogger().severe("Material type " + configLoader.config.getString("tool.item-type") + " invalid, cannot create tool.");
            return null;
        }

        ItemStack tool = new ItemStack(type, 1);
        ItemMeta itemMeta = tool.getItemMeta();

        itemMeta.setLore(lore);
        itemMeta.setDisplayName(name);

        if (configLoader.config.getBoolean("tool.glowing")) {
            tool.addUnsafeEnchantment(Enchantment.LUCK, 1);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        tool.setItemMeta(itemMeta);

        // NBT
        tool = NBTEditor.writeNBT(tool, "chunkclaims_uses", String.valueOf(uses));

        return tool;
    }

    public ItemStack takeUse(ItemStack item) {
        // NBT
        return createTool(getUses(item) - 1);
    }

    public int getUses(ItemStack item) {
        return Integer.valueOf(NBTUtils.strip(NBTEditor.getNBT(item, "chunkclaims_uses")));
    }

    public boolean isTool(ItemStack item) {
        return NBTEditor.hasNBTTag(item, "chunkclaims_uses");
    }
}
