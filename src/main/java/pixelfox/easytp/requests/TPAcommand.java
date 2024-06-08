package pixelfox.easytp.requests;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;
import pixelfox.easytp.commands.BackCommand;

import java.util.HashMap;
import java.util.UUID;

public class TPAcommand implements CommandExecutor {
    private final EasyTP plugin;
    public static HashMap<UUID, UUID> teleportRequests = new HashMap<>();

    public TPAcommand(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("Usage: /tpa <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("Player not found.");
                return true;
            }

            teleportRequests.put(target.getUniqueId(), player.getUniqueId());
            target.sendMessage(player.getName() + " has requested to teleport to you. Type /tpaccept to accept or /tpadeny to deny.");
            player.sendMessage("Teleport request sent to " + target.getName());

            plugin.getBackCommand().setLastLocation(player, player.getLocation());

            return true;
        }

        return false;
    }

}
