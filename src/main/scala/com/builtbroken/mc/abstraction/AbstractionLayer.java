package com.builtbroken.mc.abstraction;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.abstraction.framework.RegistryHandler;
import com.builtbroken.mc.abstraction.framework.entity.EntityHandler;
import com.builtbroken.mc.abstraction.framework.entity.EntityWrapper;
import com.builtbroken.mc.abstraction.framework.entity.ExplosionWrapper;
import com.builtbroken.mc.abstraction.framework.entity.RayTraceHandler;
import com.builtbroken.mc.abstraction.framework.imp.BlockHandler;
import com.builtbroken.mc.abstraction.framework.item.ItemHandler;
import com.builtbroken.mc.abstraction.framework.item.ItemStackWrap;
import com.builtbroken.mc.abstraction.framework.nbt.NBT;
import com.builtbroken.mc.abstraction.framework.nbt.TagHandler;
import com.builtbroken.mc.abstraction.framework.world.Player;
import com.builtbroken.mc.abstraction.framework.world.PlayerHandler;
import com.builtbroken.mc.abstraction.framework.world.WorldHandler;
import com.builtbroken.mc.abstraction.framework.world.WorldWrapper;
import com.builtbroken.mc.api.item.IItem;
import com.builtbroken.mc.api.save.ISaveTag;
import com.builtbroken.mc.api.wrapper.*;
import com.builtbroken.mc.framework.ModLayer;
import com.builtbroken.mc.framework.entity.RayHit;
import com.builtbroken.mc.framework.transform.vector.Pos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.UUID;

/**
 * Main class for the project
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/9/2016.
 */
public class AbstractionLayer
{
    private static final HashMap<String, Player> playersByName = new HashMap();
    private static final HashMap<UUID, Player> playersByID = new HashMap();

    private static final HashMap<Integer, WorldWrapper> worldByID = new HashMap();

    public static WorldHandler worldHandler;
    public static BlockHandler blockHandler;
    public static ItemHandler itemHandler;
    public static EntityHandler entityHandler;
    public static RayTraceHandler rayTraceHandler;
    public static TagHandler tagHandler;
    public static PlayerHandler playerHandler;
    public static RegistryHandler registryHandler;

    public static void load()
    {
        ModLayer.worldHandler = worldHandler = new WorldHandler();
        ModLayer.blockHandler = blockHandler = new BlockHandler();
        ModLayer.itemHandler = itemHandler = new ItemHandler();
        ModLayer.entityHandler = entityHandler = new EntityHandler();
        ModLayer.rayTraceHandler = rayTraceHandler = new RayTraceHandler();
        ModLayer.tagHandler = tagHandler = new TagHandler();
        ModLayer.playerHandler = playerHandler = new PlayerHandler();
        ModLayer.registryHandler = registryHandler = new RegistryHandler();
    }

    public static IPlayer getPlayer(EntityPlayer player)
    {
        return getPlayer(player.getGameProfile().getId());
    }

    public static Player getPlayer(String username)
    {
        if (playersByName.containsKey(username))
        {
            return playersByName.get(username);
        }
        return null;
    }

    public static Player getPlayer(UUID id)
    {
        if (playersByID.containsKey(id))
        {
            return playersByID.get(id);
        }
        return null;
    }

    public static WorldWrapper getWorld(int id)
    {
        if (worldByID.containsKey(id))
        {
            return worldByID.get(id);
        }

        return null;
    }

    public static World getWorld(IWorld world)
    {
        if (world instanceof WorldWrapper)
        {
            return ((WorldWrapper) world).world();
        }
        return getMCWorld(world.getDimID());
    }

    public static World getMCWorld(int dim)
    {
        return DimensionManager.getWorld(dim);
    }

    public static WorldWrapper getWorld(World world)
    {
        WorldWrapper worldWrapper = getWorld(world.provider.dimensionId);
        if (worldWrapper == null)
        {
            worldWrapper = new WorldWrapper(world);
            worldByID.put(world.provider.dimensionId, worldWrapper);
        }
        return worldWrapper;
    }

    public static Material getMaterial(String material)
    {
        //TODO implement
        return Material.rock;
    }

    public static Entity getEntity(IEntity entity)
    {
        if (entity instanceof EntityWrapper)
        {
            return ((EntityWrapper) entity).entity;
        }
        return null;
    }

    public static IEntity getEntity(Entity entity)
    {
        return new EntityWrapper(entity); //TODO cache if causes cpu issues
    }

    public static IBlock getBlockWrapper(Block block)
    {
        return blockHandler.getWrapper(block);
    }

    public static IWorld getWorld(IBlockAccess access)
    {
        if (access instanceof World)
        {
            return getWorld((World) access);
        }
        return null; //TODO implement
    }

    public static ItemStack getItemStack(IItemStack stack)
    {
        if (stack instanceof ItemStackWrap)
        {
            return ((ItemStackWrap) stack).stack;
        }
        return null; //TODO implement, or throw error
    }

    public static IExplosion getExplosion(Explosion ex)
    {
        return new ExplosionWrapper(ex);
    }

    public static NBTTagCompound getCompoundTag(ISaveTag tag)
    {
        if (tag instanceof NBT)
        {
            return ((NBT) tag).nbtTagCompound;
        }
        return null; //TODO implement, or throw error
    }

    public static ISaveTag getCompoundTag(NBTTagCompound tag)
    {
        if (tag instanceof ISaveTag)
        {
            return (ISaveTag) tag;
        }
        return new NBT(tag);
    }

    public static IItem getItem(Item item)
    {
        return itemHandler.getItem(item);
    }

    public static Item getItem(IItem item)
    {
        return itemHandler.getItem(item);
    }

    /**
     * Converts a {@link MovingObjectPosition} into a {@link RayHit}
     *
     * @param world - world wrapper object
     * @param hit   - moving object hit, doesn't check for null
     * @return ray hit
     */
    public static RayHit getRayHit(IWorld world, MovingObjectPosition hit)
    {
        if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            return new RayHit(world, hit.blockX, hit.blockY, hit.blockZ, new Pos(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord), hit.sideHit);
        }
        else if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
        {
            return new RayHit(world, new Pos(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord), AbstractionLayer.getEntity(hit.entityHit));
        }
        return new RayHit(world, 0, 0, 0);
    }

    /**
     * Helper method for getting distance between two points
     *
     * @param start
     * @param pos
     * @return
     */
    public static double distance(IPos3D start, IPos3D pos)
    {
        double x = start.x() - pos.x();
        double y = start.y() - pos.y();
        double z = start.z() - pos.z();
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Converts a {@link IPos3D} into {@link Vec3}
     *
     * @param pos - pos
     * @return vec
     */
    public static Vec3 toVec(IPos3D pos)
    {
        return Vec3.createVectorHelper(pos.x(), pos.y(), pos.z());
    }

    public static ForgeDirection getSide(int side)
    {
        return ForgeDirection.getOrientation(side);
    }
}
