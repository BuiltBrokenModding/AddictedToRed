package com.builtbroken.addictedtored.content;

import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.Tile;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by robert on 3/2/2015.
 */
public abstract class TileAbstractRedstone extends Tile
{
    //TODO switch to byte shifting to save on space
    protected boolean[] canConnect = new boolean[]{true, true, true, true, true, true};

    public TileAbstractRedstone(String name, Material material)
    {
        super(name, material);
        this.canEmmitRedstone = true;
        this.creativeTab = CreativeTabs.tabRedstone;
    }

    public boolean allowRedstone(ForgeDirection out)
    {
        return out == ForgeDirection.UNKNOWN || canConnect[out.ordinal()];
    }

    public void setAllowRedstone(ForgeDirection side, boolean b)
    {
        if (side != ForgeDirection.UNKNOWN)
            canConnect[side.ordinal()] = b;
    }

    public void toggleAllowRedstone(ForgeDirection side)
    {
        setAllowRedstone(side, !allowRedstone(side));
    }

    @Override
    protected boolean onPlayerRightClick(EntityPlayer player, int side, Pos hit)
    {
        if (player.isSneaking())
        {
            toggleAllowRedstone(ForgeDirection.getOrientation(side));
            return true;
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("b0"))
        {
            for (int i = 0; i < 6; i++)
            {
                this.canConnect[i] = nbt.getBoolean("b" + i);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        for (int i = 0; i < 6; i++)
        {
            nbt.setBoolean("b" + i, this.canConnect[i]);
        }
    }
}
