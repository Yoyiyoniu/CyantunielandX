package me.CyantunielandX.events;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.CyantunielandX.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerEnterPortalEvent implements Listener {

    private final Main main = (Main) Main.getPlugin(Main.class);
    private final Map<Player, Long> playerCheckTimes = new HashMap<>();

    @EventHandler
    public void PlayerPortalManager(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(WorldGuard.getInstance().getPlatform().getMatcher().getWorldByName("world"));
        assert regions != null;

        ConfigurationSection portalsSection = main.getConfig().getConfigurationSection("portals");
        if (portalsSection != null) {
            for (String portalName : portalsSection.getKeys(false)) {
                checkRegion(p, regions, portalName, e);
            }
        }
    }

    private void checkRegion(Player p, RegionManager regions, String regionName, PlayerMoveEvent e) {
        if (playerCheckTimes.containsKey(p)) {
            long lastCheckTime = playerCheckTimes.get(p);
            if (lastCheckTime > 0) {
                return;
            }
        }

        ProtectedRegion region = regions.getRegion(regionName);
        if (region != null) {
            boolean isInRegion = region.contains(e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());

            if (isInRegion) {
                playerCheckTimes.put(p, 1L);

                if (p.getInventory().contains(Material.ENDER_PEARL)) {

                    p.playSound(p.getLocation(), "minecraft:block.end_portal_frame.fill", 1, 1);
                    p.getInventory().removeItem(new ItemStack(Material.ENDER_PEARL, 1));
                    List<Integer> locs = main.getConfig().getIntegerList("portals." + regionName);
                    double x = locs.get(0);
                    double y = locs.get(1);
                    double z = locs.get(2);
                    float yaw = locs.get(3).floatValue();
                    float pitch = locs.get(4).floatValue();

                    Location teleportLocation = new Location(p.getWorld(), x, y, z, yaw, pitch);

                    p.teleport(teleportLocation);
                } else {
                    p.sendMessage("You need an ender pearl to enter the portal.");
                    p.setVelocity(p.getLocation().getDirection().multiply(-0.6).setY(0.8));
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        playerCheckTimes.remove(p);
                    }
                }.runTaskLater(main, 20L);
            } else {
                playerCheckTimes.remove(p);
            }
        }
    }
}