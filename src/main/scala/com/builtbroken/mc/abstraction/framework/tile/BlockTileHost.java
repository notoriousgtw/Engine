package com.builtbroken.mc.abstraction.framework.tile;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.tile.IBlockProperties;
import com.builtbroken.mc.api.tile.IBlockTile;
import com.builtbroken.mc.api.tile.ITile;
import com.builtbroken.mc.api.tile.ITileHost;
import com.builtbroken.mc.api.tile.redirects.*;
import com.builtbroken.mc.api.wrapper.IItemStack;
import com.builtbroken.mc.api.wrapper.IWorld;
import com.builtbroken.mc.framework.tile.IBlockWrapper;
import com.builtbroken.mc.framework.transform.shape.Cube;
import com.builtbroken.mc.framework.transform.vector.Point;
import com.builtbroken.mc.framework.transform.vector.Pos;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Wrapper class for sending Block method calls to {@link BlockProperties} object and  {@link TileEntityWrapper} object.
 *
 * @author Dark
 */
public class BlockTileHost extends BlockContainer implements IBlockWrapper
{
    /**
     * Property file for most data about the block
     */
    protected final IBlockProperties properties;

    public BlockTileHost(IBlockProperties data)
    {
        super(AbstractionLayer.getMaterial(data.getMaterial()));
        this.properties = data;
        if (properties.getMod() == null)
        {
            throw new IllegalArgumentException("Block properties must be generated with a mod");
        }

        //Load all data from tile data object
        if (properties.getBounds() != null)
        {
            this.setBlockBounds((float) this.properties.getBounds().min().x(), (float) this.properties.getBounds().min().y(), (float) this.properties.getBounds().min().z(), (float) this.properties.getBounds().max().x(), (float) this.properties.getBounds().max().y(), (float) this.properties.getBounds().max().z());
        }

        this.opaque = isOpaqueCube();
        setBlockName(properties.getMod().getPrefix() + properties.getBlockName());
        setCreativeTab(properties.getCreativeTab());
        setLightOpacity(isOpaqueCube() ? 255 : 0);
        setHardness(properties.getHardness());
        setResistance(properties.getResistance());
        setStepSound(properties.getStepSound());
    }

    public void setCreativeTab(String name)
    {
        setCreativeTab(AbstractionLayer.registryHandler.getCreativeTab(name));
    }

    public void setStepSound(String name)
    {
        //TODO implement
    }


    @Override
    public final IBlockProperties getBlockData(IWorld world, int x, int y, int z)
    {
        return properties;
    }

    @Override
    public final TileEntity createTileEntity(World world, int meta)
    {
        return new TileEntityWrapper(properties.createNewTile(AbstractionLayer.getWorld(world), meta));
    }


