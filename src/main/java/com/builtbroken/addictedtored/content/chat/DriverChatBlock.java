package com.builtbroken.addictedtored.content.chat;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;
import net.minecraft.world.World;

/**
 * Created by robert on 3/2/2015.
 */
public class DriverChatBlock extends DriverTileEntity
{
    @Override
    public Class<?> getTileEntityClass()
    {
        return TileChatBlock.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, int x, int y, int z)
    {
        return new Environment((TileChatBlock) world.getTileEntity(x, y, z));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileChatBlock>
    {
        public Environment(final TileChatBlock tileEntity)
        {
            super(tileEntity, "chat_block");
        }

        @Callback(doc = "function() --  Triggers the msg to display.")
        public Object[] triggerMsg(final Context context, final Arguments args)
        {
            tileEntity.trigger();
            return null;
        }

        @Callback(doc = "function():string --  Returns the msg sent to players.")
        public Object[] getMsg(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.output_msg};
        }

        @Callback(doc = "function(value:string) --  sets the msg sent to players.")
        public Object[] setMsg(final Context context, final Arguments args)
        {
            String msg = args.checkString(0);
            if (msg.length() > 200)
            {
                throw new IllegalArgumentException("msg is too long");
            }
            tileEntity.setMsg(msg);
            return null;
        }

        @Callback(doc = "function():int -- distance of detection")
        public Object[] getRange(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.range};
        }

        @Callback(doc = "function():int --  x distance of detection")
        public Object[] getRangeX(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.range.xi()};
        }

        @Callback(doc = "function():int --  y distance of detection")
        public Object[] getRangeY(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.range.yi()};
        }

        @Callback(doc = "function():int --  z distance of detection")
        public Object[] getRangeZ(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.range.zi()};
        }

        @Callback(doc = "function():int -- focus point of detection")
        public Object[] getTarget(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.target};
        }

        @Callback(doc = "function():int --  x focus point of detection")
        public Object[] getTargetX(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.target.xi()};
        }

        @Callback(doc = "function():int --  y focus point of detection")
        public Object[] getTargetY(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.target.yi()};
        }

        @Callback(doc = "function():int --  z focus point of detection")
        public Object[] getTargetZ(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.target.zi()};
        }
    }
}
