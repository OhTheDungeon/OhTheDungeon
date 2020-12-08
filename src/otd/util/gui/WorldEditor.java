/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otd.util.gui;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shadow_manager.DungeonWorldManager;
import zhehe.util.I18n;
import otd.util.config.SimpleWorldConfig;
import otd.util.config.WorldConfig;

/**
 *
 * @author
 */
public class WorldEditor extends Content {
    public final String world;
    public final Environment env;
    private boolean roguelike;
    private boolean doomlike;
    private boolean battletower;
    private boolean smoofy;
    private boolean draylar;
    private boolean ant;
    private boolean aether;
    private boolean lich;
    private boolean egg;
    private final static int SLOT = 27;
    
    private final static Material DISABLE = Material.MUSIC_DISC_BLOCKS;
    private final static Material ENABLE = Material.MUSIC_DISC_CAT;
    
    private WorldEditor() {
        super("", SLOT);
        this.world = null;
        this.env = Environment.NORMAL;
    }
    public static WorldEditor instance = new WorldEditor();
    
    public WorldEditor(String world, Environment env) {
        super(I18n.instance.World_Editor + " : " + world, SLOT);
        this.world = world;
        this.env = env;
    }
    
    @EventHandler
    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof WorldEditor)) {
            return;
        }
        kcancel(e);
        
        int slot = e.getRawSlot();
        if(slot < 0 && slot >= 27) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        WorldEditor holder = (WorldEditor) e.getInventory().getHolder();
        if(holder == null || holder.world == null) return;
        if(slot == 9) {
            RoguelikeConfig rc = new RoguelikeConfig(holder.world, holder);
            rc.openInventory(p);
        }
        if(slot == 10) {
            if(holder.world.equals(DungeonWorldManager.WORLD_NAME)) return;
            DoomlikeConfig dc = new DoomlikeConfig(holder.world, holder);
            dc.openInventory(p);
        }
        if(slot == 11) {
            if(holder.world.equals(DungeonWorldManager.WORLD_NAME)) return;
            BattleTowerConfig btc = new BattleTowerConfig(holder.world, holder);
            btc.openInventory(p);
        }
        if(slot == 12) {
            if(holder.world.equals(DungeonWorldManager.WORLD_NAME)) return;
            SmoofyConfig sc = new SmoofyConfig(holder.world, holder);
            sc.openInventory(p);
        }
        if(slot == 13) {
            if(holder.world.equals(DungeonWorldManager.WORLD_NAME)) return;
            DraylarBattleTowerConfig db = new DraylarBattleTowerConfig(holder.world, holder);
            db.openInventory(p);
        }
        if(slot == 14) {
            if(holder.world.equals(DungeonWorldManager.WORLD_NAME)) return;
            AntManDungeonConfig am = new AntManDungeonConfig(holder.world, holder);
            am.openInventory(p);
        }
        if(slot == 15) {
            if(holder.world.equals(DungeonWorldManager.WORLD_NAME)) return;
            AetherDungeonConfig ad = new AetherDungeonConfig(holder.world, holder);
            ad.openInventory(p);
        }
        if(slot == 16) {
            if(holder.world.equals(DungeonWorldManager.WORLD_NAME)) return;
            LichTowerConfig ad = new LichTowerConfig(holder.world, holder);
            ad.openInventory(p);
        }
        if(slot == 18) {
            DungeonSpawnSetting dss = new DungeonSpawnSetting(holder.world, holder);
            dss.openInventory(p);
        }
        if(slot == 19) {
            WorldSpawnerManager wsm = new WorldSpawnerManager(holder.world, holder);
            wsm.openInventory(p);
        }
