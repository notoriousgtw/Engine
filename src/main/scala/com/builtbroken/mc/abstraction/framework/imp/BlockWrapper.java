package com.builtbroken.mc.abstraction.framework.imp;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.wrapper.IEntity;
import com.builtbroken.mc.api.wrapper.IBlock;
import com.builtbroken.mc.api.wrapper.IWorld;
import net.minecraft.block.Block;

/**
 * Encapsulated block, keep in mind java's JIT will inline most of these calls at runtime. In other words performance is not that big of an issue
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/10/2016.
 */
public class BlockWrapper implements IBlock
{
    public final Block block;

    public BlockWrapper(Block block)
    {
        this.block = block;
    }

    @Override
    public boolean isReplaceable(IWorld world, int xi, int yi, int zi)
    {
        return block.isReplaceable(AbstractionLayer.getWorld(world), xi, yi, zi);
    }

    @Override
    public float getBlockHardness(IWorld world, int xi, int yi, int zi)
    {
        return block.getBlockHardness(AbstractionLayer.getWorld(world), xi, yi, zi);
    }

    @Override
    public float getExplosionResistance(IEntity cause)
    {
        return block.getExplosionResistance(AbstractionLayer.getEntity(cause));
    }

    @Override
    public float getExplosionResistance(IEntity cause, IWorld world, int xi, int yi, int zi, double xx, double yy, double zz)
    {
        return 0;
    }

    @Override
    public boolean isAir(IWorld world, int xi, int yi, int zi)
    {
        return block.isAir(AbstractionLayer.getWorld(world), xi, yi, zi);
    }

    @Override
    public void setBlockBounds(float x, float y, float z, float x1, float y1, float z1)
    {
        block.setBlockBounds(x, y, z, x1, y1, z1);
    }

    @Override
    public String getMaterial()
    {
        return block.getMaterial().toString(); //TODO implement
    }

    @Override
    public boolean isSideSolid(IWorld world, int xi, int yi, int zi, int side)
    {
        return block.isSideSolid(AbstractionLayer.getWorld(world), xi, yi, zi, AbstractionLayer.getSide(side));
    }

    @Override
    public int getBlockID()
    {
        return Block.getIdFromBlock(block);
    }

    @Override
    public int hashCode()
    {
        return block != null ? block.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof BlockWrapper)
        {
            return ((BlockWrapper) obj).block == block;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return block != null ? block.toString() : super.toString();
    }
}
