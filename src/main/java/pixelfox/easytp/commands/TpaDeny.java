package pixelfox.easytp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;
import pixelfox.easytp.requests.TPAcommand;

import java.util.UUID;

public class TpaDeny implements CommandExecutor {

    private final EasyTP plugin;

    public TpaDeny(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID requesterUUID = TPAcommand.teleportRequests.get(player.getUniqueId());

            if (requesterUUID == null) {
                player.sendMessage("You have no pending teleport requests.");
                return true;
            }

            Player requester = plugin.getServer().getPlayer(requesterUUID);
            if (requester == null) {
                player.sendMessage("The player who requested to teleport is no longer online.");
                return true;
            }

            TPAcommand.teleportRequests.remove(player.getUniqueId());
            player.sendMessage("You have denied the teleport request.");
            requester.sendMessage("Your teleport request has been denied.");

            return true;
        }

        return false;
    }

}
