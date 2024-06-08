package pixelfox.easytp.commands.HomeCommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerHomeTP implements CommandExecutor {

    private final EasyTP plugin;

    public PlayerHomeTP(EasyTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("easytp.admin")) {
            player.sendMessage("You do not have permission to teleport to other players' homes.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage("Usage: /playerhometp <player> <home>");
            return true;
        }

        String targetPlayerName = args[0];
        String homeName = args[1];
        String sql = "SELECT world, x, y, z, yaw, pitch FROM homes WHERE player_name = ? AND name = ?";

        try (Connection conn = plugin.getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, targetPlayerName);
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
                player.sendMessage("Teleported to home '" + homeName + "' of player '" + targetPlayerName + "'.");
            } else {
                player.sendMessage("No home found with the name '" + homeName + "' for player '" + targetPlayerName + "'.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while teleporting to the home.");
        }

        return true;
    }

}
