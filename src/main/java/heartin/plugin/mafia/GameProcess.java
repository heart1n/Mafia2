package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;
import nemo.mc.event.ASMEventExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

public final class GameProcess {

    private final GamePlugin plugin;
    private final GamePlayerManager playerManager;
    private final GameListener listener;
    private final GameScheduler scheduler;
    private final GameScoreboard scoreboard;
    private final GameChat chat;
    private final GameVote vote;
    private final GameInventory inventory;
    private final BukkitTask bukkitTask;

    GameProcess(GamePlugin plugin) {
        this.plugin = plugin;
        this.inventory = new GameInventory(this);
        this.scoreboard = new GameScoreboard(this);
        this.playerManager = new GamePlayerManager(this);
        this.listener = new GameListener(this);
        this.chat = new GameChat(this);
        this.scheduler = new GameScheduler(this);
        this.vote = new GameVote(this);
        this.bukkitTask = plugin.getServer().getScheduler().runTaskTimer(plugin, this.scheduler, 0L, 1L);

        ASMEventExecutor.registerEvents(this.listener, plugin);

    }

    public GamePlugin getPlugin() {
        return this.plugin;
    }

    public GameChat getChat() {
        return this.chat;
    }

    public GamePlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public GameScheduler getScheduler() {

        return this.scheduler;
    }

    public GameInventory getInventory()
    {
        return this.inventory;
    }

    public GameVote getVote() {
        return this.vote;
    }

    public GameScoreboard getScoreboard() {
        return this.scoreboard;
    }


    void unregister() {

        HandlerList.unregisterAll(this.listener);

        this.scoreboard.clear();
        this.bukkitTask.cancel();
        this.chat.playerChat.clear();

        GameScheduler.remainbar.removeAll();
        GamePlayerManager playerManager = this.playerManager;
    }
}

