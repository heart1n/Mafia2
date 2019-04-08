package heartin.plugin.mafia;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener  implements Listener {

    private final GameProcess process;

    GameListener(GameProcess process)
    {
        this.process = process;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        process.getPlayerManager().registerGamePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        process.getPlayerManager().unregisterGamePlayer(event.getPlayer());
    }

}
