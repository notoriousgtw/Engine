package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.IUpdate;
import com.builtbroken.mc.api.save.ISave;
import com.builtbroken.mc.api.save.ISaveTag;
import com.builtbroken.mc.api.tile.ISided;
import com.builtbroken.mc.api.tile.ITileModuleProvider;
import com.builtbroken.mc.api.tile.node.ITileModule;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Prefab designed to automate all node interaction of the time.
 * Does use some reflection to generate a list of all fields holding nodes
 * This is only used for Java. For Scala classes, use traits instead.
 * <p>
 *
 * @author Darkguardsman
 */
public class TileModuleMachineBase extends TileMachine implements ITileModuleProvider
{
    protected List<ITileModule> modules = new ArrayList();

    @Override
    public void onNeighborChanged(int x, int y, int z)
    {
        super.onNeighborChanged(x, y, z);
        for (ITileModule node : getNodes())
        {
            if (node != null)
            {
                node.onParentChange();
            }
        }
    }

    @Override
    public void onAdded()
    {
        super.onAdded();
        for (ITileModule node : getNodes())
        {
            if (node != null)
            {
                node.onJoinWorld();
            }
        }
    }

    @Override
    public void update(boolean tile, long tick, double delta)
    {
        super.update(tile, tick, delta);
        for (ITileModule node : getNodes())
        {
            if (node instanceof IUpdate)
            {
                ((IUpdate) node).update();
            }
        }
    }

    @Override
    public void invalidate()
    {
        for (ITileModule node : getNodes())
        {
            if (node != null)
            {
                node.onLeaveWorld();
            }
        }
        super.invalidate();
    }

    /**
     * Grabs any node that needs called by save() load() invalidate() update() onJoinWorld() etc
     */
    protected final List<ITileModule> getNodes()
    {
        return modules;
    }

    @Override
    public <N extends ITileModule> N getModule(Class<? extends N> nodeType, ForgeDirection from)
    {
        //TODO think about caching module by side or type to improve CPU time for pipe connections
        for (ITileModule module : getNodes())
        {
            if (!(module instanceof ISided) || ((ISided) module).isValidForSide(from))
            {
                if (nodeType.isAssignableFrom(module.getClass()))
                {
                    return (N) module;
                }
            }
        }
        return null;
    }

    @Override
    public void load(ISaveTag nbt)
    {
        super.load(nbt);
        for (ITileModule node : getNodes())
        {
            readFromNBT(node, nbt);
        }
    }

    @Override
    public ISaveTag save(ISaveTag nbt)
    {
        super.save(nbt);
        for (ITileModule node : getNodes())
        {
            writeToNBT(node, nbt);
        }
        return nbt;
    }

    /**
     * Called to handle reading the module from NBT
     *
     * @param module
     * @param nbt
     */
    protected void readFromNBT(ITileModule module, ISaveTag nbt)
    {
        if (module instanceof ISave)
        {
            ((ISave) module).load(nbt);
        }
    }

    /**
     * Called to handle saving the module from NBT
     *
     * @param module
     * @param nbt
     */
    protected void writeToNBT(ITileModule module, ISaveTag nbt)
    {
        if (module instanceof ISave)
        {
            ((ISave) module).save(nbt);
        }
    }
}
