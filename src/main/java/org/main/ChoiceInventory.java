package org.main;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChoiceInventory {

    @Getter
    private static Inventory inventory;

    public static void createInventory() {
        Inventory lInventory = Bukkit.createInventory(null, 9, "Откуда ты");

        ItemStack mstItem = new ItemStack(Material.BLUE_WOOL);
        ItemMeta mstMeta = mstItem.getItemMeta();
        assert mstMeta != null;
        mstMeta.setDisplayName("MST Network");
        mstItem.setItemMeta(mstMeta);

        ItemStack holyItem = new ItemStack(Material.RED_WOOL);
        ItemMeta holyMeta = holyItem.getItemMeta();
        assert holyMeta != null;
        holyMeta.setDisplayName("HolyWorld");
        holyItem.setItemMeta(holyMeta);

        lInventory.setItem(3, holyItem);
        lInventory.setItem(5, mstItem);
        inventory = lInventory;

    }
    public static void openInventory(Player player) {
        if (inventory != null) {
            player.openInventory(inventory);
        } else {
            createInventory();
            player.openInventory(inventory);
        }
    }
}
