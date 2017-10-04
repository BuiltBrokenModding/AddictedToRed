package com.builtbroken.addictedtored.content.voice;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.IWorldPosition;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by robert on 3/6/2015.
 */
public enum EnumMCSounds
{
    //TODO move to core later
    VILLAGER_YES("mob.villager.yes", "villager", "yes"),
    VILLAGER_NO("mob.villager.no", "villager", "no"),
    VILLAGER_HAGGLE("mob.villager.haggle", "villager", "haggle"),
    VILLAGER_IDLE("mob.villager.idle", "villager", "idle"),
    VILLAGER_HIT("mob.villager.hit", "villager", "hit"),
    VILLAGER_DEATH("mob.villager.death", "villager", "death"),
    CHICKEN_SAY("mob.chicken.say", "chicken", "say"),
    CHICKEN_HURT("mob.chicken.hurt", "chicken", "hurt"),
    CHICKEN_STEP("mob.chicken.step", "chicken", "step"),
    COW_SAY("mob.cow.say", "cow", "say"),
    COW_HURT("mob.cow.hurt", "cow", "hurt"),
    COW_STEP("mob.cow.step", "cow", "step"),
    HORSE_LAND("mob.horse.land", "horse", "land"),
    HORSE_ARMOR("mob.horse.armor", "horse", "armor"),
    HORSE_LEATHER("mob.horse.leather", "horse", "saddle"),
    HORSE_ZOMBIE_DEATH("mob.horse.zombie.death", "zombie horse", "death"),
    HORSE_SKELETON_DEATH("mob.horse.skeleton.death", "skeleton horse", "death"),
    HORSE_DEATH("mob.horse.death", "horse", "death"),
    DONKEY_DEATH("mob.horse.donkey.death", "donkey", "death"),
    HORSE_ZOMBIE_HIT("mob.horse.zombie.hit", "zombie horse", "hit"),
    HORSE_SKELETON_HIT("mob.horse.skeleton.hit", "skeleton horse", "hit"),
    HORSE_HIT("mob.horse.hit", "horse", "hit"),
    DONKEY_HIT("mob.horse.donkey.hit", "donkey", "death"),
    HORSE_ZOMBIE_IDLE("mob.horse.zombie.idle", "zombie horse", "idle"),
    HORSE_SKELETON_IDLE("mob.horse.skeleton.idle", "skeleton horse", "idle"),
    HORSE_IDLE("mob.horse.idle", "horse", "idle"),
    DONKEY_IDLE("mob.horse.donkey.idle", "donkey", "idle"),
    HORSE_ANGRY("mob.horse.angry", "horse", "angry"),
    DONKEY_ANGRY("mob.horse.donkey.angry", "donkey", "angry"),
    HORSE_GALLOP("mob.horse.gallop", "horse", "gallop"),
    HORSE_BREATHE("mob.horse.breathe", "horse", "breathe"),
    HORSE_STEP_WOOD("mob.horse.wood", "horse", "step wood"),
    HORSE_STEP_SOFT("mob.horse.soft", "horse", "step soft"),
    PIG_SAY("mob.pig.say", "pig", "say"),
    PIG_DEATH("mob.pig.death", "pig", "death"),
    PIG_STEP("mob.pig.step", "pig", "step"),
    SHEEP_SAY("mob.sheep.say", "sheep", "say"),
    SHEEP_STEP("mob.sheep.step", "sheep", "step"),
    SHEEP_SHEAR("mob.sheep.shear", "sheep", "shear"),
    WOLF_STEP("mob.wolf.step", "wolf", "step"),
    WOLF_GROWL("mob.wolf.growl", "wolf", "growl"),
    WOLF_WHINE("mob.wolf.whine", "wolf", "whine"),
    WOLF_PANTING("mob.wolf.panting", "wolf", "panting"),
    WOLF_BARK("mob.wolf.bark", "wolf", "bark"),
    WOLF_HURT("mob.wolf.hurt", "wolf", "hurt"),
    WOLF_DEATH("mob.wolf.death", "wolf", "death"),
    WOLF_SHAKE("mob.wolf.shake", "wolf", "shake"),
    RANDOM_DRINK("random.drink", "random", "drink"),
    RANDOM_EAT("random.eat", "random", "eat"),
    RANDOM_POP("random.pop", "random", "pop"),
    RANDOM_BOW_HIT("random.bowhit", "random", "bow hit"),
    RANDOM_SPLASH("random.splash", "random", "splash"),
    PLAYER_HURT("game.player.hurt", "player", "hurt"),
    PLAYER_HURT_BIG_FALL("game.player.hurt.fall.big", "player", "big fall"),
    PLAYER_HURT_SMALL_FALL("game.player.hurt.fall.small", "player", "small fall"),
    PLAYER_DIE("game.player.die", "player", "death"),
    CREEPER_PRIMED("creeper.primed", "creeper", "primed"),
    CREEPER_HURT("mob.creeper.say", "creeper", "hurt"),
    CREEPER_DEATH("mob.creeper.death", "creeper", "death"),
    ENDERMAN_PORTAL("mob.endermen.portal", "enderman", "portal"),
    ENDERMAN_SCREAM("mob.endermen.scream", "enderman", "scream"),
    ENDERMAN_IDLE("mob.endermen.idle", "enderman", "idle"),
    ENDERMAN_HURT("mob.endermen.hit", "enderman", "hit"),
    ENDERMAN_DEATH("mob.endermen.death", "enderman", "death"),
    IRON_GOLEM_THROW("mob.irongolem.throw", "iron golem", "throw"),
    IRON_GOLEM_HURT("mob.irongolem.hit", "iron golem", ""),
    IRON_GOLEM_DEATH("mob.irongolem.death", "iron golem", "death"),
    IRON_GOLEM_STEP("mob.irongolem.walk", "iron golem", "step"),
    PIG_ZOMBIE_ANGRY("mob.zombiepig.zpigangry", "pig zombie", "angry"),
    PIG_ZOMBIE_SAY("mob.zombiepig.zpig", "pig zombie", "say"),
    PIG_ZOMBIE_HURT("mob.zombiepig.zpighurt", "pig zombie", "hurt"),
    PIG_ZOMBIE_DEATH("mob.zombiepig.zpigdeath", "pig zombie", "death"),
    SILVER_FISH_STEP("mob.silverfish.step", "silver fish", "step"),
    SILVER_FISH_SAY("mob.silverfish.say", "silver fish", "say"),
    SILVER_FISH_HURT("mob.silverfish.say", "silver fish", "hurt"),
    SILVER_FISH_DEATH("mob.silverfish.kill", "silver fish", "death");
    //TODO add skeleton, slime, snowman, spider, zombie, wither, ender dragon, lava, fire, and random sounds


