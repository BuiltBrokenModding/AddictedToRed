package com.builtbroken.addictedtored.content.voice;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.prefab.gui.GuiContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

/**
 * Created by robert on 2/20/2015.
 */
public class GuiSoundBlock extends GuiContainerBase
{
    protected TileSoundEmitter machine;

    protected GuiTextField volume_field;
    protected GuiTextField pitch_field;
    protected GuiTextField sound_field;
    protected String errorString = "";

    public GuiSoundBlock(TileSoundEmitter launcher, EntityPlayer player)
    {
        this.machine = launcher;
        this.baseTexture = References.GUI__MC_EMPTY_FILE;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int x = guiLeft + 10;
        int y = guiTop + 40;
        this.volume_field = newField(x, y, 30, "" + machine.getVolume());
        this.buttonList.add(new GuiButton(0, x + 115, y, 40, 20, "Update"));
        y += 35;
        this.pitch_field = newField(x, y, 30, "" + machine.getPitch());
        this.buttonList.add(new GuiButton(1, x + 115, y, 40, 20, "Update"));
        y += 40;
        this.sound_field = newField(x, y, 100, "");
        this.sound_field.setMaxStringLength(2000);
        this.sound_field.setText("" + machine.getSoundName());
        this.buttonList.add(new GuiButton(2, x + 115, y, 40, 20, "Update"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);

        //Update button
        if (button.id == 0 || button.id == 1)
        {
            try
            {
                if(button.id == 0)
                {
                    machine.setVolume(Float.parseFloat(volume_field.getText()));
                }
                else
                {
                    machine.setPitch(Float.parseFloat(pitch_field.getText()));
                }
            } catch (NumberFormatException e)
            {
                //Ignore as this is expected
                errorString = "Invalid data";
            }
        }
        else if(button.id == 2)
        {
            machine.setSoundName(sound_field.getText());
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //TODO localize
        drawStringCentered("Entity Detector", 85, 10);
        int y = 30;
        drawStringCentered("Volume", 30, y);
        y += 35;
        drawStringCentered("Pitch", 30, y);
        y += 35;
        drawStringCentered("Sound Name", 35, y);
        drawStringCentered(errorString, 85, 80, Colors.RED.color);
    }
}