    @Override
    public final TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityWrapper(properties.createNewTile(AbstractionLayer.getWorld(world), meta));
    }

    @Override
    public void fillWithRain(World world, int x, int y, int z)
    {
        ITile tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        if (tile instanceof IRainFallible)
        {
            ((IRainFallible) tile).onFillRain();
        }
        eject(tile);
    }

    @Override
    public float getExplosionResistance(Entity entity)
    {
        return properties.getStaticTile() instanceof IBlockTile ? ((IBlockTile) properties.getStaticTile()).getExplosionResistance(AbstractionLayer.getEntity(entity)) : properties.getResistance();
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof IExplosiveResistance)
        {
            inject(tile, world, x, y, z);
            float resistance = ((IExplosiveResistance) tile).getExplosionResistance(AbstractionLayer.getEntity(entity), new Pos(explosionX, explosionY, explosionZ));
            eject(tile);
            return resistance;
        }
        return properties.getResistance();
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof IPlayerLeftClick)
        {
            inject(tile, world, x, y, z);
            ((IPlayerLeftClick) tile).onPlayerLeftClick(AbstractionLayer.getPlayer(player));
            eject(tile);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        ITile tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        tile.onAdded();
        eject(tile);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        ITile tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        //tile.onPlaced(entityLiving, itemStack); TODO re-add
        eject(tile);
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata)
    {
        ITile tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        //tile.onPostPlaced(metadata); TODO re-add
        eject(tile);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof IDestroyedByExplosion)
        {
            inject(tile, world, x, y, z);
            ((IDestroyedByExplosion) tile).onDestroyedByExplosion(AbstractionLayer.getExplosion(ex));
            eject(tile);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6)
    {
        ITile tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        tile.onRemove();
        eject(tile);
        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        ITile tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        boolean b = tile.removeByPlayer(AbstractionLayer.getPlayer(player), willHarvest);
        eject(tile);
        return b;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        return properties.getStaticTile() instanceof IBlockTile ? ((IBlockTile) properties.getStaticTile()).quantityDropped(meta, fortune) : 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof INeighborBlock)
        {
            inject(tile, world, x, y, z);
            ((INeighborBlock) tile).onNeighborChanged(AbstractionLayer.getBlockWrapper(block));
            eject(tile);
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof IPlaceable)
        {
            inject(tile, world, x, y, z);
            boolean b = ((IPlaceable) tile).canPlaceBlockOnSide(side);
            eject(tile);
            return b;
        }
        return super.canPlaceBlockOnSide(world, x, y, z, side);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof IPlaceable)
        {
            inject(tile, world, x, y, z);
            boolean b = ((IPlaceable) tile).canPlaceBlockAt();
            eject(tile);
            return b;
        }
        return super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof INeighborBlock)
        {
            inject(tile, world, x, y, z);
            ((INeighborBlock) tile).onNeighborChanged(new Pos(tileX, tileY, tileZ));
            eject(tile);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof IPlayerActivate)
        {
            inject(tile, world, x, y, z);
            boolean value = ((IPlayerActivate) tile).onPlayerActivated(AbstractionLayer.getPlayer(player), side, new Pos(hitX, hitY, hitZ));
            eject(tile);
            return value;
        }
        return false;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random par5Random)
    {
        ITile tile = getTile(world, x, y, z);
        inject(tile, world, x, y, z);
        tile.update(false, world.provider.getWorldTime(), 0);
        eject(tile);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random par5Random)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof IRandomTick)
        {
            inject(tile, world, x, y, z);
            ((IRandomTick) tile).randomDisplayTick();
            eject(tile);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof ITileCollide)
        {
            inject(tile, world, x, y, z);
            ((ITileCollide) tile).onCollide(AbstractionLayer.getEntity(entity));
            eject(tile);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof ITileCollide)
        {
            Cube main = new Cube(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ).subtract(new Pos(x, y, z));
            inject(tile, world, x, y, z);
            Iterable<Cube> bounds = ((ITileCollide) tile).getCollisionBoxes(main, AbstractionLayer.getEntity(entity));
            eject(tile);
            if (bounds != null)
            {
                for (Cube cuboid : bounds)
                {
                    AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(cuboid.min().x(), cuboid.min().y(), cuboid.min().z(), cuboid.max().x(), cuboid.max().y(), cuboid.max().z()).offset(x, y, z);
                    if (aabb.intersectsWith(bb))
                    {
                        list.add(bb);
                    }
                }
            }
        }
        else
        {
            super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof ITileCollide)
        {
            inject(tile, world, x, y, z);
            Cube cuboid = ((ITileCollide) tile).getSelectBounds();
            AxisAlignedBB value = AxisAlignedBB.getBoundingBox(cuboid.min().x(), cuboid.min().y(), cuboid.min().z(), cuboid.max().x(), cuboid.max().y(), cuboid.max().z()).offset(tile.xi(), tile.yi(), tile.zi());
            eject(tile);
            return value;
        }
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof ITileCollide)
        {
            inject(tile, world, x, y, z);
            Cube cuboid = ((ITileCollide) tile).getSelectBounds();
            AxisAlignedBB value = AxisAlignedBB.getBoundingBox(cuboid.min().x(), cuboid.min().y(), cuboid.min().z(), cuboid.max().x(), cuboid.max().y(), cuboid.max().z()).offset(tile.xi(), tile.yi(), tile.zi());
            eject(tile);
            return value;
        }
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        ITile tile = getTile(access, x, y, z);
        if (tile instanceof IBlockRender)
        {
            inject(tile, access, x, y, z);
            boolean value = ((IBlockRender) properties).shouldSideBeRendered(side);
            eject(tile);
            return value;
        }
        return super.shouldSideBeRendered(access, x, y, z, side);
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int side)
    {
        ITile tile = getTile(access, x, y, z);
        inject(tile, access, x, y, z);
        boolean value = getTile(access, x, y, z).isSolid(side);
        eject(tile);
        return value;
    }

    @Override
    public int getLightValue(IBlockAccess access, int x, int y, int z)
    {
        int value = 0;
        if (access != null)
        {
            ITile tile = getTile(access, x, y, z);
            inject(tile, access, x, y, z);
            value = getTile(access, x, y, z).getLightValue();
            eject(tile);
        }
        return value;
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        //TODO, add support for comparators
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return properties == null ? true : properties.getIsOpaque();
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return properties.shouldRenderAsNormalBlock();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType()
    {
        return properties.shouldRenderAsNormalBlock() ? 0 : properties.getRenderType();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side)
    {
        ITile tile = getTile(access, x, y, z);
        //TODO load JSON and get icons that way
        return Blocks.wool.getIcon(side, side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        //TODO load JSON and get icons that way
        return Blocks.wool.getIcon(side, side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        //TODO load JSON and get icons that way
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        ITile tile = getTile(access, x, y, z);
        if (tile instanceof IBlockRender)
        {
            inject(tile, access, x, y, z);
            int value = ((IBlockRender) getTile(access, x, y, z)).getColorMultiplier();
            eject(tile);
            return value;
        }
        return super.colorMultiplier(access, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor()
    {
        return properties.getBlockColor();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof ITileItem)
        {
            inject(tile, world, x, y, z);

            IItemStack value = ((ITileItem) tile).getPickBlock(AbstractionLayer.getRayHit(AbstractionLayer.getWorld(world), target));
            eject(tile);
            return AbstractionLayer.getItemStack(value);
        }
        return super.getPickBlock(target, world, x, y, z, player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ITile tile = getTile(world, x, y, z);
        if (tile instanceof ITileItem)
        {
            inject(tile, world, x, y, z);
            ArrayList<IItemStack> value = ((ITileItem) tile).getDrops(metadata, fortune);
            ArrayList<ItemStack> converted = new ArrayList();
            for (IItemStack stack : value)
            {
                ItemStack s = AbstractionLayer.getItemStack(stack);
                if (s != null)
                {
                    converted.add(s);
                }
            }
            eject(tile);
            return value != null ? converted : new ArrayList<ItemStack>();
        }
        return super.getDrops(world, x, y, z, metadata, fortune);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        if (properties.getStaticTile() instanceof IBlockTile)
        {
            ArrayList<IItemStack> value = new ArrayList();
            properties.getStaticTile().getSubBlocks(value);
            for (IItemStack stack : value)
            {
                ItemStack s = AbstractionLayer.getItemStack(stack);
                if (s != null)
                {
                    list.add(s);
                }
            }
        }

        if (list.isEmpty())
        {
            list.add(new ItemStack(item));
        }
    }

    /**
     * Redstone interaction
     */
    @Override
    public boolean canProvidePower()
    {
        return properties.canEmmitRedstone();
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side)
    {
        ITile tile = getTile(access, x, y, z);
        if (tile instanceof IRedstone)
        {
            inject(tile, access, x, y, z);
            int value = ((IRedstone) getTile(access, x, y, z)).getWeakRedstonePower(side);
            eject(tile);
            return value;
        }
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int side)
    {
        ITile tile = getTile(access, x, y, z);
        if (tile instanceof IRedstone)
        {
            inject(tile, access, x, y, z);
            int value = ((IRedstone) getTile(access, x, y, z)).getStrongRedstonePower(side);
            eject(tile);
            return value;
        }
        return 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z)
    {
        ITile tile = getTile(access, x, y, z);
        if (tile instanceof ITileBlockBounds)
        {
            inject(tile, access, x, y, z);
            ((ITileBlockBounds) getTile(access, x, y, z)).setBlockBoundsBasedOnState();
            eject(tile);
        }
    }

    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemStack)
    {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            InventoryUtility.dropItemStack(world, new Pos(x, y, z), itemStack);
        }
    }

    @Override
    public int getRenderBlockPass()
    {
        return properties.getRenderPass();
    }

    @Override
    public int tickRate(World world)
    {
        if (properties.getStaticTile() instanceof IBlockTile)
        {
            ((IBlockTile) properties.getStaticTile()).setWorldAccess(AbstractionLayer.getWorld(world));
            int t = ((IBlockTile) properties.getStaticTile()).tickRate();
            ((IBlockTile) properties.getStaticTile()).setWorldAccess(null);
        }
        return 20;

    }

    public static Point getClickedFace(Byte hitSide, float hitX, float hitY, float hitZ)
    {
        switch (hitSide)
        {
            case 0:
                return new Point(1 - hitX, hitZ);
            case 1:
                return new Point(hitX, hitZ);
            case 2:
                return new Point(1 - hitX, 1 - hitY);
            case 3:
                return new Point(hitX, 1 - hitY);
            case 4:
                return new Point(hitZ, 1 - hitY);
            case 5:
                return new Point(1 - hitZ, 1 - hitY);
            default:
                return new Point(0.5, 0.5);
        }
    }


    /**
     * Injects and eject(tile);s data from the TileEntity.
     */
    public void inject(ITile tile, IBlockAccess access, int x, int y, int z)
    {
        if (tile == properties.getStaticTile())
        {
            properties.getStaticTile().injectLocation(AbstractionLayer.getWorld(access), 0, 0, 0);
        }
    }

    public void eject(ITile tile)
    {
        if (tile == properties.getStaticTile())
        {
            properties.getStaticTile().injectLocation(null, 0, 0, 0);
        }
    }

    public ITile getTile(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof ITileHost)
        {
            return ((ITileHost) tile).getTile();
        }
        return properties.getStaticTile();
    }
}
