package org.main;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class BukkitEventHandler implements Listener {
    @EventHandler
    public void  onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (hasParentGroup(player, "mst") ||
                hasParentGroup(player, "holy")) {
            ChoiceInventory.openInventory(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (ChoiceInventory.getInventory().equals(event.getInventory())) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                assert clickedItem.getItemMeta() != null;
                String itemName = clickedItem.getItemMeta().getDisplayName();
                if (itemName.equals("HolyWorld")) {
                    grantParentGroup(event.getWhoClicked(), "holy");
                } else if (itemName.equals("MST Network")) {
                    grantParentGroup(event.getWhoClicked(), "holy");
                }
                event.getWhoClicked().closeInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (ChoiceInventory.getInventory().equals(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    public void grantParentGroup(HumanEntity player, String parentGroupName) {
        LuckPerms luckPerms = WhereYouFrom.getLuckPerms();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return;
        }

        Group parentGroup = luckPerms.getGroupManager().getGroup(parentGroupName);
        if (parentGroup == null) {
            return;
        }

        InheritanceNode inheritanceNode = InheritanceNode.builder(parentGroup)
                .value(true)
                .build();

        user.data().add(inheritanceNode);

        luckPerms.getUserManager().saveUser(user);

        Bukkit.getScheduler().runTask(WhereYouFrom.getInstance(), () -> {
            Player onlinePlayer = Bukkit.getPlayer(player.getUniqueId());
            if (onlinePlayer != null) {
                luckPerms.getUserManager().cleanupUser(user);
                luckPerms.getUserManager().loadUser(onlinePlayer.getUniqueId());
            }
        });
    }

    public static boolean hasParentGroup(Player player, String parentGroupName) {
        LuckPerms luckPerms = WhereYouFrom.getLuckPerms();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return false;
        }

        Group parentGroup = luckPerms.getGroupManager().getGroup(parentGroupName);
        if (parentGroup == null) {
            return false;
        }
        return !(user.getCachedData().getPermissionData().checkPermission("group." + parentGroupName).asBoolean());
    }
}
