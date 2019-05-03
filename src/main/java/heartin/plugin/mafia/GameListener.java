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
                    Ability ability = process.getPlayerManager().getAbility(gamePlayer);
                    if (ability.abilityType() == Ability.Type.MAFIA) {
                        process.getChat().setMafiaChat(player);
                    } else {
                        player.sendMessage("당신은 마피아가 아닙니다.");
                    }
                }
                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§b직업능력")) {
                    process.getInventory().showMafiaInventory(gamePlayer);
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§e직업능력")) {
                    process.getInventory().showDoctorInventory(gamePlayer);
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§d직업능력")) {
                    process.getInventory().showPoliceInventory(gamePlayer);
                }
            }
            // Vote inventory
            if (event.getInventory().getName().equalsIgnoreCase("Vote Inventory")) {
                event.setCancelled(true);

                if (itemStack.getItemMeta() == null) {
                    return;
                }

                try {
                    if (process.getVote().voteCheckPlayer.get(gamePlayer)) {
                        String playerName = event.getCurrentItem().getItemMeta().getDisplayName();

                        process.getVote().vote.put(playerName, Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + 1);
                        process.getVote().removeVote(gamePlayer);
                        player.sendMessage(playerName + "에게 투표를 합니다.");
                        Bukkit.broadcastMessage(playerName + ": " + Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + "표");
                    } else {
                        player.sendMessage("이미 투표를 하셨습니다.");
                    }

                } catch (NullPointerException e) {
                    player.sendMessage("투표시간이 아닙니다.");
                }
            }
        }

        // 마피아 인벤토리
        if (event.getClickedInventory() != null) {
            if (event.getInventory().getName().equalsIgnoreCase("Mafia Inventory")) {
                event.setCancelled(true);

                if (itemStack.getItemMeta() == null) {
                    return;
                }
                //  if (process.getVote().getVote(gamePlayer))
                {
                    String playerName = event.getCurrentItem().getItemMeta().getDisplayName();

                    process.getVote().vote.put(playerName, Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + 1);
                    process.getVote().removeVote(gamePlayer);
                    player.sendMessage(playerName + "에게 §c투표를 합니다.");
                    player.sendMessage(Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + "표");
                }
            }
        }

        // 의사 인벤토리
        if (event.getClickedInventory() != null) {
            if (event.getInventory().getName().equalsIgnoreCase("Doctor Inventory")) {
                event.setCancelled(true);

                if (itemStack.getItemMeta() == null) {
                    return;
                }

                String playerName = event.getCurrentItem().getItemMeta().getDisplayName();
                process.getVote().setResurrection(playerName);

                player.sendMessage(playerName + "를 살립니다.");
                player.sendMessage(process.getVote().resurrection.get(playerName) + " = value");
            }
        }

        //경찰 인벤토리
        if (event.getClickedInventory() != null) {
            if (event.getInventory().getName().equalsIgnoreCase("Police Inventory")) {
                event.setCancelled(true);


                String playerName = event.getCurrentItem().getItemMeta().getDisplayName();
                Player displayPlayer = Bukkit.getPlayer(playerName);
                GamePlayer mafia = process.getPlayerManager().getGamePlayer(displayPlayer);
                player.sendMessage(mafia.getName() + "를 조사합니다.");

                try {

                    if (process.getVote().arrest.get(gamePlayer)) {
                        Ability ability = process.getPlayerManager().getAbility(mafia);
                        if (ability.abilityType() == Ability.Type.MAFIA) {
                            gamePlayer.getPlayer().sendMessage(mafia.getName() + "마피아 입니다.");

                        } else {
                            gamePlayer.getPlayer().sendMessage(mafia.getName() + "지목한 사람은 마피아가 아닙니다.");

                        }
                    } else {
                        gamePlayer.getPlayer().sendMessage("이미 골랐습니다.");
                    }
                    process.getVote().clearArrest(gamePlayer);

                } catch (NullPointerException e) {
                    player.sendMessage("지금은 사용할 수 없습니다.");
                }
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
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if ((item != null) && (item.getType() == Material.STICK)) {
            Action action = event.getAction();

            if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) {
                Player player = event.getPlayer();
                GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

                process.getInventory().show(gamePlayer);
            }
        }

    }
}
