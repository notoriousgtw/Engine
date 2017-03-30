package com.builtbroken.mc.abstraction.mod;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.versioning.ArtifactVersion;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Mod container for the abstraction layer
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2016.
 */
public class ALVEModContainer extends DummyModContainer
{
    private static final ModMetadata md;

    static
    {
        md = new ModMetadata();
        md.modId = "voltzenginemodloader";
        md.name = "Voltz Engine Modloader";
        md.version = "0.0.1";
        md.description = "Loads mods using the mod abstraction layer allowing mods to work over several MC versions.";
    }

    public ALVEModContainer()
    {
        super(md);
        AbstractionLayer.load();
        //TODO find a better way to do this....
        try
        {
            //Inject mod container
            Field field = Loader.class.getDeclaredField("mods");
            field.setAccessible(true);

            List<ModContainer> mods = (List<ModContainer>) field.get(Loader.instance());

            for (VEModContainer container : PluginModGenerator.mods)
            {
                mods.add(new InjectedModContainer(container, container.getSource()));
            }
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("VEModLoader: Failed to access field to load mods", e);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException("VEModLoader: Failed to find field to load mods", e);
        }
    }

    @Override
    public List<ArtifactVersion> getDependants()
    {
        List<ArtifactVersion> list = new ArrayList();
        for (VEModContainer container : PluginModGenerator.mods)
        {
            list.add(container.getProcessedVersion());
        }
        return list;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        return true;
    }
}
