package com.builtbroken.mc.client.json.render.state;

import com.builtbroken.mc.client.json.imp.IRenderState;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public abstract class RenderState implements IRenderState
{
    public final String id;
    /** ID for Super object / parent object of this render state, allows sharing state data */
    public String parent;
    /** Super object / parent object of this render state, allows sharing state data */
    public IRenderState parentState;

    public RenderState(String id)
    {
        this.id = id;
    }
}
