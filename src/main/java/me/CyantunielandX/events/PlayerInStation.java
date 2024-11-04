package me.CyantunielandX.events;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.CyantunielandX.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerInStation implements Listener {
    private final Main main = (Main)Main.getPlugin(Main.class);
    private final List<Player> blockPlayers = new ArrayList<>();

    @EventHandler
    public void PlayerBridgeManager(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!this.blockPlayers.contains(p)) {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(WorldGuard.getInstance().getPlatform().getMatcher().getWorldByName("world"));

            assert regions != null;

            for (String regionName : Objects.requireNonNull(this.main.getConfig().getConfigurationSection("regions")).getKeys(false)) {
                getRegion(p, regions, regionName, this.blockPlayers, this.main);
            }

        }
    }

    private void getRegion(Player p, RegionManager regions, String regionName, List<Player> blockPlayers, Main main) {
        ProtectedRegion region = regions.getRegion(regionName);

        if (region != null && region.contains(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ())) {
            blockPlayers.add(p);
            List<Integer> locs = main.getConfig().getIntegerList("regions." + regionName);
            double x = (double) locs.get(0);
            double y = (double) locs.get(1);
            double z = (double) locs.get(2);

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "screeneffect fullscreen #0F0F11 10 10 10 nofreeze " + p.getName());
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location teleportLocation = new Location(p.getWorld(), x, y, z, p.getYaw(), p.getPitch());
                    p.teleport(teleportLocation);
                    blockPlayers.remove(p);
                }
            }.runTaskLater(main, 10L);
        }
    }
}
