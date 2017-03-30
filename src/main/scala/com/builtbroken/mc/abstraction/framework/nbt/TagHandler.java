package com.builtbroken.mc.abstraction.framework.nbt;

import com.builtbroken.mc.api.save.ISaveTag;
import com.builtbroken.mc.framework.abstraction.ITagHandler;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/10/2016.
 */
public class TagHandler implements ITagHandler
{
    @Override
    public ISaveTag getNew()
    {
        return new NBT(new NBTTagCompound());
    }
}
