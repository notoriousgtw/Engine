package com.builtbroken.mc.abstraction.framework.world;

import com.builtbroken.mc.abstraction.framework.entity.EntityWrapper;
import com.builtbroken.mc.api.wrapper.IPlayer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Wrapper class for the player/user
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/9/2016.
 */
public class Player extends EntityWrapper<EntityPlayer> implements IPlayer
{
    protected Player(EntityPlayer player)
    {
        super(player);
    }

    protected EntityPlayer getPlayer()
    {
        return entity;
    }

    @Override
    public String toString()
    {
        return "PlayerWrapper[" + getPlayer() + "]";
    }
}
