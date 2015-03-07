package com.builtbroken.addictedtored.content.voice;

import com.builtbroken.addictedtored.content.Tier;
import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.prefab.gui.GuiContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.util.Set;

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

    GuiButton sound_cat_button;
    GuiButton sound_name_button;

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
        if (machine.tier == Tier.ADVANCED)
        {
            this.sound_field = newField(x, y, 100, "");
            this.sound_field.setMaxStringLength(2000);
            this.sound_field.setText("" + machine.getSoundName());
            this.buttonList.add(new GuiButton(2, x + 115, y, 40, 20, "Update"));
        }
        else
        {
            sound_cat_button = new GuiButton(3, x, y, 60, 20, "" + machine.sound_cat);
            this.buttonList.add(sound_cat_button);
            sound_name_button = new GuiButton(4, x + 62, y, 60, 20, "" + machine.sound_name);
            this.buttonList.add(sound_name_button);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        try
        {
            //Update button
            if (button.id == 0 || button.id == 1)
            {

                if (button.id == 0)
                {
                    machine.setVolume(Float.parseFloat(volume_field.getText()));
                }
                else
                {
                    machine.setPitch(Float.parseFloat(pitch_field.getText()));
                }

            }
            else if (button.id == 2)
            {
                machine.setSoundName(sound_field.getText());
            }
            else if (button.id == 3)
            {
                String[] keys = EnumMCSounds.catToSoundMap.keySet().toArray(new String[EnumMCSounds.catToSoundMap.keySet().size()]);
                int i = 0;
                for (; i < keys.length; i++)
                {
                    if (keys[i] != null && keys[i].equalsIgnoreCase(machine.sound_cat))
                    {
                        break;
                    }
                }

                i += 1;
                if (i >= keys.length)
                {
                    i = 0;
                }

                String[] names = EnumMCSounds.catToSoundMap.get(keys[i]).keySet().toArray(new String[EnumMCSounds.catToSoundMap.get(keys[i]).keySet().size()]);
                sound_cat_button.displayString = keys[i];
                sound_name_button.displayString = names[0];
                machine.setSound(keys[i], names[0]);
            }
            else if (button.id == 4)
            {
                Set<String> set = EnumMCSounds.catToSoundMap.get(machine.sound_cat).keySet();
                String[] names = set.toArray(new String[set.size()]);
                int i = 0;
                for (; i < names.length; i++)
                {
                    if (names[i] != null && names[i].equalsIgnoreCase(machine.sound_name))
                    {
                        break;
                    }
                }

                i += 1;
                if (i >= names.length)
                {
                    i = 0;
                }

                machine.setSound(machine.sound_cat, names[i]);
                sound_name_button.displayString = names[i];
            }
        }
        catch (NumberFormatException e)
        {
            errorString = "Invalid data";
        }
        catch (Exception e)
        {
            errorString = "Error " + e.getMessage();
            e.printStackTrace();
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
