package heartin.plugin.mafia;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collection;
import java.util.Collections;

public class GameInventory {


    private Inventory inventory;
    private Inventory voteInventory;
    private final GameProcess process;

    GameInventory(GameProcess process) {

        this.process = process;
        inventory = Bukkit.createInventory(null, 27, "Inventory Name");
        inventory.setItem(11, createItem("§6투표하기", Material.APPLE));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, createItem(" ", Material.STAINED_GLASS_PANE));
            inventory.setItem(i + 18, createItem(" ", Material.STAINED_GLASS_PANE));
        }

        inventory.setItem(9, createItem(" ", Material.STAINED_GLASS_PANE));
        inventory.setItem(17, createItem(" ", Material.STAINED_GLASS_PANE));

    }

    public Inventory voteInventory(Inventory voteInventory, Player player) {
        voteInventory = Bukkit.createInventory(null, 9, "Vote Inventory");

        GamePlayer gamePlayer = (GamePlayer)process.getPlayerManager().getGamePlayer(player);

            for (Player players : Bukkit.getOnlinePlayers())
            {
                ItemStack skull = new ItemStack(Material.SKULL_ITEM);
                skull.setDurability((short) SkullType.PLAYER.ordinal());
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                skullMeta.setOwningPlayer(players.getPlayer());
                skullMeta.setDisplayName(players.getName());
                skull.setItemMeta(skullMeta);

                voteInventory.addItem(skull);
            }

      /*  for (int i = 0; i < 9; i++) {
            voteInventory.setItem(i, createItem(" ", Material.STAINED_GLASS_PANE));
            voteInventory.setItem(i + 18, createItem(" ", Material.STAINED_GLASS_PANE));
        }

        voteInventory.setItem(9, createItem(" ", Material.STAINED_GLASS_PANE));
        voteInventory.setItem(17, createItem(" ", Material.STAINED_GLASS_PANE));

*/
        return voteInventory;
    }

    public ItemStack createItem(String name, Material material) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack skullItem(GamePlayer gamePlayer) {


        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1 , (short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(gamePlayer.getPlayer());
        skullMeta.setDisplayName("§6" + gamePlayer.getName());
        skull.setItemMeta(skullMeta);

        return skull;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
        return;
    }

    public void openVoteInventory(Player player) {

        player.updateInventory();
        player.openInventory(voteInventory(voteInventory, player));

        return;
    }


}
