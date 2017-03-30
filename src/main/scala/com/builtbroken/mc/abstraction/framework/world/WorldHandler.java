package com.builtbroken.mc.abstraction.framework.world;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.wrapper.IWorld;
import com.builtbroken.mc.framework.abstraction.IWorldHandler;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/10/2016.
 */
public class WorldHandler implements IWorldHandler
{
    @Override
    public IWorld get(int dimension)
    {
        return AbstractionLayer.getWorld(dimension);
    }
}
