package com.builtbroken.addictedtored.content.detector.entity;

import com.builtbroken.addictedtored.content.Tier;
import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import com.builtbroken.mc.prefab.gui.GuiContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

/**
 * Created by robert on 2/20/2015.
 */
public class GuiEntityDetector extends GuiContainerBase
{
    protected TileEntityDetector machine;

    protected GuiTextField x_field;
    protected GuiTextField y_field;
    protected GuiTextField z_field;
    protected GuiTextField rx_field;
    protected GuiTextField ry_field;
    protected GuiTextField rz_field;
    protected String errorString = "";

    public GuiEntityDetector(TileEntityDetector launcher, EntityPlayer player)
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
        if(machine.tier != Tier.BASIC)
        {
            if(machine.tier == Tier.ADVANCED)
            {
                this.x_field = newField(x, y, 30, "" + machine.target.xi());
                this.y_field = newField(x + 35, y, 30, "" + machine.target.yi());
                this.z_field = newField(x + 70, y, 30, "" + machine.target.zi());
                this.buttonList.add(new GuiButton(0, x + 115, y, 40, 20, "Update"));
                y += 35;
            }
            this.rx_field = newField(x, y, 30, "" + machine.range.xi());
            this.ry_field = newField(x + 35, y, 30, "" + machine.range.yi());
            this.rz_field = newField(x + 70, y, 30, "" + machine.range.zi());
            this.buttonList.add(new GuiButton(1, x + 115, y, 40, 20, "Update"));
            y += 40;
        }

        this.buttonList.add(new GuiButton(2, x, y, 40, 20, "<"));
        this.buttonList.add(new GuiButton(3, x + 115, y, 40, 20, ">"));
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
                if (button.id == 0)
                {
                    Pos target = new Pos(Integer.parseInt(x_field.getText()), Integer.parseInt(y_field.getText()), Integer.parseInt(z_field.getText()));
                    machine.setTarget(target);
                }
                else
                {
                    Pos range = new Pos(Integer.parseInt(rx_field.getText()), Integer.parseInt(ry_field.getText()), Integer.parseInt(rz_field.getText()));
                    machine.setRange(range);
                }
            }
            catch (NumberFormatException e)
            {
                //Ignore as this is expected
                errorString = "Invalid data";
            }
        }
        else if(button.id == 2)
        {
            int next = machine.selector.ordinal() - 1;
            if(next < 0)
                next = EntitySelectors.values().length - 1;
            machine.setSelector(EntitySelectors.get(next));
        }
        else if(button.id == 3)
        {
            int next = machine.selector.ordinal() + 1;
            if(next >= EntitySelectors.values().length)
                next = 0;
            machine.setSelector(EntitySelectors.get(next));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //TODO localize
        drawStringCentered("Entity Detector", 85, 10);
        int y = 30;
        if(machine.tier != Tier.BASIC)
        {
            drawStringCentered("Target", 30, y);
            y += 35;
            if(machine.tier == Tier.ADVANCED)
            {
                drawStringCentered("Range", 30, y);
                y += 35;
            }
        }
        drawStringCentered("Selector", 30, y);
        drawStringCentered("" + machine.selector.displayName(), 85, y + 20);
        drawStringCentered(errorString, 85, 80, Colors.RED.color);
    }
}
