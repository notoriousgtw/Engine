package com.builtbroken.mc.abstraction.framework.tile;

import com.builtbroken.mc.api.tile.ITile;
import com.builtbroken.mc.framework.tile.imp.IBlockTile;
import com.builtbroken.mc.lib.mod.IMod;
import com.builtbroken.mc.lib.render.block.BlockRenderHandler;
import com.builtbroken.mc.lib.transform.region.Cube;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;

/**
 * Holds all data and basic logic for blocks.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public abstract class BlockProperties
{

    /** Mod object that created this tile */
    public final IMod mod;

    /** Block instance created as part of this tile */
    public Block block;

    /** ItemBlock class to register with this tile */
    public Class<? extends ItemBlock> itemBlock = ItemBlock.class;

    //Block vars
    /** Name of the block for this tile */
    public String name;
    /** Creative tab to use for listing this tile */
    public CreativeTabs creativeTab;
    /** Material type for the block to use for varies checks */
    public Material material = Material.clay;
    /** Hardness value for mining speed */
    public float hardness = 1;
    /** Resistance value for explosions */
    public float resistance = 1;
    /** Can this tile emmit redstone */
    public boolean canEmmitRedstone = false;
    /** Is the block solid (true) or can it be seen threw (false) */
    public boolean isOpaque = false;
    /** Sound this tile makes when entities step on it */
    public Block.SoundType stepSound = Block.soundTypeStone;

    /** BLOCK, Should we render a normal block */
    public boolean renderNormalBlock = true;
    /** BLOCK, Render Type used by the block for checking how to render */
    public int renderType = BlockRenderHandler.ID; //renderNormalBlock will force this to zero
    public int renderPass = 0;

    /** Name of the main texture for the block */
    public String textureName;

    /** Bounding box used by the tile */
    public Cube bounds;

    /** Wrapper for block calls that can not be sent to TileEntity */
    public IBlockTile staticTile;
    public int blockColor;

    public BlockProperties(IMod mod)
    {
        this.mod = mod;
    }

    /**
     * Generates a new tile for the block and meta combo
     *
     * @return
     */
    public abstract ITile createNewTile(World world, int meta);

    /**
     * Called to register {@link TileEntityWrapper} class that will use this data object. IF you
     * do not register the class you will need to provide an alt way to get the TileData object.
     * As it is needed in order for several methods in the {@link TileEntityWrapper} class to function.
     */
    public abstract void registerTiles();

    public void registerIcons(IIconRegister iconRegister, boolean blockLayer)
    {

    }
}