    public final String sound;
    public final String cat;
    public final String name;
    public final float volume;
    public final float pitch;

    private static boolean built = false;

    //List of sounds by category for sorting in GUIs <Category, <Name, Sound String or Enum Entry>>
    public static final HashMap<String, HashMap<String, Object>> catToSoundMap = new HashMap();
    private static boolean init = false;

    EnumMCSounds(String sound, String cat, String name)
    {
        this(sound, cat, name, 1.0f, 1.0f);
    }

    EnumMCSounds(String sound, String cat, String name, float volume, float pitch)
    {
        this.sound = sound;
        this.cat = cat;
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static void init()
    {
        if (!init)
        {
            init = true;
            for (EnumMCSounds entry : values())
            {
                if (!catToSoundMap.containsKey(entry.cat))
                {
                    catToSoundMap.put(entry.cat, new HashMap());
                }
                catToSoundMap.get(entry.cat).put(entry.name, entry);
            }
        }
    }

    public static void playSound(String cat, String name, IWorldPosition pos, float volume, float pitch)
    {
        if (catToSoundMap.containsKey(cat) && catToSoundMap.get(cat) != null)
        {
            if (catToSoundMap.get(cat).containsKey(name))
            {
                Object obj = catToSoundMap.get(cat).get(name);
                if (obj instanceof EnumMCSounds)
                {
                    ((EnumMCSounds) obj).playSound(pos.oldWorld(), pos.x(), pos.y(), pos.z(), volume, pitch);
                }
                else if (obj instanceof String)
                {
                    pos.oldWorld().playSound(pos.x(), pos.y(), pos.z(), (String) obj, volume, pitch, true);
                }
            }
        }
    }

    public void playSound(IWorldPosition pos)
    {
        playSound(pos.oldWorld(), pos.x(), pos.y(), pos.z(), volume, pitch);
    }

    public void playSound(World world, IPos3D pos)
    {
        playSound(world, pos.x(), pos.y(), pos.z(), volume, pitch);
    }

    public void playSound(World world, double x, double y, double z)
    {
        playSound(world, x, y, z, volume, pitch);
    }

    public void playSound(World world, double x, double y, double z, float volume, float pitch)
    {
        playSound(world, x, y, z, volume, pitch);
    }

    public void playSound(World world, double x, double y, double z, float volume, float pitch, boolean delay)
    {
        world.playSound(x, y, z, sound, volume, pitch, delay);
    }
}
