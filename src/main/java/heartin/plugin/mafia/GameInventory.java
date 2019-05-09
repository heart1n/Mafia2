package heartin.plugin.mafia;


import heartin.plugin.mafia.Ability.Ability;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GameInventory {


    private Inventory inventory;

    private final GameProcess process;

    GameInventory(GameProcess process) {

        this.process = process;
    }

    // 메인 인벤토리
    public Inventory mainInventory(Inventory inventory, Player player) {
        inventory = Bukkit.createInventory(null, 27, "Inventory Name");

        inventory.setItem(11, createItem("§6투표하기", Material.APPLE));
        inventory.setItem(13, createItem("§d채팅모드", Material.CAKE));


        GamePlayer gamePlayer = process.getPlayerManager().getGamePlayer(player);

        Ability ability = process.getPlayerManager().getAbility(gamePlayer);


        if (ability.getAbilityType() == Ability.Type.MAFIA) {
            inventory.setItem(15, createItem("§b직업능력", Material.DIAMOND_SWORD));
        } else if (ability.getAbilityType() == Ability.Type.DOCTOR) {
            inventory.setItem(15, createItem("§e직업능력", Material.BED));
        } else if (ability.getAbilityType() == Ability.Type.POLICE) {
            inventory.setItem(15, createItem("§d직업능력", Material.STICK));
        } else if (ability.getAbilityType() == Ability.Type.SPY) {
            inventory.setItem(15, createItem("§4직업능력", Material.DIAMOND));
        }

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, createItem(" ", Material.STAINED_GLASS_PANE));
            inventory.setItem(i + 18, createItem(" ", Material.STAINED_GLASS_PANE));
        }

        inventory.setItem(9, createItem(" ", Material.STAINED_GLASS_PANE));
        inventory.setItem(17, createItem(" ", Material.STAINED_GLASS_PANE));

        return inventory;
    }


    // 투표 인벤토리
    public Inventory voteInventory(Inventory voteInventory, Player player) {
        voteInventory = Bukkit.createInventory(null, 9, "Vote Inventory");

        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        skull.setDurability((short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

            skullMeta.setOwningPlayer(gamePlayer.getPlayer());
            skullMeta.setDisplayName(gamePlayer.getName());
            skull.setItemMeta(skullMeta);

            voteInventory.addItem(skull);
        }

        return voteInventory;
    }

    // 마피아 인벤토리
    public Inventory mafiaInventory(Inventory mafiaInventory, Player player) {
        mafiaInventory = Bukkit.createInventory(null, 9, "Mafia Inventory");

        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        skull.setDurability((short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

            skullMeta.setOwningPlayer(gamePlayer.getPlayer());
            skullMeta.setDisplayName(gamePlayer.getName());
            skull.setItemMeta(skullMeta);

            mafiaInventory.addItem(skull);
        }

        return mafiaInventory;
    }

    // 의사 인벤토리
    public Inventory doctorInventory(Inventory doctorInventory, Player player) {
        doctorInventory = Bukkit.createInventory(null, 9, "Doctor Inventory");


        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        skull.setDurability((short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

            skullMeta.setOwningPlayer(gamePlayer.getPlayer());
            skullMeta.setDisplayName(gamePlayer.getName());
            skull.setItemMeta(skullMeta);

            doctorInventory.addItem(skull);
        }

        return doctorInventory;
    }

    // 경찰 인벤토리
    public Inventory policeInventory(Inventory policeInventory, Player player) {
        policeInventory = Bukkit.createInventory(null, 9, "Police Inventory");


        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        skull.setDurability((short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

            skullMeta.setOwningPlayer(gamePlayer.getPlayer());
            skullMeta.setDisplayName(gamePlayer.getName());
            skull.setItemMeta(skullMeta);

            policeInventory.addItem(skull);
        }

        return policeInventory;
    }

    public Inventory spyInventory(Inventory spyInventory, Player player) {
        spyInventory = Bukkit.createInventory(null, 9, "Spy Inventory");


        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        skull.setDurability((short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        for (GamePlayer gamePlayer : process.getPlayerManager().getOnlinePlayers()) {

            skullMeta.setOwningPlayer(gamePlayer.getPlayer());
            skullMeta.setDisplayName(gamePlayer.getName());
            skull.setItemMeta(skullMeta);

            spyInventory.addItem(skull);
        }

        return spyInventory;
    }

    public ItemStack createItem(String name, Material material) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }


    public void show(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().updateInventory();
        gamePlayer.getPlayer().openInventory(mainInventory(inventory, gamePlayer.getPlayer()));

    }

    public void showVoteInventory(GamePlayer gamePlayer) {

        gamePlayer.getPlayer().updateInventory();
        gamePlayer.getPlayer().openInventory(voteInventory(inventory, gamePlayer.getPlayer()));

    }

    public void showMafiaInventory(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().updateInventory();
        gamePlayer.getPlayer().openInventory(mafiaInventory(inventory, gamePlayer.getPlayer()));

    }

    public void showDoctorInventory(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().updateInventory();
        gamePlayer.getPlayer().openInventory(doctorInventory(inventory, gamePlayer.getPlayer()));

    }

    public void showPoliceInventory(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().updateInventory();
        gamePlayer.getPlayer().openInventory(policeInventory(inventory, gamePlayer.getPlayer()));

    }

    public void showSpyInventory(GamePlayer gamePlayer)
    {

        gamePlayer.getPlayer().updateInventory();
        gamePlayer.getPlayer().openInventory(spyInventory(inventory, gamePlayer.getPlayer()));
    }


}
