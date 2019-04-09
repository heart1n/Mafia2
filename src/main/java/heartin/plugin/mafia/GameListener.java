package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {

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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        GameProcess process = this.process;
        Player player = event.getEntity();
        Player killer = player.getKiller();
       Ability mafia = process.getPlayerManager().getMafia(player);


        process.getPlayerManager().setDeath(player);

        player.sendMessage("당신은 죽었습니다.");

        process.getPlayerManager().checkFinish();
        process.getPlayerManager().checkCitizen();
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        Player player = event.getPlayer();

        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(event.getPlayer());
        GameChat.ChatMode mode = GameChat.playerChat.get(gamePlayer);


        if (mode == GameChat.ChatMode.GENERAL)
        {
            for (GamePlayer onlinePlayer : process.getPlayerManager().getOnlinePlayers())
                onlinePlayer.getPlayer().sendMessage(gamePlayer.getName() + " §7: §e" + event.getMessage());
        } else if (mode == GameChat.ChatMode.MAFIA)
        {

            for (GamePlayer onlinePlayer : process.getPlayerManager().getOnlineMafia())
                onlinePlayer.getPlayer().sendMessage(gamePlayer.getName() + " §7: §c" + event.getMessage());
        } else if (mode == GameChat.ChatMode.DEATH)
        {
            for (GamePlayer onlinePlayer : process.getPlayerManager().getDeathPlayers())
            {
                onlinePlayer.getPlayer().sendMessage(gamePlayer.getName() + " §7: §7" + event.getMessage());

            }
        }
    }
}
