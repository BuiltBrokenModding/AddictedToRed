package com.builtbroken.addictedtored.content;

import com.builtbroken.addictedtored.AddictedToRed;
import com.builtbroken.mc.api.tile.IGuiTile;
import com.builtbroken.mc.core.network.IByteBufWriter;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import com.builtbroken.mc.prefab.gui.ContainerDummy;
import com.builtbroken.mc.prefab.tile.Tile;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Basic machine designed to detect entities around it
 * Created by robert on 2/21/2015.
 */
public class TileEntityDetector extends Tile implements IPacketIDReceiver, IGuiTile
{
    public static int MAX_RANGE = 10;

    public Tier tier = Tier.BASIC;

    protected Pos target = new Pos(0, -1, 0);
    protected Pos range = new Pos(0, -1, 0);
    protected EntitySelectors selector = EntitySelectors.MOB_SELECTOR;
    protected List<TrackingData> entities = new ArrayList();

    private AxisAlignedBB bb = null;


    public TileEntityDetector()
    {
        super("detector", Material.rock);
    }

    @Override
    public void firstTick()
    {
        super.firstTick();
        recalc();
    }

    @Override
    public void update()
    {
        super.update();
        if (ticks % 3 == 0)
        {
            if (bb != null)
            {
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
            }
            else
            {
                //TODO display error message
            }
        }
    }

    public boolean isDetectingEntities()
    {
        return entities.size() > 0;
    }

    protected void recalc()
    {
        if (!isRangeValid())
        {
            range = new Pos(5, 5, 5);
        }
        if (!isTargetValid())
        {
            target = new Pos(this).add(0.5);
        }
        if (isRangeValid() && isTargetValid())
        {
            bb = AxisAlignedBB.getBoundingBox(target.x() + range.x(), target.y() + range.y(), target.z() + range.z(), target.x() - range.x(), target.y() - range.y(), target.z() - range.z());
        }
        else
        {
            bb = null;
        }
    }

    protected boolean isRangeValid()
    {
        return range != null && range.x() > 0 && range.x() < MAX_RANGE && range.y() > 0 && range.y() < MAX_RANGE && range.z() > 0 && range.z() < MAX_RANGE;
    }

    protected boolean isTargetValid()
    {
        return target != null && target.isAboveBedrock() && target.distance(new Pos(this)) <= MAX_RANGE;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("target"))
        {
            this.target = new Pos(nbt.getCompoundTag("target"));
        }
        if (nbt.hasKey("range"))
        {
            this.range = new Pos(nbt.getCompoundTag("range"));
        }
        if (nbt.hasKey("selector"))
        {
            this.selector = EntitySelectors.get(nbt.getInteger("selector"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (isRangeValid())
        {
            nbt.setTag("range", range.toNBT());
        }
        if (isTargetValid())
        {
            nbt.setTag("target", target.toNBT());
        }
        if (selector != EntitySelectors.MOB_SELECTOR)
        {
            nbt.setInteger("selector", selector.ordinal());
        }
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if (id == 0)
        {
            this.target = new Pos(buf);
            this.range = new Pos(buf);
            this.selector = EntitySelectors.get(buf.readInt());
            return true;
        }
        return false;
    }

    @Override
    public AbstractPacket getDescPacket()
    {
        return new PacketTile(this, 0, target, range, selector.ordinal());
    }

    protected void sendClientPacket()
    {
        //TODO send all detection data to client
    }

    public void setData(Pos range, Pos target, int selector)
    {
        this.range = range;
        this.target = target;
        this.selector = EntitySelectors.get(selector);
        recalc();
        if (isClient())
        {
            sendDescPacket();
        }
    }

    public void setTarget(Pos target)
    {
        if(target != this.target)
        {
            this.target = target;
            if (isClient())
            {
                sendDescPacket();
            }
        }
    }

    public void setRange(Pos range)
    {
        if(this.range != range)
        {
            this.range = range;
            if (isClient())
            {
                sendDescPacket();
            }
        }
    }

    public void setSelector(EntitySelectors selector)
    {
        if(selector != this.selector)
        {
            this.selector = selector;
            if (isClient())
            {
                sendDescPacket();
            }
        }
    }

    @Override
    public Tile newTile()
    {
        return new TileEntityDetector();
    }

    @Override
    protected boolean onPlayerRightClick(EntityPlayer player, int side, Pos hit)
    {
        if (isServer())
        {
            openGui(player, AddictedToRed.INSTANCE);
        }
        return true;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player)
    {
        return new ContainerDummy();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiEntityDetector(this, player);
    }

    /**
     * Used too store a little info about what we are tracking. Will be tied into
     * a GUI and Open Computers later on.
     */
    public static class TrackingData implements IByteBufWriter
    {
        public final Entity entity;
        public double distance;

        public TrackingData(Entity entity, Tile tile)
        {
            this.entity = entity;
            distance = tile.getDistanceFrom(entity.posX, entity.posY, entity.posZ);
        }

        public TrackingData(World world, ByteBuf buf)
        {
            entity = world.getEntityByID(buf.readInt());
            distance = buf.readDouble();
        }

        public String name()
        {
            return entity.getCommandSenderName();
        }

        @Override
        public ByteBuf writeBytes(ByteBuf buf)
        {
            buf.writeInt(entity.getEntityId());
            buf.writeDouble(distance);
            return buf;
        }
    }

    public static enum Tier
    {
        /**
         * Can only select detection type
         */
        BASIC,
        /**
         * Can select range
         */
        IMPROVED,
        /**
         * Can select target, and has a whitelist/blacklist
         */
        ADVANCED;
    }
}
