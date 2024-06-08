package pixelfox.easytp.commands.HomeCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.easytp.EasyTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomesView implements CommandExecutor {

    private final EasyTP plugin;

    public HomesView(EasyTP plugin) {
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
            player.sendMessage("You do not have permission to view other players' homes.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Usage: /homesview <player>");
            return true;
        }

        String targetPlayerName = args[0];
        String sql = "SELECT name FROM homes WHERE player_name = ?";
        List<String> homes = new ArrayList<>();

        try (Connection conn = plugin.getDatabaseManager().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, targetPlayerName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                homes.add(rs.getString("name"));
            }

            if (homes.isEmpty()) {
                player.sendMessage("No homes found for player " + targetPlayerName);
            } else {
                player.sendMessage("Homes for player " + targetPlayerName + ": " + String.join(", ", homes));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while fetching homes.");
        }

        return true;
    }

}
