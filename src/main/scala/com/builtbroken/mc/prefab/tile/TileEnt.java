package com.builtbroken.mc.prefab.tile;

import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.framework.network.INetPacket;
import com.builtbroken.mc.framework.network.ITileDesc;
import com.builtbroken.mc.lib.transform.vector.Location;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by robert on 1/12/2015.
 */
public class TileEnt extends com.builtbroken.mc.framework.tile.Tile implements ITileDesc
{
    protected static int DESCRIPTION_PACKET_ID = -1;

    /**
     * Reads packet data
     * <p>
     * Will default description packets to {@link #readDescPacket(ByteBuf)} on the client side
     * using ID {@link #DESCRIPTION_PACKET_ID}
     *
     * @param buf
     * @param id
     * @param player
     * @param type
     * @return true if the packet was read
     */
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if (id == DESCRIPTION_PACKET_ID)
        {
            if (isClient())
            {
                readDescPacket(buf);
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if the packet is valid
     * <p>
     * Defaults to checking distance
     *
     * @param player
     * @param receiveLocation
     * @param packet
     * @return
     */
    public boolean shouldReadPacket(EntityPlayer player, IWorldPosition receiveLocation, PacketType packet)
    {
        return isClient() || new Location(player).distance(receiveLocation.x(), receiveLocation.y(), receiveLocation.z()) < 10 && packet instanceof PacketTile;
    }

    /**
     * Called client side only to read
     * the data from a description packet
     *
     * @param buf - what to read data from
     */
    public void readDescPacket(ByteBuf buf)
    {

    }

    /**
     * Called server side to write the
     * data for a description packet
     *
     * @param buf - what to write data to
     */
    public void writeDescPacket(ByteBuf buf)
    {

    }

    @Override
    public INetPacket getDescPacket()
    {
        try
        {
            PacketTile packet = new PacketTile(this, DESCRIPTION_PACKET_ID);
            writeDescPacket(packet.data());
            return packet;
        }
        catch (Exception e)
        {
            Engine.instance.logger().error("Failed to write description packet for " + this + "  ", e);
        }
        return null;
    }
}
