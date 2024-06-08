package pixelfox.easytp.commands.HomeCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;

import java.util.ArrayList;
import java.util.List;

public class DelHome implements CommandExecutor, TabCompleter {

    private final EasyTP plugin;

    public DelHome(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("Usage: /delhome <name>");
                return true;
            }

            String homeName = args[0];
            String uuid = player.getUniqueId().toString();

            if (!plugin.getDatabaseManager().homeExists(uuid, homeName)) {
                player.sendMessage("No home found with the name '" + homeName + "'.");
                return true;
            }

            plugin.getDatabaseManager().deleteHome(uuid, homeName);
            player.sendMessage("Home '" + homeName + "' deleted.");

            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player && args.length == 1) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            List<String> homes = plugin.getDatabaseManager().getHomes(uuid);
            List<String> suggestions = new ArrayList<>();
            for (String home : homes) {
                if (home.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(home);
                }
            }
            return suggestions;
        }
        return null;
    }

}
