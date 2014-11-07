package resonant.lib.grid.node

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import resonant.api.grid.{INode, INodeProvider}

/**
 * A trait applied to NodeConnector
 * @author Calclavia
 */
trait TileConnector[A <: AnyRef] extends NodeConnector[A]
{
  override def reconstruct()
  {
    super.reconstruct()

    ForgeDirection.VALID_DIRECTIONS.foreach(direction =>
    {
      if (canConnect(direction))
      {
        val tile = (position + direction).getTileEntity

        if (tile != null)
        {
          val node = getNodeFrom(tile, direction.getOpposite)

          if (node != null)
          {
            connect(node.asInstanceOf[A], direction)
          }
          else if (tile.getClass.isAssignableFrom(getCompareClass))
          {
            connect(tile.asInstanceOf[A], direction)
          }
        }
      }
    })
  }

  protected def getNodeFrom(tile: TileEntity, from: ForgeDirection): INode =
  {
    if (tile.isInstanceOf[INodeProvider])
    {
      val node = tile.asInstanceOf[INodeProvider].getNode(getCompareClass, from)

      if (node != null)
      {
        return node
      }
    }

    return null
  }

  /**
   * The class used to compare when making connections
   */
  protected def getCompareClass: Class[_ <: A with INode]
}