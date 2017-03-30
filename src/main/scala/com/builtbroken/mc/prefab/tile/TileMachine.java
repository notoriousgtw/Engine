package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.save.ISaveTag;
import com.builtbroken.mc.api.tile.IRotation;
import com.builtbroken.mc.api.wrapper.IEntity;
import com.builtbroken.mc.api.wrapper.IItemStack;
import com.builtbroken.mc.framework.transform.Rotation;
import io.netty.buffer.ByteBuf;

/**
 * Prefab for anything that acts as a machine block
 * Created by Dark(DarkGuardsman, Robert) on 1/12/2015.
 */
public class TileMachine extends TileEnt implements IRotation
{
    /** Direction the machine is facing, try to use {@link #getFacing()} and {@link #setFacing(Rotation)} */
    protected Rotation facing = Rotation.UNKNOWN;

    @Override
    public void firstTick()
    {
        super.firstTick();
        if (useMetaForFacing())
        {
            facing = Rotation.getOrientation(getWorld().getBlockMetadata(xi(), yi(), zi()));
        }
    }

    @Override
    public void onPlaced(IEntity entityLiving, IItemStack itemStack)
    {
        super.onPlaced(entityLiving, itemStack);
        setRotationOnPlacement(entityLiving, itemStack);
    }

    /**
     * Called to set the rotation when an entity places the tile
     *
     * @param entityLiving
     * @param itemStack
     */
    protected void setRotationOnPlacement(IEntity entityLiving, IItemStack itemStack)
    {
        //TODO implement advanced placement for improved user controls
        this.setFacing(entityLiving.getFacingDirection());
    }

    @Override
    public Rotation getDirection()
    {
        return getFacing();
    }

    /**
     * Called to see if the tile wants to
     * store the facing direction as a meta
     * value rather than as an NBT value
     *
     * @return true if yes
     */
    protected boolean useMetaForFacing()
    {
        return false;
    }

    @Override
    public ISaveTag save(ISaveTag nbt)
    {
        super.save(nbt);
        if (!useMetaForFacing())
        {
            setFacing(Rotation.getOrientation(nbt.getByte("facing")));
        }
        return nbt;
    }

    @Override
    public void load(ISaveTag nbt)
    {
        super.load(nbt);
        if (!useMetaForFacing() && getFacing() != null)
        {
            nbt.setByte("facing", (byte) getFacing().ordinal());
        }
    }

    @Override
    public void readDescPacket(ByteBuf buf)
    {
        super.readDescPacket(buf);
        if (!useMetaForFacing())
        {
            setFacing(Rotation.getOrientation(buf.readByte()));
        }
    }

    @Override
    public void writeDescPacket(ByteBuf buf)
    {
        super.writeDescPacket(buf);
        if (!useMetaForFacing())
        {
            buf.writeByte(getFacing() == null ? Rotation.NORTH.ordinal() : getFacing().ordinal());
        }
    }

    /**
     * Gets the tile's facing direction
     *
     * @return direction
     */
    public Rotation getFacing()
    {
        if (facing == null)
        {
            if (useMetaForFacing())
            {
                //Facing is used as an internal cache of the meta
                facing = Rotation.getOrientation(getWorld().getBlockMetadata(xi(), yi(), zi()));
            }
            else
            {
                facing = Rotation.NORTH;
            }
        }
        return facing;
    }

    /**
     * Called to set the facing direction
     *
     * @param facing - direction to face
     */
    public void setFacing(Rotation facing)
    {
        this.facing = facing;
        if (useMetaForFacing())
        {
            //Facing is used as an internal cache of the meta
            getWorld().setMeta(xi(), yi(), zi(), facing.ordinal());
        }
    }
}
