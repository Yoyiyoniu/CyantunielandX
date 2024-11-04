package me.CyantunielandX;

import me.CyantunielandX.events.PlayerInStation;
import me.CyantunielandX.utils.Prefix;
import me.CyantunielandX.events.PlayerEnterPortalEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerInStation(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerEnterPortalEvent(), this);
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("cyantunielandX") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("cyantunielandX.reload")) {
                sender.sendMessage(Prefix.serverPrefix + " Recargando configuracion");
                this.reloadConfig();
            } else {
                sender.sendMessage(Prefix.serverPrefix + " No tienes permisos para ejecutar este comando");
            }
        }

        return false;
    }
}
