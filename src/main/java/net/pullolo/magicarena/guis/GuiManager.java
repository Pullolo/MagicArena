package net.pullolo.magicarena.guis;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import dev.dbassett.skullcreator.SkullCreator;
import net.pullolo.magicarena.data.PlayerData;
import net.pullolo.magicarena.game.Dungeon;
import net.pullolo.magicarena.game.QueueManager;
import net.pullolo.magicarena.items.ItemClass;
import net.pullolo.magicarena.wish.WishSystem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static net.pullolo.magicarena.MagicArena.*;
import static net.pullolo.magicarena.data.PlayerData.getPlayerData;
import static net.pullolo.magicarena.players.ArenaPlayer.isPlayerInGame;
import static net.pullolo.magicarena.wish.WishSystem.getRarityColorChar;
import static net.pullolo.magicarena.wish.WishSystem.getWishRarityAsInt;

public class GuiManager {

    private final JavaPlugin plugin;


    public GuiManager(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public InventoryGui createMainMenuGui(Player player){
        String[] guiSetup = {
                "         ",
                "r g p w i",
                "         "
        };
        InventoryGui gui = new InventoryGui(this.plugin, player, "Main Menu", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        gui.addElement(new StaticGuiElement('g', new ItemStack (Material.COMPASS),
                click -> {
                    click.getGui().close();
                    createGamesMenu((Player) click.getWhoClicked()).show(click.getWhoClicked());
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&a➼ Play")));
        gui.addElement(new StaticGuiElement('w', new ItemStack (Material.NETHER_STAR),
                click -> {
                    click.getGui().close();
                    createWishGui((Player) click.getWhoClicked()).show(click.getWhoClicked());
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&3✧ Wish")));
        gui.addElement(new DynamicGuiElement('p', (viewer)->{
            return new StaticGuiElement('p', getPlayerSkull(player),
                    click -> {
                        click.getGui().close();
                        createProfileMenu((Player) click.getWhoClicked()).show(click.getWhoClicked());
                        return true;
                    },
                    ChatColor.translateAlternateColorCodes('&', "&r&7✉ Your Profile"));
        }));
        gui.addElement(new StaticGuiElement('r', new ItemStack (Material.BEACON),
                click -> {
                    click.getGui().close();
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&6⚡ Ranks")));
        gui.addElement(new StaticGuiElement('i', new ItemStack (Material.CRAFTING_TABLE),
                click -> {
                    click.getGui().close();
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&7✎ Your Items")));

        return gui;
    }
    public InventoryGui createProfileMenu(Player player){
        String[] guiSetup = {
                "    h    ",
                "  lxwsd  ",
                "         "
        };
        InventoryGui gui = new InventoryGui(this.plugin, player, "Select Game Mode", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)); // fill the empty slots with this
        gui.addElement(new DynamicGuiElement('h', (viewer)->{
            return new StaticGuiElement('h', getPlayerSkull(player),
                    ChatColor.translateAlternateColorCodes('&', "&r&7✉ Profile &a" + getPlayerData(player).getName()));
        }));
        gui.addElement(new DynamicGuiElement('l', (viewer)->{
            return new StaticGuiElement('l', new ItemStack(Material.ENCHANTING_TABLE),
                    ChatColor.translateAlternateColorCodes('&', "&r&7Your level: &a" + getPlayerData(player).getLevel() + " ≛"));
        }));
        gui.addElement(new DynamicGuiElement('x', (viewer)->{
            return new StaticGuiElement('x', new ItemStack(Material.EXPERIENCE_BOTTLE),
                    ChatColor.translateAlternateColorCodes('&', "&r&7Your xp: &a" + getPlayerData(player).getXp() + " ❊"));
        }));
        gui.addElement(new DynamicGuiElement('w', (viewer)->{
            return new StaticGuiElement('w', new ItemStack(Material.NETHER_STAR),
                    ChatColor.translateAlternateColorCodes('&', "&r&7Your wishes: &a" + getPlayerData(player).getWishes() + " ✧"));
        }));
        gui.addElement(new DynamicGuiElement('s', (viewer)->{
            return new StaticGuiElement('s', new ItemStack(Material.PRISMARINE_CRYSTALS),
                    ChatColor.translateAlternateColorCodes('&', "&r&7Your star essence: &a" + getPlayerData(player).getStarEssence() + " ✷"));
        }));
        gui.addElement(new DynamicGuiElement('d', (viewer)->{
            return new StaticGuiElement('d', new ItemStack(Material.FIRE_CHARGE),
                    ChatColor.translateAlternateColorCodes('&', "&r&7Your dungeon essence: &a" + getPlayerData(player).getDungeonEssence() + " ✪"));
        }));

        return gui;
    }
    public InventoryGui createGamesMenu(Player player){
        String[] guiSetup = {
                "         ",
                "   p d   ",
                "         "
        };
        InventoryGui gui = new InventoryGui(this.plugin, player, "Select Game Mode", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)); // fill the empty slots with this
        gui.addElement(new StaticGuiElement('p', new ItemStack (Material.COMPASS),
                click -> {
                    click.getGui().close();
                    createGameSelectGui((Player) click.getWhoClicked()).show(click.getWhoClicked());
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&aPlay PvP!")));
        gui.addElement(new StaticGuiElement('d', getPlayerSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM3YjZmNTAxNTRkMTkyZDhjM2E3MmQxM2ZhNDRjOTUzYjQxMTM4NThjOWQyZWRmMjE4ZjUxNzk5OGQ3MzM2YyJ9fX0="),
                click -> {
                    click.getGui().close();
                    createDungeonSelectGui((Player) click.getWhoClicked()).show(click.getWhoClicked());
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&aPlay Dungeons!")));
        return gui;
    }
    public InventoryGui createDungeonSelectGui(Player player){
        String[] guiSetup = {
                "         ",
                "  a b c  ",
                "         "
        };
        InventoryGui gui = new InventoryGui(this.plugin, player, "Select Game", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)); // fill the empty slots with this

        gui.addElement(new StaticGuiElement('a', getPlayerSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWVmOTA0YzY2ZjRjZDM1N2ViODBhMWU0MDU3ODNmNTIxZDI4NDUwMzQzNWFlZGU2MDNjODlkYjE1ZTY4NTcwOSJ9fX0="),
                click -> {
                    click.getGui().close();
                    startDungeon((Player) click.getWhoClicked(), 10);
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&aDungeon Lvl10!")));
        gui.addElement(new StaticGuiElement('b', getPlayerSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBiM2M5MWI3MjdkODdkOGM4YWE5NjAyOGYyMjc1Yjg0MDVkZWJjNzUxNmEwMjNkMGY3NzQ4YmFiMjFmOWM0MyJ9fX0="),
                click -> {
                    click.getGui().close();
                    startDungeon((Player) click.getWhoClicked(), 50);
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&aDungeon Lvl50!")));
        gui.addElement(new StaticGuiElement('c', getPlayerSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM0MDdjM2JkOWJiN2I3N2Q3OWYzN2M0YzVkZmE1MDlmMTYyZWY4NTQ1MTkzODlmNzAxNmI4ZTQ0ZDg2OTI4In19fQ=="),
                click -> {
                    click.getGui().close();
                    startDungeon((Player) click.getWhoClicked(), 90);
                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&aDungeon Lvl90!")));

        return gui;
    }
    public InventoryGui createGameSelectGui(Player player){
        String[] guiSetup = {
                "         ",
                " a b d d ",
                "         "
        };
        InventoryGui gui = new InventoryGui(this.plugin, player, "Select Game", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1)); // fill the empty slots with this
        gui.addElement(new StaticGuiElement('a', new ItemStack (Material.COMPASS),
                click -> {
                    click.getGui().close();
                    if (!partyManager.isPlayerInParty((Player) click.getWhoClicked())){
                        if (gameManager.getQueueManager().isPlayerInQueue((Player) click.getWhoClicked())){
                            gameManager.getQueueManager().removePlayerFromQueue((Player) click.getWhoClicked());
                        } else gameManager.getQueueManager().addPlayerToQueue((Player) click.getWhoClicked(), QueueManager.QueueType.SOLO);
                    } else click.getWhoClicked().sendMessage(ChatColor.RED + "To queue first leave the party!");

                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&aPlay 1v1!")));
        gui.addElement(new StaticGuiElement('b', new ItemStack (Material.COMPASS),
                click -> {
                    click.getGui().close();
                    Player p = (Player) click.getWhoClicked();

                    if (gameManager.getQueueManager().isPlayerInQueue(p)){
                        if (partyManager.isPartyOwner(p)){
                            gameManager.getQueueManager().removePartyFromQueue(new ArrayList<>(partyManager.getPlayersParty(p)));
                        } else if (!partyManager.isPlayerInParty(p)) gameManager.getQueueManager().removePlayerFromQueue(p);
                        else p.sendMessage(ChatColor.RED + "Only the owner can leave queue!");
                    } else {
                        if (partyManager.isPartyOwner(p)){
                            gameManager.getQueueManager().addPartyToQueue(new ArrayList<>(partyManager.getPlayersParty(p)), QueueManager.QueueType.DUO);
                        } else if (!partyManager.isPlayerInParty(p)) gameManager.getQueueManager().addPlayerToQueue(p, QueueManager.QueueType.DUO);
                        else p.sendMessage(ChatColor.RED + "Only the owner can start queue!");
                    }

                    return true;
                }, ChatColor.translateAlternateColorCodes('&', "&r&aPlay 2v2!")));
        gui.addElement(new StaticGuiElement('d', new ItemStack (Material.BARRIER),
                ChatColor.translateAlternateColorCodes('&', "&r&cComing Soon!")));
        return gui;
    }

    public InventoryGui createWishGui(Player player){
        String[] guiSetup = {
                "         ",
                "  a   w  ",
                "    n    "
        };
        InventoryGui gui = new InventoryGui(this.plugin, player, "Wish!", guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
        gui.addElement(new StaticGuiElement('a', new ItemStack(Material.NETHERITE_INGOT),
                click -> {
                    if (!gameManager.getQueueManager().isPlayerInQueue((Player) click.getWhoClicked())){
                        if (getWishSystem().wish((Player) click.getWhoClicked(), WishSystem.WishType.ARMOR_WISH)) click.getGui().close();
                    } else click.getWhoClicked().sendMessage(ChatColor.RED + "You can't wish in queue!");
                    return true;
                },
                ChatColor.translateAlternateColorCodes('&', "&r&3Wish for Armor! &a1 ✧")));
        gui.addElement(new StaticGuiElement('w', new ItemStack(Material.BLAZE_ROD),
                click -> {
                    if (!gameManager.getQueueManager().isPlayerInQueue((Player) click.getWhoClicked())){
                        if (getWishSystem().wish((Player) click.getWhoClicked(), WishSystem.WishType.WEAPON_WISH)) click.getGui().close();
                    } else click.getWhoClicked().sendMessage(ChatColor.RED + "You can't wish in queue!");
                    return true;
                },
                ChatColor.translateAlternateColorCodes('&', "&r&cWish for Weapons! &a1 ✧")));
        gui.addElement(new DynamicGuiElement('n', (viewer)->{
            return new StaticGuiElement('n', new ItemStack(Material.NETHER_STAR),
                    ChatColor.translateAlternateColorCodes('&', "&r&7Your wishes: &a" + getPlayerData(player).getWishes() + " ✧"));
        }));

        return gui;
    }

    public ArrayList<InventoryGui> createWishAnim(Player player, WishSystem.WishRarity wishRarity, WishSystem.WishType wishType, int stars, ItemClass itemClass, ItemStack finalItem){
        ArrayList<InventoryGui> finishedList = new ArrayList<>();
        int rarity = getWishRarityAsInt(wishRarity);

        ArrayList<String[]> rarities = new ArrayList<>();

        String[] gui1 = {
                "   bbb   ",
                "   bbb   ",
                "   bbb   "
        };
        rarities.add(gui1);
        String[] guiUncommon = {
                "  buuub  ",
                "  buuub  ",
                "  buuub  "
        };
        rarities.add(guiUncommon);
        String[] guiRare = {
                " burrrub ",
                " burrrub ",
                " burrrub "
        };
        rarities.add(guiRare);
        String[] guiEpic = {
                "bureeerub",
                "bureeerub",
                "bureeerub"
        };
        rarities.add(guiEpic);
        String[] guiLegendary = {
                "urellleru",
                "urellleru",
                "urellleru"
        };
        rarities.add(guiLegendary);
        String[] guiMythic = {
                "relmmmler",
                "relmmmler",
                "relmmmler"
        };
        rarities.add(guiMythic);

        for (int i = 0; i<=rarity; i++){
            InventoryGui gui = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', "&r&8Wishing for " + wishType.toString().toLowerCase().split("_")[0].replace(String.valueOf(wishType.toString().toLowerCase().split("_")[0].toCharArray()[0]), String.valueOf(wishType.toString().toLowerCase().split("_")[0].toCharArray()[0]).toUpperCase())), rarities.get(i));
            gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
            gui.addElement(new DynamicGuiElement('b', (viewer)->{
                return new StaticGuiElement('b', new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('u', (viewer)->{
                return new StaticGuiElement('u', new ItemStack(Material.LIME_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('r', (viewer)->{
                return new StaticGuiElement('r', new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('e', (viewer)->{
                return new StaticGuiElement('e', new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('l', (viewer)->{
                return new StaticGuiElement('l', new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), "");
            }));
            gui.addElement(new DynamicGuiElement('m', (viewer)->{
                return new StaticGuiElement('m', new ItemStack(Material.PINK_STAINED_GLASS_PANE), "");
            }));

            finishedList.add(gui);
        }
        finishedList.addAll(createPulloutAnim(player, wishRarity, wishType, stars, itemClass, finalItem));
        return finishedList;
    }

    //todo add item class
    private ArrayList<InventoryGui> createPulloutAnim(Player player, WishSystem.WishRarity wishRarity, WishSystem.WishType wishType, int stars, ItemClass itemClass, ItemStack finalItem){
        ArrayList<InventoryGui> finishedList = new ArrayList<>();
        String[] gui1 = {
                "         ",
                "c        ",
                "         "
        };
        String[] gui2 = {
                "         ",
                "c aaaaa  ",
                "         "
        };
        ArrayList<String[]> starList = new ArrayList<>();
        String[] guiStars0 = {
                "         ",
                "c saaaa  ",
                "         "
        };
        starList.add(guiStars0);
        String[] guiStars1 = {
                "         ",
                "c ssaaa  ",
                "         "
        };
        starList.add(guiStars1);
        String[] guiStars2 = {
                "         ",
                "c sssaa  ",
                "         "
        };
        starList.add(guiStars2);
        String[] guiStars3 = {
                "         ",
                "c ssssa  ",
                "         "
        };
        starList.add(guiStars3);
        String[] guiStars4 = {
                "         ",
                "c sssss  ",
                "         "
        };
        starList.add(guiStars4);
        String[] guiFinal = {
                "         ",
                "c   i    ",
                "         "
        };
        String s = "&r&" + getRarityColorChar(wishRarity) + "Wishing for " + wishType.toString().toLowerCase().split("_")[0].replace(String.valueOf(wishType.toString().toLowerCase().split("_")[0].toCharArray()[0]), String.valueOf(wishType.toString().toLowerCase().split("_")[0].toCharArray()[0]).toUpperCase());
        InventoryGui gui = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', s), gui1);
        gui.setFiller(new ItemStack(WishSystem.getWishRarityAsGlassPane(wishRarity), 1));
        gui.addElement(new DynamicGuiElement('c', (viewer)->{
            return new StaticGuiElement('c', getClassItem(itemClass), getItemAsString(getClassItem(itemClass)));
        }));
        finishedList.add(gui);

        InventoryGui guiSecond = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', s), gui2);
        guiSecond.setFiller(new ItemStack(WishSystem.getWishRarityAsGlassPane(wishRarity), 1));
        guiSecond.addElement(new DynamicGuiElement('c', (viewer)->{
            return new StaticGuiElement('c', getClassItem(itemClass), getItemAsString(getClassItem(itemClass)));
        }));
        guiSecond.addElement(new DynamicGuiElement('a', (viewer)->{
            return new StaticGuiElement('a', new ItemStack(Material.AIR));
        }));
        finishedList.add(guiSecond);

        for (int i = 0; i<stars; i++){
            InventoryGui g = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', s), starList.get(i));
            g.setFiller(new ItemStack(WishSystem.getWishRarityAsGlassPane(wishRarity), 1));
            g.addElement(new DynamicGuiElement('c', (viewer)->{
                return new StaticGuiElement('c', getClassItem(itemClass), getItemAsString(getClassItem(itemClass)));
            }));
            g.addElement(new DynamicGuiElement('a', (viewer)->{
                return new StaticGuiElement('a', new ItemStack(Material.AIR));
            }));
            g.addElement(new DynamicGuiElement('s', (viewer)->{
                return new StaticGuiElement('s', new ItemStack(Material.NETHER_STAR), ChatColor.translateAlternateColorCodes('&', "&r&6Star"));
            }));
            finishedList.add(g);
        }

        InventoryGui g = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', s), starList.get(stars-1));
        g.setFiller(new ItemStack(WishSystem.getWishRarityAsGlassPane(wishRarity), 1));
        g.addElement(new DynamicGuiElement('c', (viewer)->{
            return new StaticGuiElement('c', getClassItem(itemClass), getItemAsString(getClassItem(itemClass)));
        }));
        g.addElement(new DynamicGuiElement('a', (viewer)->{
            return new StaticGuiElement('a', new ItemStack(Material.AIR));
        }));
        g.addElement(new DynamicGuiElement('s', (viewer)->{
            return new StaticGuiElement('s', new ItemStack(Material.NETHER_STAR), ChatColor.translateAlternateColorCodes('&', "&r&6Star"));
        }));
        finishedList.add(g);

        InventoryGui finalgui = new InventoryGui(this.plugin, player, ChatColor.translateAlternateColorCodes('&', s), guiFinal);
        finalgui.setCloseAction(close -> {
            close.getGui().show(close.getPlayer());
            return false;
        });
        finalgui.setFiller(new ItemStack(WishSystem.getWishRarityAsGlassPane(wishRarity), 1));
        finalgui.addElement(new DynamicGuiElement('c', (viewer)->{
            return new StaticGuiElement('c', getClassItem(itemClass), getItemAsString(getClassItem(itemClass)));
        }));
        finalgui.addElement(new DynamicGuiElement('i', (viewer)->{
            return new StaticGuiElement('i', finalItem, click -> {
                //todo temp open classes gui and add item there add db support
                click.getWhoClicked().getInventory().addItem(finalItem);
                //todo temp end
                click.getGui().close();
                return true;
            }, getItemAsString(finalItem));
        }));
        addClosePrevention(finalgui);

        finishedList.add(finalgui);

        return finishedList;
    }

    private void addClosePrevention(InventoryGui gui){
        gui.setCloseAction(close -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    close.getGui().show(close.getPlayer());
                }
            }.runTaskLater(plugin, 1);
            return false;
        });
    }

    private ItemStack getClassItem(ItemClass itemClass){
        ItemStack item;
        if (itemClass == ItemClass.DPS){
            item = new ItemStack(Material.IRON_SWORD);
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Class: &aDPS"));
            item.setItemMeta(im);
        } else if (itemClass == ItemClass.ARCHER) {
            item = new ItemStack(Material.BOW);
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Class: &aARCHER"));
            item.setItemMeta(im);
        } else if (itemClass == ItemClass.TANK) {
            item = new ItemStack(Material.IRON_CHESTPLATE);
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Class: &aTANK"));
            item.setItemMeta(im);
        } else {
            item = new ItemStack(Material.POTION);
            PotionMeta pm = (PotionMeta) item.getItemMeta();
            pm.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
            pm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Class: &aHEALER"));
            item.setItemMeta(pm);
        }

        return item;
    }

    private String getItemAsString(ItemStack item){
        String finalString = "";
        finalString = item.getItemMeta().getDisplayName();

        if (item.getItemMeta().hasLore()){
            for (String s : item.getItemMeta().getLore()){
                finalString+="\n" + s;
            }
        }

        return finalString;
    }

    private ItemStack getPlayerSkull(Player p){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) skull.getItemMeta();
        sm.setOwningPlayer(p);
        skull.setItemMeta(sm);
        return skull;
    }

    private void startDungeon(Player p, int level){
        //todo temp
        if (partyManager.isPlayerInParty(p) && partyManager.isPartyOwner(p) && !gameManager.getQueueManager().isPlayerInQueue(p)){
            for (Player member : partyManager.getPlayersParty(p)){
                if (isPlayerInGame(member)){
                    p.sendMessage(ChatColor.RED + "No players can be in game!");
                }
            }
            p.sendMessage(ChatColor.GREEN + "Creating Dungeon...");
            new Dungeon(partyManager.getPlayersParty(p), level, Dungeon.getRandomDifficulty(), false);
        } else p.sendMessage(ChatColor.RED + "You need to be in a party and be it's owner to execute this command!");
        //todo end temp
    }

    private ItemStack getPlayerSkull(String base64){
        return SkullCreator.itemFromBase64(base64);
    }
}