//        if(slot == 15) {
//            SimpleWorldConfig swc = WorldConfig.wc.dict.get(holder.world);
//            swc.egg_change_spawner = !swc.egg_change_spawner;
//            WorldConfig.wc.dict.put(holder.world, swc);
//            WorldConfig.save();
//            holder.init();
//        }
    }
    
    @Override
    public void init() {
        inv.clear();
        {
            if(WorldConfig.wc.dict.containsKey(world)) {
                SimpleWorldConfig config = WorldConfig.wc.dict.get(world);
                roguelike = config.roguelike.doNaturalSpawn;
                doomlike = config.doomlike.doNaturalSpawn;
                battletower = config.battletower.doNaturalSpawn;
                smoofy = config.smoofydungeon.doNaturalSpawn;
                draylar = config.draylar_battletower.doNaturalSpawn;
                ant = config.ant_man_dungeon.doNaturalSpawn;
                aether = config.aether_dungeon.doNaturalSpawn;
                lich = config.lich_tower.doNaturalSpawn;
                egg = config.egg_change_spawner;
            } else {
                roguelike = false;
                doomlike = false;
                battletower = false;
                smoofy = false;
                draylar = false;
                ant = false;
                aether = false;
                lich = false;
                egg = true;
            }
        }
        {
            Material material;
            switch (env) {
                case NORMAL:
                    material = WorldManager.NORMAL;
                    break;
                case NETHER:
                    material = WorldManager.NETHER;
                    break;
                default:
                    material = WorldManager.ENDER;
                    break;
            }
            if(world.equals(DungeonWorldManager.WORLD_NAME)) {
                material = WorldManager.MAP;
            }

            ItemStack is = new ItemStack(material);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(world);

            List<String> lores = new ArrayList<>();
            if(world.equals(DungeonWorldManager.WORLD_NAME)) {
                lores.add(I18n.instance.PPDI_WORLD);
            } else {
                if(roguelike) {
                    lores.add(I18n.instance.Roguelike_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
                } else {
                    lores.add(I18n.instance.Roguelike_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
                }
                if(doomlike) {
                    lores.add(I18n.instance.Doomlike_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
                } else {
                    lores.add(I18n.instance.Doomlike_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
                }
                if(battletower) {
                    lores.add(I18n.instance.Battle_Tower + " : " + ChatColor.RED + I18n.instance.Enable);
                } else {
                    lores.add(I18n.instance.Battle_Tower + " : " + ChatColor.GRAY + I18n.instance.Disable);
                }
                if(smoofy) {
                    lores.add(I18n.instance.Smoofy_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
                } else {
                    lores.add(I18n.instance.Smoofy_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
                }
                if(draylar) {
                    lores.add(I18n.instance.Draylar_Battle_Tower + " : " + ChatColor.RED + I18n.instance.Enable);
                } else {
                    lores.add(I18n.instance.Draylar_Battle_Tower + " : " + ChatColor.GRAY + I18n.instance.Disable);
                }
                if(ant) {
                    lores.add(I18n.instance.Ant_Man_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
                } else {
                    lores.add(I18n.instance.Ant_Man_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
                }
                if(aether) {
                    lores.add(I18n.instance.Aether_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
                } else {
                    lores.add(I18n.instance.Aether_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
                }
                if(lich) {
                    lores.add(I18n.instance.LichTower + " : " + ChatColor.RED + I18n.instance.Enable);
                } else {
                    lores.add(I18n.instance.LichTower + " : " + ChatColor.GRAY + I18n.instance.Disable);
                }
            }
            im.setLore(lores);
            is.setItemMeta(im);

            addItem(0, is);
        }
        
        {
            ItemStack is = new ItemStack(Material.DIAMOND_BLOCK);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Roguelike_Dungeon);
            List<String> lores = new ArrayList<>();
            if(roguelike) {
                lores.add(I18n.instance.Roguelike_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
            } else {
                lores.add(I18n.instance.Roguelike_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
            }
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 0, is);
        }
        
        if(world.equals(DungeonWorldManager.WORLD_NAME)) {
            ItemStack is = new ItemStack(Material.BARRIER);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Doomlike_Dungeon);
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.PPDI_WORLD_LORE);
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 1, is);
        } else {
            ItemStack is = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Doomlike_Dungeon);
            List<String> lores = new ArrayList<>();
            if(doomlike) {
                lores.add(I18n.instance.Doomlike_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
            } else {
                lores.add(I18n.instance.Doomlike_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
            }
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 1, is);
        }
        
        if(world.equals(DungeonWorldManager.WORLD_NAME)) {
            ItemStack is = new ItemStack(Material.BARRIER);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Battle_Tower);
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.PPDI_WORLD_LORE);
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 2, is);
        } else {
            ItemStack is = new ItemStack(Material.IRON_BLOCK);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Battle_Tower);
            List<String> lores = new ArrayList<>();
            if(battletower) {
                lores.add(I18n.instance.Battle_Tower + " : " + ChatColor.RED + I18n.instance.Enable);
            } else {
                lores.add(I18n.instance.Battle_Tower + " : " + ChatColor.GRAY + I18n.instance.Disable);
            }
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 2, is);
        }
        
        if(world.equals(DungeonWorldManager.WORLD_NAME)) {
            ItemStack is = new ItemStack(Material.BARRIER);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Smoofy_Dungeon);
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.PPDI_WORLD_LORE);
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 3, is);
        } else {
            ItemStack is = new ItemStack(Material.COBBLESTONE);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Smoofy_Dungeon);
            List<String> lores = new ArrayList<>();
            if(smoofy) {
                lores.add(I18n.instance.Smoofy_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
            } else {
                lores.add(I18n.instance.Smoofy_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
            }
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 3, is);
        }
        
        if(world.equals(DungeonWorldManager.WORLD_NAME)) {
            ItemStack is = new ItemStack(Material.BARRIER);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Draylar_Battle_Tower);
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.PPDI_WORLD_LORE);
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 4, is);
        } else {
            ItemStack is = new ItemStack(Material.BOOKSHELF);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Draylar_Battle_Tower);
            List<String> lores = new ArrayList<>();
            if(draylar) {
                lores.add(I18n.instance.Draylar_Battle_Tower + " : " + ChatColor.RED + I18n.instance.Enable);
            } else {
                lores.add(I18n.instance.Draylar_Battle_Tower + " : " + ChatColor.GRAY + I18n.instance.Disable);
            }
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 4, is);
        }
        
        if(world.equals(DungeonWorldManager.WORLD_NAME)) {
            ItemStack is = new ItemStack(Material.BARRIER);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Ant_Man_Dungeon);
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.PPDI_WORLD_LORE);
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 5, is);
        } else {
            ItemStack is = new ItemStack(Material.END_PORTAL_FRAME);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Ant_Man_Dungeon);
            List<String> lores = new ArrayList<>();
            if(ant) {
                lores.add(I18n.instance.Ant_Man_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
            } else {
                lores.add(I18n.instance.Ant_Man_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
            }
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 5, is);
        }
        
        if(world.equals(DungeonWorldManager.WORLD_NAME)) {
            ItemStack is = new ItemStack(Material.BARRIER);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Aether_Dungeon);
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.PPDI_WORLD_LORE);
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 6, is);
        } else {
            ItemStack is = new ItemStack(Material.GLOWSTONE);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Aether_Dungeon);
            List<String> lores = new ArrayList<>();
            if(aether) {
                lores.add(I18n.instance.Aether_Dungeon + " : " + ChatColor.RED + I18n.instance.Enable);
            } else {
                lores.add(I18n.instance.Aether_Dungeon + " : " + ChatColor.GRAY + I18n.instance.Disable);
            }
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 6, is);
        }
        
        if(world.equals(DungeonWorldManager.WORLD_NAME)) {
            ItemStack is = new ItemStack(Material.BARRIER);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.LichTower);
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.PPDI_WORLD_LORE);
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 7, is);
        } else {
            ItemStack is = new ItemStack(Material.PAINTING);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.LichTower);
            List<String> lores = new ArrayList<>();
            if(lich) {
                lores.add(I18n.instance.LichTower + " : " + ChatColor.RED + I18n.instance.Enable);
            } else {
                lores.add(I18n.instance.LichTower + " : " + ChatColor.GRAY + I18n.instance.Disable);
            }
            im.setLore(lores);
            is.setItemMeta(im);
            
            addItem(1, 7, is);
        }
        
        {
            ItemStack is = new ItemStack(Material.ACTIVATOR_RAIL);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Dungeon_Spawn_Setting);
            is.setItemMeta(im);
            
            addItem(2, 0, is);
        }
        
        {
            ItemStack is = new ItemStack(Material.COMMAND_BLOCK);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.World_Spawner_Manager);
            is.setItemMeta(im);
            
            addItem(2, 1, is);
        }
    }
}