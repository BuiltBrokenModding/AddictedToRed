package com.builtbroken.addictedtored.content.chat.emitter;

import com.builtbroken.addictedtored.AddictedToRed;
import com.builtbroken.addictedtored.content.TileAbstractRedstone;
import com.builtbroken.mc.api.tile.IGuiTile;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.lib.helper.recipe.OreNames;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.gui.ContainerDummy;
import com.builtbroken.mc.prefab.tile.Tile;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

/**
 * Created by robert on 3/2/2015.
 */
public class TileChatBlock extends TileAbstractRedstone implements IGuiTile, IPacketIDReceiver, IPostInit
{
    public static IIcon basic_icon;

    public String output_msg = "Hello!";

    public Pos target = new Pos(0, -1, 0);
    public Pos range = new Pos(0, -1, 0);

    public TileChatBlock()
    {
        super("chatblock", Material.rock);
        this.canEmmitRedstone = true;
        this.creativeTab = CreativeTabs.tabRedstone;
    }


    @Override
    public void onPostInit()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AddictedToRed.chatBlock, 1, 0), "sps", "cec", "sps", 'c', Blocks.pumpkin, 'e', new ItemStack(AddictedToRed.basicDetector, 1, 1), 's', OreNames.INGOT_IRON, 'p', Blocks.sticky_piston));
    }

    @Override
    public void triggerRedstone()
    {
        if (target == null || !target.isAboveBedrock())
        {
            target = new Pos((TileEntity)this).add(0.5);
        }
        if (range == null || !range.isAboveBedrock())
        {
            range = new Pos(5, 5, 5);
        }

        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(target.x() - range.x(), target.y() - range.y(), target.z() - range.z(), target.x() + range.x(), target.y() + range.y(), target.z() + range.z());
        List<EntityPlayer> list = world().getEntitiesWithinAABB(EntityPlayer.class, bb);
        for (EntityPlayer player : list)
        {
            player.addChatComponentMessage(new ChatComponentText("" + output_msg));
        }
    }

    @Override
    public Tile newTile()
    {
        return new TileChatBlock();
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
        if (nbt.hasKey("msg"))
            this.output_msg = nbt.getString("msg");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setTag("range", range.toNBT());
        nbt.setTag("target", target.toNBT());
        nbt.setString("msg", output_msg);
    }

    @Override
    public boolean read(ByteBuf buf, int id, EntityPlayer player, PacketType type)
    {
        if (id == 0)
        {
            this.target = new Pos(buf);
            this.range = new Pos(buf);
            this.output_msg = ByteBufUtils.readUTF8String(buf);
            return true;
        }
        return false;
    }

    @Override
    public AbstractPacket getDescPacket()
    {
        return new PacketTile(this, 0, target, range, output_msg);
    }

    public void setTarget(Pos target)
    {
        this.target = target;
        sendPacketToServer(getDescPacket());
    }

    public void setRange(Pos range)
    {
        this.range = range;
        sendPacketToServer(getDescPacket());
    }

    public void setMsg(String msg)
    {
        this.output_msg = msg;
        sendPacketToServer(getDescPacket());
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player)
    {
        return new ContainerDummy();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiChatBlock(this, player);
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        basic_icon = reg.registerIcon(AddictedToRed.PREFIX + "chat.basic");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return basic_icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon()
    {
        return basic_icon;
    }
}
