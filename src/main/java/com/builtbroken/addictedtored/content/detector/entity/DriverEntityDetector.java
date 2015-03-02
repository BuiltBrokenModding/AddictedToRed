package com.builtbroken.addictedtored.content.detector.entity;

import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
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
public class DriverEntityDetector extends DriverTileEntity
{
    @Override
    public Class<?> getTileEntityClass()
    {
        return TileEntityDetector.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, int x, int y, int z)
    {
        TileEntityDetector tile = (TileEntityDetector) world.getTileEntity(x, y, z);
        if (tile.tier == TileEntityDetector.Tier.IMPROVED)
            return new EImprovedTier(tile);
        if (tile.tier == TileEntityDetector.Tier.ADVANCED)
            return new EAdvancedTier(tile);
        return new EBasicTier(tile);
    }

    public static class EBasicTier extends ManagedTileEntityEnvironment<TileEntityDetector>
    {
        public EBasicTier(final TileEntityDetector tileEntity)
        {
            super(tileEntity, "basicdetector");
        }

        public EBasicTier(final TileEntityDetector tileEntity, String name)
        {
            super(tileEntity, name);
        }

        @Callback(doc = "function():boolean --  is the detector picking up entities")
        public Object[] isDetecting(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.isDetectingEntities()};
        }

        @Callback(doc = "function():int --  number of entities the detector picks up")
        public Object[] getDetectionCount(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.entities.size()};
        }

        @Callback(doc = "function():string --  type of detection")
        public Object[] getDetectionType(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.selector.displayName().replace(" ", "")};
        }

        @Callback(doc = "function():int --  id # of the detection type")
        public Object[] getDetectionID(final Context context, final Arguments args)
        {
            return new Object[]{tileEntity.selector.ordinal()};
        }

        @Callback(doc = "function():int --  id # of the detection type")
        public Object[] getDetectionMaxID(final Context context, final Arguments args)
        {
            return new Object[]{EntitySelectors.values().length - 1};
        }

        @Callback(doc = "function(value:int) --  sets the detection type using an int")
        public Object[] setDetectionType(final Context context, final Arguments args)
        {
            int id = args.checkInteger(0);
            if (id < 0 || id >= EntitySelectors.values().length)
                throw new IllegalArgumentException("Bad detection type id");
            tileEntity.setSelector(EntitySelectors.values()[id]);
            return null;
        }
    }

    public static class EImprovedTier extends EBasicTier
    {
        public EImprovedTier(final TileEntityDetector tileEntity)
        {
            super(tileEntity, "improveddetector");
        }

        public EImprovedTier(final TileEntityDetector tileEntity, String name)
        {
            super(tileEntity, name);
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
    }

    public static class EAdvancedTier extends EImprovedTier
    {
        public EAdvancedTier(final TileEntityDetector tileEntity)
        {
            super(tileEntity, "advanceddetector");
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
