package com.builtbroken.mc.core.network.packet;

import com.builtbroken.mc.lib.transform.vector.Pos;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.IPacketReceiver;

/**
 * Packet type designed to be used with Tiles
 *
 * @author tgame14
 * @since 26/05/14
 */
public class PacketTile extends PacketType
{
    public int x;
    public int y;
    public int z;

    public PacketTile()
    {

    }

    /**
     * @param x    - location
     * @param y    - location
     * @param z    - location
     * @param args -  data to send, first arg should be packetID if
     *             the tile is an instance of {@code IPacketIDReceiver}
     *             Should never be null
     */
    public PacketTile(int x, int y, int z, Object... args)
    {
        super(args);

        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @param tile - TileEntity to send this packet to, only used for location data
     * @param args -  data to send, first arg should be packetID if
     *             the tile is an instance of {@code IPacketIDReceiver}
     *             Should never be null
     */
    public PacketTile(TileEntity tile, Object... args)
    {
        this(tile.xCoord, tile.yCoord, tile.zCoord, args);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeBytes(data());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        data_$eq(buffer.slice());
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        handle(player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        handle(player);
    }

    /**
     * Called to handle a packet when it is received
     *
     * @param player - player who received the packet
     */
    public void handle(EntityPlayer player)
    {
        handle(player, player.getEntityWorld().getTileEntity(this.x, this.y, this.z));
    }

    /**
     * Called to handler a packet when it is received
     *
     * @param player - player who received the packet
     * @param tile   - tile who is receiving the packet
     */
    public void handle(EntityPlayer player, TileEntity tile)
    {
        sender_$eq(player);
        if (tile.isInvalid())
        {
            System.out.println("Packet sent to an invalid TileEntity [" + tile + "] in " + new Pos(x, y, z));
        }
        if (tile instanceof IPacketIDReceiver)
        {
            try
            {
                IPacketIDReceiver receiver = (IPacketIDReceiver) tile;
                ByteBuf buf = data().slice();

                int id;
                try
                {
                    id = buf.readInt();
                } catch (IndexOutOfBoundsException ex)
                {
                    System.out.println("Packet sent to a Tile[" + tile + "] failed to provide a packet ID");
                    System.out.println("Location: " + new Pos(x, y, z));
                    return;
                }
                receiver.read(buf, id, player, this);
            } catch (Exception e)
            {
                System.out.println("Packet sent to a TileEntity failed to be received [" + tile + "] in " + new Pos(x, y, z));
                e.printStackTrace();
            }
        }
        else if (tile instanceof IPacketReceiver)
        {
            try
            {
                IPacketReceiver receiver = (IPacketReceiver) tile;
                receiver.read(data().slice(), player, this);
            } catch (IndexOutOfBoundsException e)
            {
                System.out.println("Packet sent to a TileEntity was read out side its bounds [" + tile + "] in " + new Pos(x, y, z));
            } catch (Exception e)
            {
                System.out.println("Packet sent to a TileEntity failed to be received [" + tile + "] in " + new Pos(x, y, z));
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Packet was sent to a tile not implementing IPacketReceiver, this is a coding error [" + tile + "] in " + new Pos(x, y, z));
        }
    }
}
