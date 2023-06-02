package org.main;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static org.main.BukkitEventHandler.hasParentGroup;

public final class WhereYouFrom extends JavaPlugin {

    @Getter
    private static WhereYouFrom instance;

    @Getter
    private static LuckPerms luckPerms;

    @Override
    public void onEnable() {
        instance = this;
        luckPerms = instance.getServer().getServicesManager().getRegistration(LuckPerms.class).getProvider();
        Bukkit.getPluginManager().registerEvents(new BukkitEventHandler(), this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasParentGroup(player.getUniqueId(), "mst") || hasParentGroup(player.getUniqueId(), "holy")) {
                ChoiceInventory.openInventory(player);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
