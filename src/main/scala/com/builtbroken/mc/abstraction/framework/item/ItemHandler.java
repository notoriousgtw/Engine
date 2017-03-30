package com.builtbroken.mc.abstraction.framework.item;

import com.builtbroken.mc.api.item.IItem;
import com.builtbroken.mc.api.wrapper.IBlock;
import com.builtbroken.mc.api.wrapper.IItemStack;
import com.builtbroken.mc.framework.abstraction.IItemHandler;
import net.minecraft.item.Item;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/10/2016.
 */
public class ItemHandler implements IItemHandler
{
    private final HashMap<Item, IItem> itemCache = new HashMap();

    public IItem getItem(Item item)
    {
        if (itemCache.containsKey(item))
        {
            return itemCache.get(item);
        }
        ItemWrapper wrapper = new ItemWrapper(item);
        itemCache.put(item, wrapper);
        return wrapper;
    }

    public Item getItem(IItem item)
    {
        if(item instanceof ItemWrapper)
        {
            return ((ItemWrapper) item).item;
        }
        return null; //TODO impalement or throw error
    }

    @Override
    public IItemStack newStack(IBlock block, int stackSize, int damage)
    {
        return null;
    }

    @Override
    public IItemStack newStack(IItem block, int stackSize, int damage)
    {
        return null;
    }
}
