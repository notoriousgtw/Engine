package com.builtbroken.mc.lib.mod.compat;

import cpw.mods.fml.common.Loader;

/**
 * Enum of supported mods, mainly used to track mod ids.
 * Created by robert on 12/31/2014.
 */
public enum Mods
{
    OC("OpenComputers"),
    CC("ComputerCraft"),
    AE("AppliedEnergistics"),
    AM2("arsmagica2"),
    BOP("BiomesOPlenty"),
    BC("BuildCraft|Core"),
    IC2("IC2"),
    WAILA("Waila"),
    DRAGON_API("DragonAPI"),
    RF(""),
    TINKERS("TConstruct"),
    MFR("MineFactoryReloaded"),
    TF_EXPANSION("ThermalExpansion"),
    TF_FOUNDATION("ThermalFoundation"),
    CoFH_API_ENERGY("CoFHAPI|energy"),
    CoFH_CORE("CoFHCore"),
    NEI("NotEnoughItems");

    public final String mod_id;

    Mods(String id)
    {
        this.mod_id = id;
    }

    public boolean isLoaded()
    {
        return Loader.isModLoaded(mod_id);
    }
}
