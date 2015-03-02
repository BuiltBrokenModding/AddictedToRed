package com.builtbroken.addictedtored.content.detector;

import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import com.builtbroken.mc.prefab.tile.Tile;
import com.builtbroken.mc.prefab.tile.item.ItemBlockMetadata;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by robert on 3/2/2015.
 */
public abstract class TileAbstractDetector extends Tile
{
    public EntitySelectors selector = EntitySelectors.MOB_SELECTOR;
    protected List<TrackingData> entities = new ArrayList();

    public TileAbstractDetector(String name, Material material)
    {
        super(name, material);
        this.canEmmitRedstone = true;
        this.itemBlock = ItemBlockMetadata.class;
        this.hardness = 2;
        this.resistance = 10;
        this.creativeTab = CreativeTabs.tabRedstone;
    }

    @Override
    public void update()
    {
        super.update();
        if (isServer() && ticks % 3 == 0)
        {
            AxisAlignedBB bb = getBoundBox();
            if (bb != null)
            {
                int s = entities.size();
                List<Entity> list = world().selectEntitiesWithinAABB(Entity.class, bb, selector.selector());

                Iterator<TrackingData> it = entities.iterator();
                while (it.hasNext())
                {
                    TrackingData data = it.next();

                    if (list.contains(data.entity))
                    {
                        //Update existing entries
                        data.distance = getDistanceFrom(data.entity.posX, data.entity.posY, data.entity.posZ);
                        list.remove(data.entity);
                    }
                    else
                    {
                        //Remove old entries
                        it.remove();
                    }
                }

                //Add new entries
                for (Entity entity : list)
                {
                    entities.add(new TrackingData(entity, this));
                }

                if (s != entities.size())
                {
                    world().notifyBlocksOfNeighborChange(xi(), yi(), zi(), this.getTileBlock());
                }
            }
        }
    }

    @Override
    public int getStrongRedstonePower(int side)
    {
        return isDetectingEntities() ? 15 : 0;
    }

    public boolean isDetectingEntities()
    {
        return entities.size() > 0;
    }

    public abstract AxisAlignedBB getBoundBox();

    public void setSelector(EntitySelectors selector)
    {
        this.selector = selector;
        sendPacketToServer(getDescPacket());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("selector"))
        {
            this.selector = EntitySelectors.get(nbt.getInteger("selector"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (selector != EntitySelectors.MOB_SELECTOR)
        {
            nbt.setInteger("selector", selector.ordinal());
        }
    }
}
