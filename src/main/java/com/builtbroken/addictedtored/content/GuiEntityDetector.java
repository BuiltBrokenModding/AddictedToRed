package com.builtbroken.addictedtored.content;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.transform.vector.Pos;
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
        //TODO hide target if basic tier
        this.x_field = newField(x, y, 30, "" + machine.target.xi());
        this.y_field = newField(x + 35, y, 30, "" + machine.target.yi());
        this.z_field = newField(x + 70, y, 30, "" + machine.target.zi());
        this.buttonList.add(new GuiButton(0, x + 115, y, 40, 20, "Update"));

        y += 35;
        this.rx_field = newField(x, y, 30, "" + machine.range.xi());
        this.ry_field = newField(x + 35, y, 30, "" + machine.range.yi());
        this.rz_field = newField(x + 70, y, 30, "" + machine.range.zi());

        this.buttonList.add(new GuiButton(1, x + 115, y, 40, 20, "Update"));
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
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //TODO localize
        drawStringCentered("Entity Detector", 85, 10);
        drawStringCentered("Target", 30, 30);
        drawStringCentered("Range", 30, 65);
        drawStringCentered(errorString, 85, 80, Colors.RED.color);
    }
}
