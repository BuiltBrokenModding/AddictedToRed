package com.builtbroken.addictedtored.content.chat;

import com.builtbroken.addictedtored.content.TileAbstractRedstone;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.Tile;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Will pick up on chat spoken on the server or within range of the block. Can be configured to
 * catch the chat locally so it doesn't go into server chat.
 * Created by robert on 3/20/2015.
 */
public class TileChatDetector extends TileAbstractRedstone
{
    /**
     * Detect chat only in the same dim as the detector
     */
    public boolean sameDim = true;
    /**
     * Hide matching chat messages
     */
    public boolean hideMsg = false;
    /**
     * Detect chat only in a set range
     */
    public boolean localMsg = true;

    /**
     * Detects a set list of user names
     */
    public boolean detectUsername = true;

    public String chatMessage = "Hello";

    public List<String> users = new ArrayList();

    public Pos range = new Pos(5, 5, 5);
    public Pos target;

    public TileChatDetector()
    {
        super("chatDetector", Material.rock);
        this.firstTick();
    }

    @Override
    public void firstTick()
    {
        super.firstTick();
        if (target == null || !target.isAboveBedrock())
            target = new Pos(this).add(0.5);
        MinecraftForge.EVENT_BUS.register(this);
        this.invalidate();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onChatEvent(ServerChatEvent event)
    {
        if(!detectUsername || users.contains(event.username.toLowerCase()))
        {
            
        }
    }

    @Override
    public Tile newTile()
    {
        return new TileChatDetector();
    }
}
