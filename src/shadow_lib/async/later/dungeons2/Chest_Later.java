/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shadow_lib.async.later.dungeons2;

import forge_sandbox.com.someguyssoftware.gottschcore.enums.Rarity;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.ICoords;
import forge_sandbox.greymerk.roguelike.worldgen.Coord;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import shadow_lib.async.AsyncWorldEditor;
import shadow_lib.async.later.roguelike.Later;
import zhehe.util.RandomCollection;
import zhehe.util.config.SimpleWorldConfig;
import zhehe.util.config.WorldConfig;

/**
 *
 * @author
 */
public class Chest_Later extends Later {
    private Coord coords;
    private AsyncWorldEditor world;
    private Random random;
    private Rarity rarity;
    private BlockData state;
    
    
    private static class LootNode {
        public int min, max;
        public Material item;
        public LootNode(int min, int max, Material item) {
            this.max = max;
            this.min = min;
            this.item = item;
        }
    }
    private final static Map<Rarity, RandomCollection<LootNode>> LOOT_TABLE;
    static {
        LOOT_TABLE = new HashMap<>();
        {
            RandomCollection<LootNode> list = new RandomCollection();
            list.add(10, new LootNode(1, 1, Material.ENDER_PEARL));
            list.add(3, new LootNode(1, 3, Material.DIAMOND));
            list.add(10, new LootNode(1, 5, Material.IRON_INGOT));
            list.add(10, new LootNode(1, 3, Material.GOLD_INGOT));
            list.add(5, new LootNode(4, 9, Material.REDSTONE));
            list.add(15, new LootNode(1, 3, Material.BREAD));
            list.add(15, new LootNode(1, 3, Material.APPLE));
            list.add(5, new LootNode(1, 1, Material.IRON_PICKAXE));
            list.add(5, new LootNode(1, 1, Material.IRON_SWORD));
            list.add(5, new LootNode(1, 1, Material.IRON_CHESTPLATE));
            list.add(5, new LootNode(1, 1, Material.IRON_LEGGINGS));
            list.add(5, new LootNode(1, 1, Material.IRON_BOOTS));
            list.add(1, new LootNode(1, 1, Material.GOLDEN_APPLE));
            list.add(1, new LootNode(1, 1, Material.SADDLE));
            list.add(1, new LootNode(1, 1, Material.IRON_HORSE_ARMOR));
            list.add(1, new LootNode(1, 1, Material.GOLDEN_HORSE_ARMOR));
            list.add(1, new LootNode(1, 1, Material.DIAMOND_HORSE_ARMOR));
            
            //TODO Improve this
            for(Rarity rarity : Rarity.values()) {
                LOOT_TABLE.put(rarity, list);
            }
        }
        
    }
    
    
    
    private static boolean generate(final Chunk chunk, final Random random, Coord pos,
            final Rarity rarity, BlockData state) {
        
        int x = pos.getX() % 16;
        int y = pos.getY();
        int z = pos.getZ() % 16;
        if(x < 0) x = x + 16;
        if(z < 0) z = z + 16;
        
        Block block = chunk.getBlock(x, y, z);
        block.setBlockData(state);
        
        String world_name = chunk.getWorld().getName();
        boolean builtin= true;
        if(WorldConfig.wc.dict.containsKey(world_name) && !WorldConfig.wc.dict.get(world_name).ant_man_dungeon.builtinLoot) {
            builtin = false;
        }
        if(builtin) {
            BlockState bs = block.getState();
            if(bs instanceof Chest) {
                Chest chest = (Chest) bs;
                Inventory inv = chest.getInventory();
                
                RandomCollection<LootNode> list = LOOT_TABLE.get(rarity);
                
                for(int i = 0; i < 8; i++) {
                    LootNode ln = list.next();
                    ItemStack item = new ItemStack(ln.item);
                    int amount = ln.min + random.nextInt(ln.max - ln.min + 1);
                    if(amount > 0 && amount < 64) {
                        item.setAmount(amount);
                        
                        inv.setItem(random.nextInt(inv.getSize()), item);
                    }
                }
            }
        }
        
        if(WorldConfig.wc.dict.containsKey(world_name)) {
            SimpleWorldConfig swc = WorldConfig.wc.dict.get(world_name);
            BlockState bs = block.getState();
            if(bs instanceof Chest) {
                Chest chest = (Chest) bs;
                Inventory inv = chest.getInventory();
                for(zhehe.util.config.LootNode ln : swc.ant_man_dungeon.loots) {
                    if(random.nextDouble() <= ln.chance) {
                        ItemStack item = ln.getItem();
                        int amount = ln.min + random.nextInt(ln.max - ln.min + 1);
                        item.setAmount(amount);
                        inv.setItem(random.nextInt(inv.getSize()), item);
                    }
                }
            }
        }
        
        
        return true;
    }
    
    public static boolean generate_later(AsyncWorldEditor world, Random random, ICoords coords, Rarity rarity, BlockData state) {
        Chest_Later later = new Chest_Later();
        later.coords = new Coord(coords.getX(), coords.getY(), coords.getZ());
        later.random = random;
        later.world = world;
        later.rarity = rarity;
        later.state = state;
        world.addLater(later);
        
        return true;
    }

    @Override
    public Coord getPos() {
        return this.coords;
    }

    @Override
    public void doSomething() {
    }

    @Override
    public void doSomethingInChunk(Chunk c) {
        Chest_Later.generate(c, random, coords, rarity, state);
    }
}