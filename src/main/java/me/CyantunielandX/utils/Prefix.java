package me.CyantunielandX.utils;

import org.bukkit.Bukkit;

public class Prefix {
    public static String serverPrefix = "§x§7§D§E§9§F§8§lC§x§8§9§E§8§F§6§ly§x§9§5§E§7§F§4§la§x§A§1§E§6§F§2§ln§x§A§D§E§5§F§1§lt§x§B§9§E§4§E§F§lu§x§C§5§E§3§E§D§ln§x§D§1§E§2§E§B§li§x§D§D§E§1§E§9§le§x§D§B§D§D§E§B§ll§x§D§8§D§A§E§E§la§x§D§6§D§6§F§0§ln§x§D§3§D§2§F§3§ld§x§D§1§C§E§F§5§lX §x§C§C§C§7§F§A§l¦§x§C§9§C§3§F§C§l> ";

    public static void cmd(String message){
        Bukkit.getConsoleSender().sendMessage(serverPrefix + ":" + message);
    }
}
