package com.builtbroken.mc.abstraction.framework.entity;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.wrapper.IWorld;
import com.builtbroken.mc.framework.abstraction.IRayTraceHandler;
import com.builtbroken.mc.framework.entity.RayHit;
import com.builtbroken.mc.framework.transform.shape.Cube;
import com.builtbroken.mc.framework.transform.vector.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/10/2016.
 */
public class RayTraceHandler implements IRayTraceHandler
{
    @Override
    public RayHit rayTraceEntities(IWorld world, IPos3D start, IPos3D end)
    {
        MovingObjectPosition closestEntityMOP = null;
        double closetDistance = 0D;

        double checkDistance = AbstractionLayer.distance(start, end);
        Cube scanRegion = new Cube(-checkDistance, -checkDistance, -checkDistance, checkDistance, checkDistance, checkDistance).add(start.x(), start.y(), start.z());

        List checkEntities = world.getEntitiesWithin(scanRegion, null);

        for (Object obj : checkEntities)
        {
            Entity entity = (Entity) obj;
            if (entity != null && entity.canBeCollidedWith() && entity.boundingBox != null)
            {
                float border = entity.getCollisionBorderSize();
                AxisAlignedBB bounds = entity.boundingBox.expand(border, border, border);
                MovingObjectPosition hit = bounds.calculateIntercept(AbstractionLayer.toVec(start), Vec3.createVectorHelper(end.x(), end.y(), end.z()));

                if (hit != null)
                {

                    if (bounds.isVecInside(AbstractionLayer.toVec(start)))
                    {
                        if (0 < closetDistance || closetDistance == 0)
                        {
                            closestEntityMOP = new MovingObjectPosition(entity);

                            closestEntityMOP.hitVec = hit.hitVec;
                            closetDistance = 0;
                        }
                    }
                    else
                    {
                        double dist = AbstractionLayer.distance(start, new Pos(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord));

                        if (dist < closetDistance || closetDistance == 0)
                        {
                            closestEntityMOP = new MovingObjectPosition(entity);
                            closestEntityMOP.hitVec = hit.hitVec;

                            closetDistance = dist;
                        }
                    }
                }
            }
        }


        return closestEntityMOP != null ? AbstractionLayer.getRayHit(world, closestEntityMOP) : null;
    }

    @Override
    public RayHit rayTraceBlocks(IWorld world, IPos3D pos, IPos3D end)
    {
        MovingObjectPosition hit = AbstractionLayer.getWorld(world).rayTraceBlocks(AbstractionLayer.toVec(pos), AbstractionLayer.toVec(end));
        if (hit != null)
        {
            return AbstractionLayer.getRayHit(world, hit);
        }
        return null;
    }
}
