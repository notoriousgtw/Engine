package com.builtbroken.mc.framework.logic;

/**
 * Replaces {@link com.builtbroken.mc.prefab.tile.Tile}
 * <p>
 * Object that is used to control the logic behind a tile
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/30/2017.
 */
public interface ITileController
{
    /**
     * Called for the first tick of the controller
     */
    default void firstTick()
    {

    }

    /**
     * Called each tick
     *
     * @param tick
     */
    void update(long tick);

    /**
     * Called to destroy the controller
     */
    default void destroy()
    {

    }

    /**
     * Called every so often to clean up data
     * and refresh the tile's cache values.
     */
    default void doCleanupCheck()
    {

    }

    /**
     * How long to wait between cleanup calls
     *
     * @return
     */
    default int getNextCleanupTick()
    {
        return 200; //every 10 seconds (20 ticks a second, or 50ms a tick with 1000ms a second)
    }
}