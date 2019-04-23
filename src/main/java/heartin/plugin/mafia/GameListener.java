package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GameListener implements Listener {

    private final GameProcess process;


    GameListener(GameProcess process) {
        this.process = process;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        process.getPlayerManager().registerGamePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        process.getPlayerManager().unregisterGamePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        GameProcess process = this.process;
        Player player = event.getEntity();

        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);
        Player killer = player.getKiller();
        Ability mafia = process.getPlayerManager().getMafia(gamePlayer);


        process.getPlayerManager().setDeath(player);

        player.sendMessage("당신은 죽었습니다.");

        process.getPlayerManager().checkFinish();
        process.getPlayerManager().checkCitizen();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        GamePlayer gamePlayer = (GamePlayer) process.getPlayerManager().getGamePlayer(player);

        // Main inventory
        if (event.getClickedInventory() != null) {
            if (event.getInventory().getName().equalsIgnoreCase("Inventory Name")) {
                event.setCancelled(true);

                if (itemStack.getItemMeta() == null) {
                    return;
                }

                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§6투표하기")) {
                    process.getInventory().showVoteInventory(gamePlayer);
                }
                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§d채팅모드")) {
                    Ability mafia = process.getPlayerManager().getMafia(gamePlayer);
                    if (mafia.abilityType() == Ability.Type.MAFIA) {
                        process.getChat().setMafiaChat(player);
                    } else {
                        player.sendMessage("당신은 마피아가 아닙니다.");
                    }
                }
                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§b직업능력")) {
                    process.getInventory().showMafiaInventory(gamePlayer);
                }
            }
            // Vote inventory
            if (event.getInventory().getName().equalsIgnoreCase("Vote Inventory")) {
                event.setCancelled(true);
                try {
                    if (process.getVote().getVote(gamePlayer)) {
                        String playerName = event.getCurrentItem().getItemMeta().getDisplayName();

                        process.getVote().vote.put(playerName, Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + 1);
                        process.getVote().removeVote(gamePlayer);
                        player.sendMessage(playerName + "에게 투표를 합니다.");
                    } else {
                        player.sendMessage("투표권이 없습니다");
                    }

                } catch (NullPointerException e) {
                    player.sendMessage("투표시간이 아닙니다.");
                }
            }
        }
        // Mafia inventory
        if (event.getInventory().getName().equalsIgnoreCase("Mafia Inventory")) {
            event.setCancelled(true);
            try {
                if (process.getVote().getVote(gamePlayer)) {
                    String playerName = event.getCurrentItem().getItemMeta().getDisplayName();

                    process.getVote().vote.put(playerName, Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + 1);
                    process.getVote().removeVote(gamePlayer);
                    player.sendMessage(playerName + "에게 투표를 합니다.");
                } else {
                    player.sendMessage("투표권이 없습니다");
                }

            } catch (NullPointerException e) {
                player.sendMessage("투표시간이 아닙니다.");
            }
        }
    }


    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        Player player = event.getPlayer();

        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(event.getPlayer());
        GameChat.ChatMode mode = GameChat.playerChat.get(gamePlayer);

        if (mode == GameChat.ChatMode.GENERAL) {
            for (GamePlayer onlinePlayer : process.getPlayerManager().getOnlinePlayers())
                onlinePlayer.getPlayer().sendMessage(gamePlayer.getName() + " §7: §e" + event.getMessage());
        } else if (mode == GameChat.ChatMode.MAFIA) {
            for (GamePlayer onlinePlayer : process.getPlayerManager().getOnlineMafia())
                onlinePlayer.getPlayer().sendMessage(gamePlayer.getName() + " §7: §c" + event.getMessage());
        } else if (mode == GameChat.ChatMode.DEATH) {
            for (GamePlayer onlinePlayer : process.getPlayerManager().getDeathPlayers())
                onlinePlayer.getPlayer().sendMessage(gamePlayer.getName() + " §7: §7" + event.getMessage());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        ItemStack item = event.getItem();

        if ((item != null) && (item.getType() == Material.STICK))
        {
            Action action = event.getAction();

            if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK))
            {
                Player player = event.getPlayer();
                GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

                process.getInventory().show(gamePlayer);
            }
        }

    }
}
