package heartin.plugin.mafia;

import heartin.plugin.mafia.Ability.Ability;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

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

        player.sendMessage(Message.SYSTEM + "당신은 죽었습니다.");

        process.getPlayerManager().checkFinish();
        process.getPlayerManager().checkCitizen();

        event.setDeathMessage(null);
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
                    process.getChat().changePlayerChat(gamePlayer);
                }
                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§b직업능력")) {
                    process.getInventory().showMafiaInventory(gamePlayer);
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§e직업능력")) {
                    process.getInventory().showDoctorInventory(gamePlayer);
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§d직업능력")) {
                    process.getInventory().showPoliceInventory(gamePlayer);
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§4직업능력")) {
                    process.getInventory().showSpyInventory(gamePlayer);
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
                        Player p = Bukkit.getPlayer(playerName);
                        GamePlayer target = process.getPlayerManager().getGamePlayer(p);
                        Ability ability = process.getPlayerManager().getAbility(gamePlayer);

                        if (target.isDead()) {
                            player.sendMessage(Message.SYSTEM + "죽은 플레이어 입니다.");
                        } else {

                            if (ability.getAbilityType() == Ability.Type.POLITICIAN) {

                                process.getVote().vote.put(playerName, Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + 2);
                                process.getVote().removeVote(gamePlayer);
                                player.sendMessage(Message.SYSTEM + "§b" + playerName + "§r에게 투표를 합니다.");
                                Bukkit.broadcastMessage(Message.SYSTEM + "§b" + playerName + "§7: §6" + Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + " §7표");
                            } else {
                                process.getVote().vote.put(playerName, Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + 1);
                                process.getVote().removeVote(gamePlayer);
                                player.sendMessage(Message.SYSTEM + "§b" + playerName + "§r에게 투표를 합니다.");
                                Bukkit.broadcastMessage(Message.SYSTEM + "§b" + playerName + "§7: §6" + Integer.valueOf((Integer) process.getVote().vote.get(playerName)).intValue() + " §7표");
                            }
                        }
                    } else {
                        player.sendMessage(Message.SYSTEM + "이미 투표를 하셨습니다.");
                    }
                } catch (NullPointerException e) {
                    player.sendMessage(Message.SYSTEM + "투표시간이 아닙니다.");
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

                try {
                    if (process.getVote().getAbilityCheck(gamePlayer)) {
                        String playerName = event.getCurrentItem().getItemMeta().getDisplayName();
                        Player p = Bukkit.getPlayer(playerName);
                        GamePlayer target = process.getPlayerManager().getGamePlayer(p);

                        if (target.isDead()) {
                            player.sendMessage(Message.SYSTEM + "죽은 플레이어 입니다.");
                        } else {

                            process.getVote().mafiaVote.put(target, Integer.valueOf((Integer) process.getVote().mafiaVote.get(target)).intValue() + 1);
                            player.sendMessage(Message.SYSTEM + target.getName() + " 에게 §c투표를 합니다.");

                        }
                        process.getVote().removeAbilityCheck(gamePlayer);
                    } else {
                        player.sendMessage(Message.SYSTEM + "이미 능력을 사용했습니다.");
                    }
                } catch (NullPointerException e) {
                    player.sendMessage(Message.SYSTEM + "지금은 사용할 수 없습니다.");
                }


            }
        }
        // 스파이 인벤토리
        if (event.getClickedInventory() != null) {
            if (event.getInventory().getName().equalsIgnoreCase("Spy Inventory")) {
                event.setCancelled(true);

                if (itemStack.getItemMeta() == null) {
                    return;
                }

                String playerName = event.getCurrentItem().getItemMeta().getDisplayName();
                Player p = Bukkit.getPlayer(playerName);
                GamePlayer mafia = process.getPlayerManager().getGamePlayer(p);

                try {
                    if (process.getVote().getAbilityCheck(gamePlayer)) {
                        Ability ability = process.getPlayerManager().getAbility(mafia);
                        if (mafia.isDead()) {
                            player.sendMessage(Message.SYSTEM + "죽은 플레이어 입니다.");
                        } else {
                            if (ability.getAbilityType() == Ability.Type.MAFIA) {
                                mafia.getPlayer().sendMessage(Message.SYSTEM + "§4스파이§r가 §c마피아§r에게 접선했습니다.");
                                player.sendMessage(Message.SYSTEM + "§4스파이§r가 §c마피아§r에게 접선했습니다.");

                                process.getPlayerManager().addMafia(gamePlayer);
                                process.getChat().setMafiachat(gamePlayer);
                            } else {
                                player.sendMessage(Message.SYSTEM  + "§b" +  mafia.getName() + " §r의 직업은§c " + ability.getAbilityName() + " §r입니다.");
                            }


                        }
                        process.getVote().removeAbilityCheck(gamePlayer);
                    } else {
                        player.sendMessage(Message.SYSTEM + "이미 능력을 사용했습니다.");
                    }
                } catch (NullPointerException e) {
                    player.sendMessage(Message.SYSTEM + "지금은 사용할 수 없습니다.");
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
                try {
                    if (process.getVote().getAbilityCheck(gamePlayer)) {
                        String playerName = event.getCurrentItem().getItemMeta().getDisplayName();
                        Player p = Bukkit.getPlayer(playerName);
                        GamePlayer target = process.getPlayerManager().getGamePlayer(p);
                        if (target.isDead()) {
                            player.sendMessage(Message.SYSTEM + "죽은 플레이어 입니다.");
                        } else {

                            process.getVote().setResurrection(target);

                            player.sendMessage(Message.SYSTEM + "§b" + playerName + "를 살립니다.");
                            process.getVote().removeAbilityCheck(gamePlayer);
                        }
                    }
                } catch (NullPointerException e) {
                    player.sendMessage(Message.SYSTEM + "이미 능력을 사용했습니다.");
                }
            }
        }

        //경찰 인벤토리
        if (event.getClickedInventory() != null) {
            if (event.getInventory().getName().equalsIgnoreCase("Police Inventory")) {
                event.setCancelled(true);

                String playerName = event.getCurrentItem().getItemMeta().getDisplayName();
                Player p = Bukkit.getPlayer(playerName);
                GamePlayer target = process.getPlayerManager().getGamePlayer(p);

                try {
                    if (process.getVote().getAbilityCheck(gamePlayer)) {
                        Ability ability = process.getPlayerManager().getAbility(target);

                        if (target.isDead()) {
                            player.sendMessage(Message.SYSTEM + "죽은 플레이어 입니다.");
                        } else {

                            if (ability.getAbilityType() == Ability.Type.MAFIA) {
                                player.sendMessage(Message.SYSTEM + "§b" + target.getName() + " 는 마피아 입니다.");

                            } else {
                                player.sendMessage(Message.SYSTEM + "§b" + target.getName() + "는 마피아가 아닙니다.");

                            }
                            process.getVote().removeAbilityCheck(gamePlayer);
                            process.getVote().clearArrest(gamePlayer);

                        }
                    } else {
                        player.sendMessage(Message.SYSTEM + "이미 능력을 사용했습니다.");
                    }
                } catch (NullPointerException e) {
                    player.sendMessage(Message.SYSTEM + "지금은 사용할 수 없습니다.");
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
            for (GamePlayer deathPlayer : process.getPlayerManager().getDeathPlayers())
                deathPlayer.getPlayer().sendMessage(gamePlayer.getName() + " §7: §c" + event.getMessage());
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
