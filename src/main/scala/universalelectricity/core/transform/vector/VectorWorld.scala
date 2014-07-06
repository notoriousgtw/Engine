package universalelectricity.core.transform.vector

import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager

class VectorWorld(var world: World, x: Double, y: Double, z: Double) extends Vector3(x, y, z) with IVectorWorld
{
  def this(nbt: NBTTagCompound) = this(DimensionManager.getWorld(nbt.getInteger("dimension")), nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))

  def this(data: ByteBuf) = this(DimensionManager.getWorld(data.readInt()), data.readDouble(), data.readDouble(), data.readDouble())

  def this(entity: Entity) = this(entity.worldObj, entity.posX, entity.posY, entity.posZ)

  def this(tile: TileEntity) = this(tile.getWorldObj, tile.xCoord, tile.yCoord, tile.zCoord)

  def this(world: World, vector: IVector3) = this(world, vector.x, vector.y, vector.z)

  def world(newWorld: World)
  {
    world = newWorld
  }

  /**
   * Conversions
   */
  override def writeNBT(nbt: NBTTagCompound): NBTTagCompound =
  {
    nbt.setInteger("dimension", world.provider.dimensionId)
    nbt.setDouble("x", x)
    nbt.setDouble("y", y)
    nbt.setDouble("z", z)
    return nbt
  }

  override def writeByteBuf(data: ByteBuf): ByteBuf =
  {
    data.writeInt(world.provider.dimensionId)
    data.writeDouble(x)
    data.writeDouble(y)
    data.writeDouble(z)
    return data
  }

  /**
   * World Access
   */
  def getBlock: Block = if (world != null) super.getBlock(world) else null

  def getBlockMetadata = if (world != null) super.getBlockMetadata(world) else null

  def getTileEntity = if (world != null) super.getTileEntity(world) else null

  def setBlock(block: Block, metadata: Int, notify: Int): Boolean = super.setBlock(world, block, metadata, notify)

  def setBlock(block: Block, metadata: Int): Boolean = super.setBlock(world, block, metadata)

  def setBlock(block: Block): Boolean = super.setBlock(world, block)

  def rayTraceEntities(target: Vector3): MovingObjectPosition = super.rayTraceEntities(world, target)

  override def clone: VectorWorld = new VectorWorld(world, x, y, z)

  override def equals(o: Any): Boolean =
  {
    if (o.isInstanceOf[VectorWorld])
    {
      return (super.equals(o)) && this.world == (o.asInstanceOf[VectorWorld]).world
    }
    return false
  }

  override def toString: String =
  {
    return "VectorWorld [" + this.x + "," + this.y + "," + this.z + "," + this.world + "]"
  }
}