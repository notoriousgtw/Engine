package com.builtbroken.mc.abstraction.framework.nbt;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.save.ISaveTag;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/9/2016.
 */
public class NBT implements ISaveTag
{
    public final NBTTagCompound nbtTagCompound;

    public NBT(NBTTagCompound tag)
    {
        this.nbtTagCompound = tag;
    }

    @Override
    public double getDouble(String name)
    {
        return nbtTagCompound.getDouble(name);
    }

    @Override
    public ISaveTag setDouble(String x, double x1)
    {
        nbtTagCompound.setDouble(x, x1);
        return this;
    }

    @Override
    public int getInteger(String name)
    {
        return nbtTagCompound.getInteger(name);
    }

    @Override
    public ISaveTag setInteger(String x, int xi)
    {
        nbtTagCompound.setInteger(x, xi);
        return this;
    }

    @Override
    public ISaveTag getCompoundTag(String name)
    {
        nbtTagCompound.getCompoundTag(name);
        return this;
    }

    @Override
    public boolean hasKey(String name)
    {
        return nbtTagCompound.hasKey(name);
    }

    @Override
    public ISaveTag setTag(String name, ISaveTag tag)
    {
        nbtTagCompound.setTag(name, AbstractionLayer.getCompoundTag(tag));
        return this;
    }
}
