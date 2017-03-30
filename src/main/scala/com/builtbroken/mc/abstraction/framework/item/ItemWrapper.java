package com.builtbroken.mc.abstraction.framework.item;

import com.builtbroken.mc.api.item.IItem;
import net.minecraft.item.Item;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/10/2016.
 */
public class ItemWrapper implements IItem
{
    public final Item item;

    public ItemWrapper(Item item)
    {
        this.item = item;
    }
}
