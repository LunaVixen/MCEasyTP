package pixelfox.easytp;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pixelfox.easytp.Utils.DatabaseManager;
import pixelfox.easytp.commands.BackCommand;
import pixelfox.easytp.commands.HomeCommands.*;
import pixelfox.easytp.commands.TPR;
import pixelfox.easytp.requests.TPAcommand;
import pixelfox.easytp.commands.TPAccept;
import pixelfox.easytp.commands.TpaDeny;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class EasyTP extends JavaPlugin implements Listener {

    private DatabaseManager databaseManager;
    private BackCommand backCommand;
    private Map<UUID, Location> deathLocations = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("EasyTP is booting up!");

        File dataFolder = new File(getDataFolder(), "EasyTP");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        databaseManager = new DatabaseManager();
        databaseManager.connect();


        this.getCommand("tpa").setExecutor(new TPAcommand(this));
        this.getCommand("tpaccept").setExecutor(new TPAccept(this));
        this.getCommand("tpadeny").setExecutor(new TpaDeny(this));
        this.getCommand("sethome").setExecutor(new SetHome(this));
        this.getCommand("home").setExecutor(new Homes(this));
        this.getCommand("delhome").setExecutor(new DelHome(this));
        this.getCommand("homes").setExecutor(new HomeList(this));
        this.getCommand("tpr").setExecutor(new TPR(this));
        this.getCommand("death").setExecutor(new Death(this));
        this.getCommand("homesview").setExecutor(new HomesView(this));
        this.getCommand("playerhometp").setExecutor(new PlayerHomeTP(this));

        backCommand = new BackCommand(this);
        this.getCommand("back").setExecutor(backCommand);


        this.getCommand("home").setTabCompleter(new Homes(this));
        this.getCommand("home").setTabCompleter(new DelHome(this));


        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("EasyTP is shutting down!");
        databaseManager.close();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public BackCommand getBackCommand() {
        return backCommand;
    }

    public Location getDeathLocation(UUID playerUUID) {
        return deathLocations.get(playerUUID);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        UUID playerUUID = event.getEntity().getUniqueId();
        Location deathLocation = event.getEntity().getLocation();
        deathLocations.put(playerUUID, deathLocation);

        String deathMessage = String.format("You died at X: %d, Y: %d, Z: %d",
                deathLocation.getBlockX(), deathLocation.getBlockY(), deathLocation.getBlockZ());
        event.getEntity().sendMessage(deathMessage);
    }

}
