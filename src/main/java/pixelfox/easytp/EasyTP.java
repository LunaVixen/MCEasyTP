package pixelfox.easytp;

import org.bukkit.plugin.java.JavaPlugin;
import pixelfox.easytp.Utils.DatabaseManager;
import pixelfox.easytp.commands.BackCommand;
import pixelfox.easytp.commands.HomeCommands.DelHome;
import pixelfox.easytp.commands.HomeCommands.HomeList;
import pixelfox.easytp.commands.HomeCommands.SetHome;
import pixelfox.easytp.requests.TPAcommand;
import pixelfox.easytp.commands.HomeCommands.Homes;
import pixelfox.easytp.commands.TPAccept;
import pixelfox.easytp.commands.TpaDeny;

import java.io.File;

public final class EasyTP extends JavaPlugin {

    private DatabaseManager databaseManager;
    private BackCommand backCommand;

    @Override
    public void onEnable() {
        getLogger().info("EasyTP is booting up!");

        File dataFolder = new File(getDataFolder(), "EasyTP");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        databaseManager = new DatabaseManager();
        databaseManager.connect();


        this.getCommand("tpa").setExecutor(new TPAcommand(this));
        this.getCommand("tpaccept").setExecutor(new TPAccept(this));
        this.getCommand("tpadeny").setExecutor(new TpaDeny(this));
        this.getCommand("sethome").setExecutor(new SetHome(this));
        this.getCommand("home").setExecutor(new Homes(this));
        this.getCommand("delhome").setExecutor(new DelHome(this));
        this.getCommand("homes").setExecutor(new HomeList(this));

        backCommand = new BackCommand(this);
        this.getCommand("back").setExecutor(backCommand);


        this.getCommand("home").setTabCompleter(new Homes(this));
        this.getCommand("home").setTabCompleter(new DelHome(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("EasyTP is shutting down!");
        databaseManager.close();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public BackCommand getBackCommand() {
        return backCommand;
    }
}
