package com.builtbroken.addictedtored.content.detector.selection;

import com.builtbroken.addictedtored.AddictedToRed;
import com.builtbroken.addictedtored.content.detector.TileAbstractDetector;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.tile.IGuiTile;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import com.builtbroken.mc.prefab.gui.ContainerDummy;
import com.builtbroken.mc.prefab.tile.Tile;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

/**
 * Created by robert on 3/2/2015.
 */
public class TileSelectionDetector extends TileAbstractDetector implements IGuiTile, IPacketIDReceiver
{
    public Cube selection;
    public int maxRange = 100;

    public TileSelectionDetector()
    {
        super("sdetector", Material.rock);
    }

    @Override
    public AxisAlignedBB getBoundBox()
    {
        if(selection == null || !selection.isValid() || selection.max().distance(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) > maxRange || selection.min().distance(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) > maxRange)
        {
            selection = new Cube(new Pos(this).sub(10), new Pos(this).add(10));
        }
        return selection.toAABB();
    }

    public void setSelection(IPos3D one, IPos3D two)
    {
        selection.setPointOne(one);
        selection.setPointTwo(two);
        sendPacketToServer(getDescPacket());
    }

    @Override
    public Tile newTile()
    {
        return new TileSelectionDetector();
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
        return new GuiSelectionDetector(this, player);
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if(id == 0)
        {
            selection = new Cube(buf);
            selector = EntitySelectors.get(buf.readInt());
            return true;
        }
        return false;
    }

    @Override
    public AbstractPacket getDescPacket()
    {
        return new PacketTile(this, 0, selection != null ? selection : new Cube(), selector.ordinal());
    }
}
