package com.builtbroken.mc.abstraction.framework.entity;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.wrapper.IEntity;
import com.builtbroken.mc.api.wrapper.IExplosion;
import com.builtbroken.mc.api.wrapper.IWorld;
import net.minecraft.world.Explosion;

/**
 * Wrapper for MC's explosion class
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/10/2016.
 */
public class ExplosionWrapper implements IExplosion
{
    final Explosion explosion;

    public ExplosionWrapper(Explosion explosion)
    {
        this.explosion = explosion;
    }

    @Override
    public IEntity getExplosivePlacedBy()
    {
        return AbstractionLayer.getEntity(explosion.getExplosivePlacedBy());
    }

    @Override
    public float getExplosiveSize()
    {
        return explosion.explosionSize;
    }

    @Override
    public IWorld getWorld()
    {
        return getExplosivePlacedBy() != null ? getExplosivePlacedBy().getWorld() : null; //TODO implement accessor, world field is private
    }

    @Override
    public double x()
    {
        return explosion.explosionX;
    }

    @Override
    public double y()
    {
        return explosion.explosionY;
    }

    @Override
    public double z()
    {
        return explosion.explosionZ;
    }

    @Override
    public int hashCode()
    {
        return explosion != null ? explosion.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ExplosionWrapper)
        {
            return ((ExplosionWrapper) obj).explosion == explosion; //TODO check to ensure this always works, as there is a small chance two entities are the same by entityID
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "ExplosionWrapper[" + explosion + "]";
    }
}
