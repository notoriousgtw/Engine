package resonant.engine.content.debug

import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._
import resonant.lib.content.prefab.java.TileIO
import resonant.lib.multiblock.synthetic.IBlockActivate
import universalelectricity.core.transform.vector.Vector3

/**
 * Designed to debug fluid devices by draining everything that comes in at one time
 *
 * @author DarkGuardsman
 */
class TileInfiniteFluid extends TileIO(Material.iron) with IFluidHandler
{
  saveIOMap = true
  tank = new FluidTank(Integer.MAX_VALUE)
  ioMap = 728

  var tank: FluidTank = null
  var active: Boolean = false

  override def update()
  {
    super.updateEntity

    if (active)
    {
      ForgeDirection.VALID_DIRECTIONS.filter(getOutputDirections.contains(_)).foreach(
        direction =>
        {
          val tile: TileEntity = (new Vector3(this) + direction).getTileEntity(world)

          if (tile.isInstanceOf[IFluidHandler])
          {
            (tile.asInstanceOf[IFluidHandler]).fill(direction.getOpposite, tank.getFluid, true)
          }

        });
    }
  }

  def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] =
  {
    return Array[FluidTankInfo](this.tank.getInfo)
  }

  def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int =
  {
    if (getInputDirections.contains(from))
    {
      return resource.amount
    }
    return 0
  }

  def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack =
  {
    if (getOutputDirections.contains(from))
    {
      return resource
    }
    return null
  }

  def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack =
  {
    if (getOutputDirections.contains(from))
    {
      return this.tank.drain(maxDrain, false)
    }
    return null
  }

  def canFill(from: ForgeDirection, fluid: Fluid): Boolean =
  {
    return getInputDirections.contains(from)
  }

  def canDrain(from: ForgeDirection, fluid: Fluid): Boolean =
  {
    return getOutputDirections.contains(from)
  }

  /*
    def onActivated(entityPlayer: EntityPlayer): Boolean =
    {
      if (entityPlayer != null && entityPlayer.getHeldItem != null)
      {
        if (entityPlayer.getHeldItem.getItem eq Item.stick)
        {
          this.active = !this.active
          entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("[FluidVoid] Pumping:" + this.active))
          return true
        }
        var stack: FluidStack = FluidContainerRegistry.getFluidForFilledItem(entityPlayer.getHeldItem)
        if (stack != null)
        {
          stack = stack.copy
          stack.amount = Integer.MAX_VALUE
          this.tank.setFluid(stack)
          entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("[FluidVoid] Fluid:" + stack.getFluid.getName))
          return true
        }
      }
      return false
    }*/

}