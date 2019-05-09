package heartin.plugin.mafia;

import heartin.plugin.mafia.heartin.plugin.command.*;
import nemo.mc.command.bukkit.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GamePlugin extends JavaPlugin {

    private static GamePlugin instance;

    private GameProcess process;

    public static GamePlugin getInstance() {
        return instance;
    }

    public void onEnable() {

        setupCommand();
        instance = this;
    }

    private void setupCommand() {
        new CommandManager().addHelp("help").addComponent("start", new CommandGameStart()).addComponent("stop", new CommandGameStop()).addComponent("chat", new CommandGameChat()).addComponent("vote", new CommandGameVote()).addComponent("inventory", new CommandGameInventory()).register(getCommand("game"));
    }

    public boolean processStart() {
        if (this.process != null) {
            return false;
        }
        saveDefaultConfig();
        reloadConfig();
        GameConfig.load(getConfig());

        this.process = new GameProcess(this);

        return true;
    }

    public boolean processStop() {
        if (this.process == null) {
            return false;
        }
        this.process.unregister();
        this.process = null;

        return true;
    }

    public GameProcess getProcess()
    {
        return process;
    }


}
