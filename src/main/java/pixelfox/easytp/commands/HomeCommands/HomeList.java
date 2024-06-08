package pixelfox.easytp.commands.HomeCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;

import java.util.List;

public class HomeList implements CommandExecutor {

    private final EasyTP plugin;

    public HomeList(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            List<String> homes = plugin.getDatabaseManager().getHomes(uuid);

            if (homes.isEmpty()) {
                player.sendMessage("You have no homes set.");
            } else {
                player.sendMessage("Your homes: " + String.join(", ", homes));
            }
            return true;
        }
        return false;
    }

}
