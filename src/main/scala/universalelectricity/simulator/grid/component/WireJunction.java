package universalelectricity.simulator.grid.component;

import net.minecraftforge.common.util.ForgeDirection;
import universalelectricity.api.core.grid.sim.ISimNode;
import universalelectricity.simulator.grid.SimulatedGrid;

import java.util.HashMap;

/**
 * @author DarkCow
 */
public class WireJunction extends NetworkPart
{
    HashMap<Object, ForgeDirection> connectionMap;
    HashMap<ForgeDirection, Boolean> inputMap;
    ISimNode node = null;

    public WireJunction(SimulatedGrid sim, ISimNode node)
    {
        super(sim);
        this.node = node;
    }

    public void add(Object object, ForgeDirection side)
    {
        connectionMap.put(object, side);
    }

    public void remove(Object object)
    {
        if(connectionMap.containsKey(object))
        {
            ForgeDirection direction = connectionMap.get(object);
            connectionMap.remove(object);
            inputMap.remove(direction);
        }
    }

}
