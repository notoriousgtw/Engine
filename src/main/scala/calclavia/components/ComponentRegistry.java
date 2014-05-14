package calclavia.components;

import java.util.LinkedHashSet;
import java.util.Set;

import calclavia.lib.Calclavia;

/** This should be the only class you include in your mod. If your mod is a coremod, feel free to
 * download Basic Components Core directly during run-time.
 * 
 * @author Calclavia */

public class ComponentRegistry
{
    public static final Set<String> requests = new LinkedHashSet<String>();

    /** @param request - Name of the item/block to register. Use the EXACT FIELD NAME of the
     * BasicComponents.java field. */
    public static void register(String request)
    {
        Calclavia.CONFIGURATION.load();
        if (Calclavia.CONFIGURATION.get("Allow Load Items", request, true).getBoolean(true))
        {
            requests.add(request);
        }
        Calclavia.CONFIGURATION.save();
    }

    /** Requests all items in Basic Components. */
    public static void registerAll()
    {
        register("itemIngotCopper");
        register("itemIngotTin");

        register("blockOreCopper");
        register("blockOreTin");

        register("itemIngotSteel");
        register("itemDustSteel");
        register("itemPlateSteel");

        register("itemIngotBronze");
        register("itemDustBronze");
        register("itemPlateBronze");

        register("itemPlateCopper");
        register("itemPlateTin");
        register("itemPlateIron");
        register("itemPlateGold");

        register("itemCircuitBasic");
        register("itemCircuitAdvanced");
        register("itemCircuitElite");

        register("itemMotor");
        register("itemWrench");
    }
}
