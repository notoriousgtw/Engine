package com.builtbroken.mc.abstraction.mod;

import com.builtbroken.mc.abstraction.AbstractionLayer;
import com.builtbroken.mc.api.mod.IMod;
import com.builtbroken.mc.framework.tile.BlockProperties;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;

import java.io.File;
import java.security.cert.Certificate;
import java.util.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2016.
 */
public class VEModContainer implements ModContainer
{
    public final IMod mod;
    private final ModMetadata modMeta;
    private final File file;

    private ArtifactVersion processedVersion;

    public VEModContainer(IMod mod, File file)
    {
        this.mod = mod;
        this.file = file;

        //Load data into metadata
        modMeta = new ModMetadata();
        modMeta.name = mod.getDisplayName();
        modMeta.modId = mod.getDomain();
        modMeta.version = mod.getVersion();
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event)
    {
        mod.loadHandlers();
    }

    @Subscribe
    public void init(FMLInitializationEvent event)
    {
        List<BlockProperties> blocks = new ArrayList();
        mod.loadBlocks(blocks);
        AbstractionLayer.blockHandler.loadBlocks(blocks);
        mod.loadItems();

    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent event)
    {
        mod.loadRecipes();
        mod.postLoad();
    }

    @Override
    public String getModId()
    {
        return modMeta.modId;
    }

    @Override
    public String getName()
    {
        return modMeta.name;
    }

    @Override
    public String getVersion()
    {
        return modMeta.version;
    }

    @Override
    public File getSource()
    {
        return file;
    }

    @Override
    public ModMetadata getMetadata()
    {
        return modMeta;
    }

    @Override
    public void bindMetadata(MetadataCollection mc)
    {

    }

    @Override
    public void setEnabledState(boolean enabled)
    {

    }

    @Override
    public Set<ArtifactVersion> getRequirements()
    {
        return Collections.emptySet();
    }

    @Override
    public List<ArtifactVersion> getDependencies()
    {
        return Collections.emptyList();
    }

    @Override
    public List<ArtifactVersion> getDependants()
    {
        return Collections.emptyList();
    }

    @Override
    public String getSortingRules()
    {
        return "";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

    @Override
    public boolean matches(Object mod)
    {
        return false;
    }

    @Override
    public Object getMod()
    {
        return null;
    }

    @Override
    public ArtifactVersion getProcessedVersion()
    {
        if (processedVersion == null)
        {
            processedVersion = new DefaultArtifactVersion(getModId(), true);
        }
        return processedVersion;
    }

    @Override
    public boolean isImmutable()
    {
        return false;
    }

    @Override
    public String getDisplayVersion()
    {
        return modMeta.version;
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange()
    {
        return Loader.instance().getMinecraftModContainer().getStaticVersionRange();
    }

    @Override
    public Certificate getSigningCertificate()
    {
        return null;
    }

    @Override
    public Map<String, String> getCustomModProperties()
    {
        return EMPTY_PROPERTIES;
    }

    @Override
    public Class<?> getCustomResourcePackClass()
    {
        return null;
    }

    @Override
    public Map<String, String> getSharedModDescriptor()
    {
        return null;
    }

    @Override
    public Disableable canBeDisabled()
    {
        return Disableable.NEVER;
    }

    @Override
    public String getGuiClassName()
    {
        return null;
    }

    @Override
    public List<String> getOwnedPackages()
    {
        return ImmutableList.of();
    }
}
