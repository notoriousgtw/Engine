package com.builtbroken.mc.abstraction.framework.entity;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.wrapper.IEntity;
import com.builtbroken.mc.api.wrapper.IWorld;
import net.minecraft.entity.Entity;

/**
 * Wrapper for MC's entity class
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/9/2016.
 */
public class EntityWrapper<E extends Entity> implements IEntity
{
    public final E entity;

    public EntityWrapper(E entity)
    {
        if(entity == null)
        {
            throw new IllegalArgumentException("EntityWrapper: Entity can not be null");
        }
        this.entity = entity;
    }

    @Override
    public float getRotationYaw()
    {
        return entity.rotationYaw;
    }

    @Override
    public float getPrevRotationYaw()
    {
        return entity.prevRotationYaw;
    }

    @Override
    public float getRotationPitch()
    {
        return entity.rotationPitch;
    }

    @Override
    public float getPrevRotationPitch()
    {
        return entity.prevRotationPitch;
    }

    @Override
    public IWorld getWorld()
    {
        return AbstractionLayer.getWorld(entity.worldObj);
    }

    @Override
    public double x()
    {
        return entity.posX;
    }

    @Override
    public double y()
    {
        return entity.posZ;
    }

    @Override
    public double z()
    {
        return entity.posZ;
    }

    @Override
    public int hashCode()
    {
        return entity != null ? entity.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof EntityWrapper)
        {
            return ((EntityWrapper) obj).entity == entity; //TODO check to ensure this always works, as there is a small chance two entities are the same by entityID
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "EntityWrapper[" + entity + "]";
    }
}
