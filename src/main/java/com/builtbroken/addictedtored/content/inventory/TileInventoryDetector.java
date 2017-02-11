package com.builtbroken.addictedtored.content.inventory;

import com.builtbroken.mc.prefab.inventory.filters.IInventoryFilter;

/**
 * Used to detect values inside of inventories
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/19/2017.
 */
public class TileInventoryDetector
{
    /** Should we bypass {@link net.minecraft.inventory.ISidedInventory#getAccessibleSlotsFromSide(int)} */
    boolean bypassISided = false;
    /** What sides should be check of the machine, default will check connected side */
    byte sidesToCheck = 0;
    /** What slots should be check, default is all of accessible */
    int[] slotsToCheck;
    /** What items should be check for */
    IInventoryFilter checkFilter;

    //TODO use multi-threading to check inventories so not to impact main thread performance
    //TODO use same system ICBM uses to update main thread after detecting changes
    //TODO ASM inject update events into some machines to help improve performance and detection
}
