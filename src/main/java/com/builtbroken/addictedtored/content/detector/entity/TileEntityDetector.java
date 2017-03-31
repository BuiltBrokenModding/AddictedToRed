package com.builtbroken.addictedtored.content.detector.entity;

import com.builtbroken.addictedtored.AddictedToRed;
import com.builtbroken.addictedtored.content.Tier;
import com.builtbroken.addictedtored.content.detector.TileAbstractDetector;
import com.builtbroken.mc.api.tile.IGuiTile;
import com.builtbroken.mc.core.network.IPacketIDReceiver;
import com.builtbroken.mc.core.network.packet.AbstractPacket;
import com.builtbroken.mc.core.network.packet.PacketTile;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.lib.helper.recipe.OreNames;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import com.builtbroken.mc.prefab.gui.ContainerDummy;
import com.builtbroken.mc.prefab.tile.Tile;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

/**
 * Basic machine designed to detect entities around it
 * Created by robert on 2/21/2015.
 */
public class TileEntityDetector extends TileAbstractDetector implements IPacketIDReceiver, IGuiTile, IPostInit
{
    public static int MAX_RANGE = 10;

    public Tier tier = Tier.BASIC;

    protected Pos target = new Pos(0, -1, 0);
    protected Pos range = new Pos(5, 5, 5);


    protected static IIcon basic_icon;
    protected static IIcon improved_icon;
    protected static IIcon advanced_icon;

    public TileEntityDetector()
    {
        super("detector", Material.rock);
    }

    @Override
    public void onPostInit()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AddictedToRed.basicDetector, 1, 0), "wsw", "cec", "wsw", 'c', Items.comparator, 'e', Items.ender_pearl, 's', Blocks.stone_slab, 'w', OreNames.INGOT_IRON));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AddictedToRed.basicDetector, 1, 1), "sls", "cec", "sls", 'c', Items.ender_pearl, 'e', new ItemStack(AddictedToRed.basicDetector, 1, 0), 's', OreNames.STONE, 'l', Blocks.lever));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(AddictedToRed.basicDetector, 1, 2), "sls", "cec", "sls", 'c', Items.ender_pearl, 'e', new ItemStack(AddictedToRed.basicDetector, 1, 1), 's', OreNames.INGOT_IRON, 'l', OreNames.INGOT_GOLD));
    }

    @Override
    public void firstTick()
    {
        super.firstTick();
        int meta = worldObj.getBlockMetadata(xi(), yi(), zi());
        if(meta < Tier.values().length)
            tier = Tier.values()[meta];
    }

    protected boolean isRangeValid()
    {
        return range != null && range.x() > 0 && range.x() < MAX_RANGE && range.y() > 0 && range.y() < MAX_RANGE && range.z() > 0 && range.z() < MAX_RANGE;
    }

    protected boolean isTargetValid()
    {
        return target != null && target.isAboveBedrock() && target.distance(new Pos((TileEntity)this).add(0.5)) <= MAX_RANGE;
    }

    @Override
    public AxisAlignedBB getBoundBox()
    {
        if (target == null || target.yi() == -1)
        {
            target = new Pos((TileEntity)this);
        }
        if (isRangeValid() && isTargetValid())
        {
            return AxisAlignedBB.getBoundingBox(target.x() - range.x(), target.y() - range.y(), target.z() - range.z(), target.x() + range.x(), target.y() + range.y(), target.z() + range.z());
        }
        return null;
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
        sendPacketToServer(getDescPacket());
    }

    public void setTarget(Pos target)
    {
        if(tier == Tier.BASIC)
        {
            this.target = target;
            sendPacketToServer(getDescPacket());
        }
    }

    public void setRange(Pos range)
    {
        if(tier != Tier.ADVANCED)
        {
            this.range = range;
            sendPacketToServer(getDescPacket());
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

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    @Override @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        basic_icon = reg.registerIcon(AddictedToRed.PREFIX + "detector.basic");
        improved_icon = reg.registerIcon(AddictedToRed.PREFIX + "detector.improved");
        advanced_icon = reg.registerIcon(AddictedToRed.PREFIX + "detector.advanced");
    }

    @Override @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if(meta == 1)
        {
            return improved_icon;
        }
        else if(meta == 2)
        {
            return advanced_icon;
        }
        return basic_icon;
    }

    @Override @SideOnly(Side.CLIENT)
    public IIcon getIcon()
    {
        if(tier.ordinal() == 1)
        {
            return improved_icon;
        }
        else if(tier.ordinal() == 2)
        {
            return advanced_icon;
        }
        return basic_icon;
    }
}
