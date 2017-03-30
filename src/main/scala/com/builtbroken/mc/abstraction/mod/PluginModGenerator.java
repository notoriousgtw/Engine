package com.builtbroken.mc.abstraction.mod;

import com.builtbroken.mc.api.mod.IMod;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/17/2016.
 *
 * -Dfml.coreMods.load=com.builtbroken.mc.abstraction.mod.PluginModGenerator
 */
@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
public class PluginModGenerator implements IFMLLoadingPlugin, IFMLCallHook
{
    public static final List<VEModContainer> mods = new ArrayList();

    public static File minecraftDir;
    public static String currentMcVersion;
    public static Logger logger = LogManager.getLogger("VE-ModLoader");

    public PluginModGenerator()
    {
        if (minecraftDir == null)
        {
            minecraftDir = (File) FMLInjectionData.data()[6];
            currentMcVersion = (String) FMLInjectionData.data()[4];
        }
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return "com.builtbroken.mc.abstraction.mod.ALVEModContainer";
    }

    @Override
    public String getSetupClass()
    {
        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }

    @Override
    public Void call()
    {
        //Loads mods from system props, mainly only for development environments
        if(System.getProperty("ve.mods.load") != null)
        {
            String mods = System.getProperty("ve.mods.load");
            if(mods != null && !mods.isEmpty())
            {
                String[] split = mods.split(";");
                for (String s : split)
                {
                    newMod(s, null);
                }
            }
        }
        //Looks for mods to load from file
        File modsDir = new File(minecraftDir, "mods");
        for (File file : modsDir.listFiles())
        {
            scanMod(file);
        }
        File versionModsDir = new File(minecraftDir, "mods/" + currentMcVersion);
        if (versionModsDir.exists())
        {
            for (File file : versionModsDir.listFiles())
            {
                scanMod(file);
            }
        }
        //TODO load from class path
        //TODO look for annotations, use ASM loader for this as it passes all classes to it during ASM time
        return null;
    }

    //Loads a mod from file
    private void scanMod(File file)
    {
        if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip"))
        {
            try
            {
                JarFile jar = new JarFile(file);
                try
                {
                    Manifest manifest = jar.getManifest();
                    if (manifest != null)
                    {
                        Attributes attr = manifest.getMainAttributes();
                        if (attr != null)
                        {
                            String className = attr.getValue("VExModClass");
                            if (className != null)
                            {
                               newMod(className, file);
                            }
                        }
                    }
                }
                finally
                {
                    jar.close();
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException("Failed to load mod", e);
            }
        }
    }

    //Loads a mod's main class file
    private void newMod(String className, File file)
    {
        try
        {
            Class clazz = getClass().getClassLoader().loadClass(className);
            IMod mod = (IMod) clazz.newInstance();
            VEModContainer container = new VEModContainer(mod, file);
            mods.add(container);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Failed to access mod class " + className, e);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException("Failed to load mod class " + className, e);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to find mod class " + className, e);
        }
        //catch (NoSuchFieldException e)
        //{
        //    throw new RuntimeException("Failed to find field required to load mods", e);
        //}
    }
}
