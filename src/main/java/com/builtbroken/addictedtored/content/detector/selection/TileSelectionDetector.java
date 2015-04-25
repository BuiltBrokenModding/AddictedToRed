package com.builtbroken.addictedtored.content.detector.selection;

import com.builtbroken.addictedtored.AddictedToRed;
import com.builtbroken.addictedtored.content.detector.TileAbstractDetector;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.tile.IGuiTile;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.lib.transform.region.Cube;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import com.builtbroken.mc.prefab.gui.ContainerDummy;
import com.builtbroken.mc.prefab.tile.Tile;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by robert on 3/2/2015.
 */
public class TileSelectionDetector extends TileAbstractDetector implements IGuiTile, IPacketIDReceiver, IPostInit
{
    public Cube selection;
    public int maxRange = 100;

    @SideOnly(Side.CLIENT)
    public static IIcon basic_icon;

    public TileSelectionDetector()
    {
        super("sdetector", Material.rock);
    }

    @Override
    public void firstTick()
    {
        super.firstTick();
        if(selection == null)
        {
            selection = new Cube(toPos().sub(5), toPos().add(5));
        }
    }

    @Override
    public void onPostInit()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AddictedToRed.selectionDetector, 1, 0), "wcw", "cec", "wcw", 'c', Items.ender_eye, 'e', new ItemStack(AddictedToRed.basicDetector, 1, 2), 'w', Items.emerald));
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

    @Override @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.basic_icon = reg.registerIcon(AddictedToRed.PREFIX + "detector.area");
    }

    @Override @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return basic_icon;
    }

    @Override @SideOnly(Side.CLIENT)
    public IIcon getIcon()
    {
        return basic_icon;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if(nbt.hasKey("selection"))
            selection = new Cube(nbt.getCompoundTag("selection"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if(selection != null)
            nbt.setTag("selection", selection.toNBT());
    }
}
