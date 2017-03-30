package com.builtbroken.mc.abstraction.framework.imp;

import com.builtbroken.mc.abstraction.framework.tile.BlockTileHost;
import com.builtbroken.mc.api.wrapper.IBlock;
import com.builtbroken.mc.framework.abstraction.IBlockHandler;
import com.builtbroken.mc.framework.tile.BlockProperties;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/10/2016.
 */
public class BlockHandler implements IBlockHandler
{
    private HashMap<String, IBlock> nameCache = new HashMap();
    private HashMap<Block, IBlock> blockCache = new HashMap();

    @Override
    public IBlock getBlock(String registeredName)
    {
        if (nameCache.containsKey(registeredName))
        {
            return nameCache.get(registeredName);
        }
        Block block = (Block) Block.blockRegistry.getObject(registeredName);
        if (block != null)
        {
            BlockWrapper wrapper = new BlockWrapper(block);
            nameCache.put(registeredName, wrapper);
            return wrapper;
        }
        return null;
    }

    /**
     * Gets the wrapper for the block, generates a new
     * wrapper if missing.
     *
     * @param block
     * @return
     */
    public IBlock getWrapper(Block block)
    {
        if (blockCache.containsKey(block))
        {
            return blockCache.get(block);
        }
        BlockWrapper wrapper = new BlockWrapper(block);
        blockCache.put(block, wrapper);
        return wrapper;
    }

    /**
     * Turns a list of property data into actual blocks
     * @param blocks
     */
    public void loadBlocks(List<BlockProperties> blocks)
    {
        for(BlockProperties prop : blocks)
        {
            if(prop != null)
            {
                loadBlock(prop);
            }
        }
    }

    /**
     * Converts a property file into a block
     * @param block
     */
    public void loadBlock(BlockProperties block)
    {
        try
        {
            BlockTileHost host = new BlockTileHost(block);
            block.block = getWrapper(GameRegistry.registerBlock(host, block.name));
            block.onRegistered();
        }
        catch (Exception e)
        {
            throw e;
        }
        //TODO check for recipe loaders
        //TODO check for other interfaces
    }

    public Block getBlock(IBlock block)
    {
        if(block instanceof BlockWrapper)
        {
            return ((BlockWrapper) block).block;
        }
        return Blocks.stone;
    }
}
