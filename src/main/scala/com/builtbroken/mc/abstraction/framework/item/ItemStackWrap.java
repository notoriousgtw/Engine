package com.builtbroken.mc.abstraction.framework.item;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.item.IItem;
import com.builtbroken.mc.api.save.ISaveTag;
import com.builtbroken.mc.api.wrapper.IItemStack;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/10/2016.
 */
public class ItemStackWrap implements IItemStack
{
    public final ItemStack stack;

    public ItemStackWrap(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public IItem getItem()
    {
        return AbstractionLayer.getItem(stack.getItem());
    }

    @Override
    public ISaveTag writeToNBT(ISaveTag tag)
    {
        stack.writeToNBT(AbstractionLayer.getCompoundTag(tag));
        return tag;
    }

    @Override
    public ISaveTag readFromNBT(ISaveTag tag)
    {
        stack.readFromNBT(AbstractionLayer.getCompoundTag(tag));
        return tag;
    }
}
