package com.builtbroken.addictedtored.content.detector;

import com.builtbroken.jlib.data.network.IByteBufWriter;
import com.builtbroken.mc.prefab.tile.Tile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class TrackingData implements IByteBufWriter
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