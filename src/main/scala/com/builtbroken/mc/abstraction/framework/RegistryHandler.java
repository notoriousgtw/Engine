package com.builtbroken.mc.abstraction.framework;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.abstraction.framework.imp.BlockWrapper;
import com.builtbroken.mc.api.tile.IBlockProperties;
import com.builtbroken.mc.api.wrapper.IBlock;
import com.builtbroken.mc.api.wrapper.IItemStack;
import com.builtbroken.mc.framework.abstraction.IRegistryHandler;
import com.builtbroken.mc.framework.tile.BlockProperties;
import com.builtbroken.mc.lib.mod.ModCreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2016.
 */
public class RegistryHandler implements IRegistryHandler
{
    private HashMap<String, ModCreativeTab> tabs = new HashMap();

    @Override
    public void newCreativeTab(String name)
    {
        if(!tabs.containsKey(name.toLowerCase()))
        {
            tabs.put(name.toLowerCase(), new ModCreativeTab(name));
        }
    }

    @Override
    public void setCreativeTabIcon(String name, IBlockProperties properties)
    {
        if(tabs.containsKey(name.toLowerCase()) && properties instanceof BlockProperties && ((BlockProperties) properties).block instanceof BlockWrapper)
        {
            tabs.get(name.toLowerCase()).itemStack = new ItemStack(((BlockWrapper)((BlockProperties) properties).block).block);
        }
    }

    @Override
    public void addOreValue(String oreName, IItemStack stack)
    {
        OreDictionary.registerOre(oreName, AbstractionLayer.getItemStack(stack));
    }

    @Override
    public void addOreValue(String oreName, IBlock block, int meta)
    {
        OreDictionary.registerOre(oreName, new ItemStack(AbstractionLayer.blockHandler.getBlock(block), 1, meta));
    }

    @Override
    public void newOreGen(String oreName, IItemStack stack, int minY, int maxY, int amountPerChunk, int amountPerBranch)
    {
        //TODO implement
    }

    public CreativeTabs getCreativeTab(String name)
    {
        return tabs.containsKey(name.toLowerCase()) ? tabs.get(name.toLowerCase()) : CreativeTabs.tabAllSearch;
    }
}
