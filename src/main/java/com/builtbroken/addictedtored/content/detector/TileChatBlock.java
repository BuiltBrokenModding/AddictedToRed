package com.builtbroken.addictedtored.content.detector;

import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.Tile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 3/2/2015.
 */
public class TileChatBlock extends Tile
{
    public boolean prev_signal = false;
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
    public void onNeighborChanged(Block block)
    {
        boolean red = world().isBlockIndirectlyGettingPowered(xi(), yi(), zi());
        if (target == null || !target.isAboveBedrock())
        {
            target = new Pos(this).add(0.5);
        }
        if (range == null || !range.isAboveBedrock())
        {
            range = new Pos(5, 5, 5);
        }
        if (!prev_signal && red)
        {
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(target.x() - range.x(), target.y() - range.y(), target.z() - range.z(), target.x() + range.x(), target.y() + range.y(), target.z() + range.z());
            List<EntityPlayer> list = world().getEntitiesWithinAABB(EntityPlayer.class, bb);
            for (EntityPlayer player : list)
            {
                player.addChatComponentMessage(new ChatComponentText("" + output_msg));
            }
        }
        prev_signal = red;
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
        if(nbt.hasKey("msg"))
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
}
