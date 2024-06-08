package pixelfox.easytp.commands.HomeCommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetHome implements CommandExecutor {


    private final EasyTP plugin;

    public SetHome(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage("Usage: /sethome <name>");
                return true;
            }

            String homeName = args[0];
            String uuid = player.getUniqueId().toString();

            if (plugin.getDatabaseManager().homeExists(uuid, homeName)) {
                player.sendMessage("A home with that name already exists.");
                return true;
            }

            Location loc = player.getLocation();
            String sql = "INSERT OR REPLACE INTO homes (uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = plugin.getDatabaseManager().getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, uuid);
                pstmt.setString(2, homeName);
                pstmt.setString(3, loc.getWorld().getName());
                pstmt.setDouble(4, loc.getX());
                pstmt.setDouble(5, loc.getY());
                pstmt.setDouble(6, loc.getZ());
                pstmt.setFloat(7, loc.getYaw());
                pstmt.setFloat(8, loc.getPitch());
                pstmt.executeUpdate();
                player.sendMessage("Home '" + homeName + "' set!");
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage("An error occurred while setting your home.");
            }

            return true;
        }
        return false;
    }

}
