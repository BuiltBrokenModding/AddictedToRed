package com.builtbroken.addictedtored.content;

import com.builtbroken.addictedtored.AddictedToRed;
import com.builtbroken.addictedtored.content.chat.emitter.DriverChatBlock;
import com.builtbroken.addictedtored.content.detector.entity.DriverEntityDetector;
import com.builtbroken.mc.framework.mod.Mods;
import com.builtbroken.mc.framework.mod.loadable.AbstractLoadable;
import li.cil.oc.api.Driver;

/** Proxy that handles loading content for Open Computers
 * Created by robert on 3/2/2015.
 */
public class OCProxy extends AbstractLoadable
{
    @Override
    public void init()
    {
        AddictedToRed.INSTANCE.logger().info("Loaded Open Computer Support...");
        Driver.add(new DriverChatBlock());
        Driver.add(new DriverEntityDetector());
    }

    @Override
    public boolean shouldLoad()
    {
        return Mods.OC.isLoaded();
    }
}
