package pixelfox.easytp.commands.HomeCommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;

public class Death implements CommandExecutor {

    private final EasyTP plugin;

    public Death(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players.");
            return true;
        }

        Player player = (Player) sender;
        Location deathLocation = plugin.getDeathLocation(player.getUniqueId());

        if (deathLocation == null) {
            player.sendMessage("You have no recorded death location.");
            return true;
        }

        player.teleport(deathLocation);
        player.sendMessage("Teleported to your last death location: " + deathLocation.getBlockX() + ", " + deathLocation.getBlockY() + ", " + deathLocation.getBlockZ());

        return true;
    }

}
