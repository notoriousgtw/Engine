package com.builtbroken.mc.abstraction.framework.tile;

import com.builtbroken.mc.api.wrapper.IBlock;
import com.builtbroken.mc.api.tile.IBlockProperties;
import com.builtbroken.mc.api.tile.ITile;
import com.builtbroken.mc.api.tile.ITileHost;
import com.builtbroken.mc.api.wrapper.IWorld;
import com.builtbroken.mc.framework.tile.IBlockWrapper;

import java.util.HashMap;

/**
 * Handler for most functionality for Tile Framework System
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2016.
 */
public class TileFramework
{
    /** Map of {@link ITile} to it's block property file, keep in mind properties are shared for meta value tiles */
    protected static final HashMap<Class<? extends ITile>, BlockProperties> classToData = new HashMap();
    /** Map of {@link ITile} to it's ID name, id should be MOD:NAME to prevent conflicts */
    protected static final HashMap<Class<? extends ITile>, String> tileNameToClass = new HashMap();
    /** Map of old TileEntity names to new reference objects, used to load old maps */
    protected static final HashMap<String, Class<? extends ITile>> legacyTEMap = new HashMap();

    /**
     * Gets the block property data object for the tile object
     *
     * @param aClass - class
     * @return property file if one was registered
     */
    public static BlockProperties getBlockDataFor(Class<? extends ITile> aClass)
    {
        if (classToData.containsKey(aClass))
        {
            return classToData.get(aClass);
        }
        return null;
    }

    public static IBlockProperties getBlockDataFor(IWorld world, int x, int y, int z)
    {
        IBlock block = world.getBlock(x, y, z);
        if (block != null && block instanceof IBlockWrapper)
        {
            return ((IBlockWrapper) block).getBlockData(world, x, y, z);
        }
        return null;
    }

    public static ITile getTileFor(ITileHost host)
    {
        IBlockProperties props = getBlockDataFor(host.world(), (int)host.x(), (int)host.y(), (int)host.z());
        if(props != null)
        {
            return props.createNewTile(host.world(), host.world().getBlockMetadata((int)host.x(), (int)host.y(), (int)host.z()));
        }
        return null;
    }
}
