package com.builtbroken.addictedtored.content.voice;

import com.builtbroken.addictedtored.content.TileAbstractRedstone;
import com.builtbroken.mc.prefab.tile.Tile;
import net.minecraft.block.material.Material;

/**
 * Plays sounds effects
 * Created by robert on 3/5/2015.
 */
public class TileSoundEmitter extends TileAbstractRedstone
{
    String sound_name = "mob.villager.haggle";
    float volume = 5;
    float pitch = 1;

    public TileSoundEmitter()
    {
        super("soundEmitter", Material.rock);
    }

    @Override
    public void triggerRedstone()
    {
        if (isClient())
            world().playSound(x() + 0.5, y() + 0.5, z() + 0.5, sound_name, volume, pitch, false);
        else
            sendRedstoneSignalPacket();
    }

    @Override
    public Tile newTile()
    {
        return new TileSoundEmitter();
    }
}