package pixelfox.easytp.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;

import java.util.HashMap;
import java.util.UUID;

public class BackCommand implements CommandExecutor {

    private final EasyTP plugin;
    private final HashMap<UUID, Location> lastLocations = new HashMap<>();

    public BackCommand(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (!lastLocations.containsKey(playerUUID)) {
            player.sendMessage("No previous location to teleport back to.");
            return true;
        }

        Location lastLocation = lastLocations.get(playerUUID);
        player.teleport(lastLocation);
        player.sendMessage("Teleported back to your previous location.");
        return true;
    }

    public void setLastLocation(Player player, Location location) {
        lastLocations.put(player.getUniqueId(), location);
    }

    public void removeLastLocation(Player player) {
        lastLocations.remove(player.getUniqueId());
    }

}
