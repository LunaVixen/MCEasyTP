package pixelfox.easytp.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;

import java.util.Random;

public class TPR implements CommandExecutor {

    private final EasyTP plugin;
    private final Random random = new Random();

    public TPR(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players.");
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();

        int maxX = 10000; // Set your maximum X coordinate
        int maxZ = 10000; // Set your maximum Z coordinate

        // Generate random coordinates
        int x = random.nextInt(maxX * 2) - maxX;
        int z = random.nextInt(maxZ * 2) - maxZ;
        int y = world.getHighestBlockYAt(x, z);

        Location randomLocation = new Location(world, x, y + 1, z);

        // Set the last location before teleporting
        plugin.getBackCommand().setLastLocation(player);

        player.teleport(randomLocation);
        player.sendMessage("Teleported to a random location: " + randomLocation.getBlockX() + ", " + randomLocation.getBlockY() + ", " + randomLocation.getBlockZ());

        return true;
    }

}
