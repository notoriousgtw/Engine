package com.builtbroken.mc.abstraction.framework.world;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.entity.IEntitySorter;
import com.builtbroken.mc.api.wrapper.IBlock;
import com.builtbroken.mc.api.wrapper.ITileEntity;
import com.builtbroken.mc.api.wrapper.IBiomeGenBase;
import com.builtbroken.mc.api.wrapper.IChunk;
import com.builtbroken.mc.api.wrapper.IWorld;
import com.builtbroken.mc.framework.transform.shape.Cube;
import com.sun.xml.internal.stream.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

/**
 * Wrapper class for the world object from minecraft
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/9/2016.
 */
public class WorldWrapper implements IWorld
{
    private WeakReference<World> worldWeakReference;
    final int dim;

    public WorldWrapper(World world)
    {
        worldWeakReference = new WeakReference<World>(world);
        dim = world.provider.dimensionId;
    }

    public World world()
    {
        if (worldWeakReference.get() == null)
        {
            World world = DimensionManager.getWorld(dim);
            if (world != null)
            {
                worldWeakReference = new WeakReference<World>(world);
            }
        }
        return worldWeakReference.get();
    }

    @Override
    public IBlock getBlock(int x, int y, int z)
    {
        return AbstractionLayer.getBlockWrapper(world().getBlock(x, y, z));
    }

    @Override
    public int getBlockMetadata(int x, int y, int z)
    {
        return 0;
    }

    @Override
    public boolean setBlockToAir(int x, int y, int z)
    {
        return false;
    }

    @Override
    public void markBlockForUpdate(int x, int y, int z)
    {

    }

    @Override
    public boolean setBlock(int xi, int yi, int zi, IBlock block, int metadata, int notify)
    {
        return false;
    }

    @Override
    public boolean isAirBlock(int xi, int yi, int zi)
    {
        return false;
    }

    @Override
    public boolean isBlockFreezable(int xi, int yi, int zi)
    {
        return false;
    }

    @Override
    public boolean canBlockSeeTheSky(int xi, int yi, int zi)
    {
        return false;
    }

    @Override
    public ITileEntity getTileEntity(int xi, int yi, int zi)
    {
        return null;
    }

    @Override
    public boolean chunkExists(int cx, int cz)
    {
        return false;
    }

    @Override
    public boolean isChunkLoaded(int cx, int cz)
    {
        return false;
    }

    @Override
    public IChunk getChunkFromBlockCoords(int xi, int zi)
    {
        return null;
    }

    @Override
    public int getDimID()
    {
        return 0;
    }

    @Override
    public IBiomeGenBase getBiomeGenForCoords(int xi, int zi)
    {
        return null;
    }

    @Override
    public List<Entity> getEntitiesWithin(Cube cube, IEntitySorter sorter)
    {
        return null;
    }

    @Override
    public IChunk getChunkFromChunkCoords(int chunkX, int chunkZ)
    {
        return null;
    }

    @Override
    public void playSound(IBlock block)
    {
        //TODO play block break sound
    }

    @Override
    public void playSound(IPos3D pos, String sound, float volume, float pitch, boolean b)
    {
        world().playSound(pos.x(), pos.y(), pos.z(), sound, volume, pitch, b);
    }

    @Override
    public void playSound(double x, double y, double z, String sound, float volume, float pitch, boolean b)
    {
        world().playSound(x, y, z, sound, volume, pitch, b);
    }

    @Override
    public void spawnParticle(String t, double x, double y, double z, double vel_x, double vel_y, double vel_z)
    {
        world().spawnParticle(t, x, y, z, vel_x, vel_y, vel_z);
    }

    @Override
    public Random getRandom()
    {
        return world().rand;
    }
}
