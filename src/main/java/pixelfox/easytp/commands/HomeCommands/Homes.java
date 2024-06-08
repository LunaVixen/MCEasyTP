package pixelfox.easytp.commands.HomeCommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;
import pixelfox.easytp.commands.BackCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Homes implements CommandExecutor, TabCompleter {

    private final EasyTP plugin;

    public Homes(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("Usage: /home <name>");
                return true;
            }

            String homeName = args[0];
            String sql = "SELECT world, x, y, z, yaw, pitch FROM homes WHERE uuid = ? AND name = ?";

            try (Connection conn = plugin.getDatabaseManager().getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, player.getUniqueId().toString());
                pstmt.setString(2, homeName);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String world = rs.getString("world");
                    double x = rs.getDouble("x");
                    double y = rs.getDouble("y");
                    double z = rs.getDouble("z");
                    float yaw = rs.getFloat("yaw");
                    float pitch = rs.getFloat("pitch");
                    Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                    plugin.getBackCommand().setLastLocation(player);
                    player.teleport(loc);
                    player.sendMessage("Teleported to home '" + homeName + "'!");
                } else {
                    player.sendMessage("No home found with the name '" + homeName + "'. Use /sethome <name> to set your home.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage("An error occurred while teleporting to your home.");
            }


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
