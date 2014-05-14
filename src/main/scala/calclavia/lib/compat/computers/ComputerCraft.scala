package calclavia.lib.compat.computers

import calclavia.lib.modproxy.ICompatProxy
import calclavia.lib.utility.Mods

/**
 * Stub for the moment, in 1.7 will be adjusted to ship no API files at all.
 *
 * @since 12/04/14
 * @author tgame14
 */
class ComputerCraft extends ICompatProxy {
  override def preInit(): Unit =
    {

    }

  override def init(): Unit =
    {

    }

  override def postInit(): Unit =
    {

    }

  override def modId(): String = Mods.CC

}
