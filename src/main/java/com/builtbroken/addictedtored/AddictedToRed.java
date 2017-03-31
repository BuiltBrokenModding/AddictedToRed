package com.builtbroken.addictedtored;

import com.builtbroken.addictedtored.content.OCProxy;
import com.builtbroken.addictedtored.content.chat.emitter.TileChatBlock;
import com.builtbroken.addictedtored.content.detector.entity.TileEntityDetector;
import com.builtbroken.addictedtored.content.detector.selection.TileSelectionDetector;
import com.builtbroken.addictedtored.content.voice.EnumMCSounds;
import com.builtbroken.addictedtored.content.voice.TileSoundEmitter;
import com.builtbroken.mc.lib.mod.AbstractMod;
import com.builtbroken.mc.lib.mod.ModCreativeTab;
import com.builtbroken.mc.lib.mod.Mods;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;

/**
 * Created by robert on 11/18/2014.
 */
@Mod(modid = AddictedToRed.DOMAIN, name = AddictedToRed.NAME, version = AddictedToRed.VERSION, dependencies = "required-after:VoltzEngine;after:OpenComputers")
public final class AddictedToRed extends AbstractMod
{
    /**
     * Name of the channel and mod ID.
     */
    public static final String NAME = "Addicted To Red";
    public static final String DOMAIN = "addictedtored";
    public static final String PREFIX = DOMAIN + ":";

    /**
     * The version of AddictedToRed.
     */
    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;

    public static final String ASSETS_PATH = "/assets/addictedtored/";
    public static final String TEXTURE_PATH = "textures/";
    public static final String GUI_PATH = TEXTURE_PATH + "gui/";
    public static final String MODEL_PREFIX = "models/";
    public static final String MODEL_DIRECTORY = ASSETS_PATH + MODEL_PREFIX;

    public static final String MODEL_TEXTURE_PATH = TEXTURE_PATH + MODEL_PREFIX;
    public static final String BLOCK_PATH = TEXTURE_PATH + "blocks/";
    public static final String ITEM_PATH = TEXTURE_PATH + "items/";

    @Mod.Instance(DOMAIN)
    public static AddictedToRed INSTANCE;

    @SidedProxy(clientSide = "com.builtbroken.addictedtored.ClientProxy", serverSide = "com.builtbroken.addictedtored.CommonProxy")
    public static CommonProxy proxy;

    public static ModCreativeTab CREATIVE_TAB;

    public static Block basicDetector;
    public static Block selectionDetector;
    public static Block chatBlock;
    public static Block soundBlock;

    public AddictedToRed()
    {
        super(DOMAIN, "AddictedToRed");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        loader.applyModule(OCProxy.class, Mods.OC.isLoaded());
        //CREATIVE_TAB = new ModCreativeTab("addictedtored");
        //getManager().setTab(CREATIVE_TAB);

        basicDetector = getManager().newBlock(DOMAIN + "entitydetector", TileEntityDetector.class);
        selectionDetector = getManager().newBlock(DOMAIN + "selectiondetector", TileSelectionDetector.class);
        chatBlock = getManager().newBlock(DOMAIN + "chatblock", TileChatBlock.class);
        soundBlock = getManager().newBlock(DOMAIN + "soundBlock", TileSoundEmitter.class);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        EnumMCSounds.init();
    }

    @Override
    public CommonProxy getProxy()
    {
        return proxy;
    }
}
