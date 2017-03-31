package com.builtbroken.addictedtored.content.chat.detector;

import com.builtbroken.addictedtored.content.TileAbstractRedstone;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.prefab.tile.Tile;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Will pick up on chat spoken on the server or within range of the block. Can be configured to
 * catch the chat locally so it doesn't go into server chat.
 * Created by robert on 3/20/2015.
 */
public class TileChatDetector extends TileAbstractRedstone
{
    //Settings for how to match the chat message
    public boolean detectSameDimOnly = true;
    public boolean detectLocalMsgOnly = true;
    public boolean detectUsername = true;
    public boolean hideChatMsg = false;
    public MatchType matchType = MatchType.CONTAINS;

    //Device settings
    public String chatMessage = "Hello";
    public String errorMessage = "";

    public boolean output_red = false;

    public List<String> users = new ArrayList();

    public double range = 5;
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
            target = new Pos((TileEntity)this).add(0.5);
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
        if(errorMessage != "")
            return;
        try
        {
            //Check if username matches
            if (!detectUsername || users.contains(event.username.toLowerCase()))
            {
                //Check if dim matches
                if (!detectSameDimOnly || world() == event.player.worldObj)
                {
                    //Check if range is good
                    if (!detectLocalMsgOnly || new Pos(event.player).distance(target) <= range)
                    {
                        boolean match = false;

                        //Find if the message matches
                        if (matchType == MatchType.CONTAINS)
                            match = event.message.contains(chatMessage);
                        else if (matchType == MatchType.START)
                            match = event.message.startsWith(chatMessage);
                        else if (matchType == MatchType.END)
                            match = event.message.endsWith(chatMessage);
                        else if (matchType == MatchType.REGEX)
                            match = Pattern.compile(chatMessage).matcher(event.message).find();

                        //If match do action
                        if (match)
                        {
                            output_red = true;
                            if (hideChatMsg)
                                event.setCanceled(true);
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            //TODO send packet
            errorMessage = e.getClass().getSimpleName() + " " + e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public Tile newTile()
    {
        return new TileChatDetector();
    }

    public enum MatchType
    {
        START, CONTAINS, END, REGEX
    }
}
