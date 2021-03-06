package com.builtbroken.mc.client.json.effects;

import com.builtbroken.mc.client.effects.VisualEffectProvider;
import com.builtbroken.mc.client.effects.VisualEffectRegistry;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IEffectData;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.builtbroken.mc.lib.json.loading.JsonProcessorData;
import com.builtbroken.mc.lib.json.processors.JsonGenData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Holds audio data and is used to play audio
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2017.
 */
public class EffectLayer extends JsonGenData implements IEffectData
{
    public final String key;
    public final String effectKey;

    private NBTTagCompound nbt;
    private Pos renderOffset = Pos.zero;

    //Cache object used for calculations but keep to save memory and GC time
    private static EulerAngle angle = new EulerAngle(0, 0);

    public EffectLayer(IJsonProcessor processor, String key, String effectKey)
    {
        super(processor);
        this.key = key.toLowerCase();
        this.effectKey = effectKey.toLowerCase();
    }

    @Override
    public void register()
    {
        ClientDataHandler.INSTANCE.addEffect(key, this);
    }

    @Override
    public String getContentID()
    {
        return null;
    }

    public void trigger(World world, double x, double y, double z, double mx, double my, double mz, boolean endPoint)
    {
        trigger(world, x, y, z, mx, my, mz, endPoint, getNbt());
    }

    public void trigger(World world, double x, double y, double z, double mx, double my, double mz, boolean endPoint, NBTTagCompound nbt)
    {
        VisualEffectProvider provider = VisualEffectRegistry.main.get(effectKey);
        if (provider != null)
        {
            NBTTagCompound usedNBT;
            if (nbt != null && !nbt.hasNoTags())
            {
                usedNBT = (NBTTagCompound) nbt.copy();
                //Merges base NBT with server nbt
                if (this.getNbt() != null)
                {
                    for (Object o : getNbt().func_150296_c())
                    {
                        if (o instanceof String)
                        {
                            String key = (String) o;
                            NBTBase tag = getNbt().getTag(key);
                            if (tag != null)
                            {
                                usedNBT.setTag(key, tag);
                            }
                        }
                    }
                }
            }
            else if (this.getNbt() != null)
            {
                usedNBT = nbt;
            }
            else
            {
                usedNBT = new NBTTagCompound();
            }
            Pos renderOffset = this.renderOffset;
            if (renderOffset != Pos.zero && (usedNBT.hasKey("yaw") || usedNBT.hasKey("pitch")))
            {
                float yaw = usedNBT.getFloat("yaw");
                float pitch = usedNBT.getFloat("pitch");
                angle.set(yaw, pitch, 0);
                renderOffset = (Pos) angle.transform(renderOffset);
            }
            provider.displayEffect(world, x + renderOffset.x(), y + renderOffset.y(), z + renderOffset.z(), mx, my, mz, endPoint, usedNBT);
        }
        else
        {
            Engine.logger().error("Failed to find a visual effect provider for key '" + effectKey + "'");
        }
    }

    public NBTTagCompound getNbt()
    {
        return nbt;
    }

    @JsonProcessorData(value = "additionalEffectData", type = "nbt")
    public void setNbt(NBTTagCompound nbt)
    {
        this.nbt = nbt;
    }

    public Pos getRenderOffset()
    {
        return renderOffset;
    }

    @JsonProcessorData(value = "renderOffset", type = "pos")
    public void setRenderOffset(Pos renderOffset)
    {
        this.renderOffset = renderOffset;
    }

    @Override
    public String toString()
    {
        return "EffectData[ " + key + ", " + effectKey + "]@" + hashCode();
    }
}
