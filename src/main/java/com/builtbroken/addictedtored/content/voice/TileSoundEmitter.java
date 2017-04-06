package com.builtbroken.addictedtored.content.voice;

import com.builtbroken.addictedtored.AddictedToRed;
import com.builtbroken.addictedtored.content.Tier;
import com.builtbroken.addictedtored.content.TileAbstractRedstone;
import com.builtbroken.mc.api.tile.access.IGuiTile;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.prefab.gui.ContainerDummy;
import com.builtbroken.mc.prefab.tile.Tile;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * Plays sounds effects
 * Created by robert on 3/5/2015.
 */
public class TileSoundEmitter extends TileAbstractRedstone implements IGuiTile
{
    protected String sound = "mob.villager.haggle";

    protected String sound_cat = EnumMCSounds.VILLAGER_HAGGLE.cat;
    protected String sound_name = EnumMCSounds.VILLAGER_HAGGLE.name;

    protected float volume = 5;
    protected float pitch = 1;

    public Tier tier = Tier.BASIC;

    public TileSoundEmitter()
    {
        super("soundEmitter", Material.rock);
    }

    @Override
    public void firstTick()
    {
        super.firstTick();
        int meta = worldObj.getBlockMetadata(xi(), yi(), zi());
        if (meta < Tier.values().length)
            tier = Tier.values()[meta];
    }

    @Override
    public void triggerRedstone()
    {
        if (isClient())
        {
            if (tier != Tier.ADVANCED)
            {
                EnumMCSounds.playSound(sound_cat, sound_name, new Location((TileEntity)this), getVolume(), getPitch());
            }
            else
            {
                world().playSound(x() + 0.5, y() + 0.5, z() + 0.5, getSoundName(), getVolume(), getPitch(), true);
            }
        }
        else
        {
            sendRedstoneSignalPacket();
        }
    }

    @Override
    public AbstractPacket getDescPacket()
    {
        if (tier != Tier.ADVANCED)
        {
            return new PacketTile(this, 0, sound_cat, sound_name, volume, pitch);
        }
        return new PacketTile(this, 1, sound, volume, pitch);
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if (id == 0)
        {
            this.sound_cat = ByteBufUtils.readUTF8String(buf);
            this.sound_name = ByteBufUtils.readUTF8String(buf);
            this.volume = buf.readFloat();
            this.pitch = buf.readFloat();
            return true;
        }
        else if (id == 1)
        {
            this.sound_name = ByteBufUtils.readUTF8String(buf);
            this.volume = buf.readFloat();
            this.pitch = buf.readFloat();
            return true;
        }
        return super.read(buf, id, player, type);
    }

    @Override
    public Tile newTile()
    {
        return new TileSoundEmitter();
    }

    public String getSoundName()
    {
        return sound;
    }

    public void setSoundName(String sound_name)
    {
        if (tier != Tier.BASIC)
        {
            this.sound = sound_name;
            if (isClient())
                sendPacketToServer(getDescPacket());
        }
    }

    public float getVolume()
    {
        return volume;
    }

    public void setVolume(float volume)
    {
        this.volume = volume;
        if (isClient())
            sendPacketToServer(getDescPacket());
    }

    public float getPitch()
    {
        return pitch;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
        if (isClient())
            sendPacketToServer(getDescPacket());
    }

    public void setSound(String cat, String name)
    {
        if (tier != Tier.ADVANCED)
        {
            this.sound_cat = cat;
            this.sound_name = name;
            if (isClient())
                sendPacketToServer(getDescPacket());
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setFloat("volume", volume);
        nbt.setFloat("pitch", pitch);
        nbt.setString("sound", sound);
        nbt.setString("sound_name", sound_name);
        nbt.setString("sound_cat", sound_cat);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("volume"))
            this.volume = nbt.getFloat("volume");
        if (nbt.hasKey("pitch"))
            this.pitch = nbt.getFloat("pitch");
        if (nbt.hasKey("sound"))
            this.sound = nbt.getString("sound");
        if (nbt.hasKey("sound_name"))
            this.sound_name = nbt.getString("sound_name");
        if (nbt.hasKey("sound_cat"))
            this.sound_cat = nbt.getString("sound_cat");
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player)
    {
        return new ContainerDummy();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiSoundBlock(this, player);
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
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }
}