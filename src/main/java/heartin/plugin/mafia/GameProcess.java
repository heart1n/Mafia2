package heartin.plugin.mafia;

import nemo.mc.event.ASMEventExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

public final class GameProcess {

    private final GamePlugin plugin;
    private final GamePlayerManager playerManager;
    private final GameListener listener;
    private final GameScheduler scheduler;
    private final BukkitTask bukkitTask;

    GameProcess(GamePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = new GamePlayerManager(this);
        this.listener = new GameListener(this);
        this.scheduler = new GameScheduler(this);
        this.bukkitTask = plugin.getServer().getScheduler().runTaskTimer(plugin, this.scheduler, 0L, 1L);

        ASMEventExecutor.registerEvents(this.listener, plugin);

    }

    public GamePlugin getPlugin() {
        return this.plugin;
    }

    public GamePlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public GameScheduler getScheduler() {
        return this.scheduler;
    }

    void unregister() {

        HandlerList.unregisterAll(this.listener);

        this.bukkitTask.cancel();

        GamePlayerManager playerManager = this.playerManager;
    }
}

