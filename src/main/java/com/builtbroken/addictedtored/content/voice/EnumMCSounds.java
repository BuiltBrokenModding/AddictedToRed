package com.builtbroken.addictedtored.content.voice;

/**
 * Created by robert on 3/6/2015.
 */
public enum EnumMCSounds
{
    //TODO move to core later
    VILLAGER_YES("mob.villager.yes"),
    VILLAGER_NO("mob.villager.no"),
    VILLAGER_HAGGLE("mob.villager.haggle"),
    VILLAGER_IDLE("mob.villager.idle"),
    VILLAGER_HIT("mob.villager.hit"),
    VILLAGER_DEATH("mob.villager.death"),
    CHICKEN_SAY("mob.chicken.say"),
    CHICKEN_HURT("mob.chicken.hurt"),
    CHICKEN_STEP("mob.chicken.step"),
    COW_SAY("mob.cow.say"),
    COW_HURT("mob.cow.hurt"),
    COW_STEP("mob.cow.step"),
    HORSE_LAND("mob.horse.land"),
    HORSE_ARMOR("mob.horse.armor"),
    HORSE_LEATHER("mob.horse.leather"),
    HORSE_ZOMBIE_DEATH("mob.horse.zombie.death"),
    HORSE_SKELETON_DEATH("mob.horse.skeleton.death"),
    HORSE_DEATH("mob.horse.death"),
    DONKEY_DEATH("mob.horse.donkey.death"),
    HORSE_ZOMBIE_HIT("mob.horse.zombie.hit"),
    HORSE_SKELETON_HIT("mob.horse.skeleton.hit"),
    HORSE_HIT("mob.horse.hit"),
    DONKEY_HIT("mob.horse.donkey.hit"),
    HORSE_ZOMBIE_IDLE("mob.horse.zombie.idle"),
    HORSE_SKELETON_IDLE("mob.horse.skeleton.idle"),
    HORSE_IDLE("mob.horse.idle"),
    DONKEY_IDLE("mob.horse.donkey.idle"),
    HORSE_ANGRY("mob.horse.angry"),
    DONKEY_ANGRY("mob.horse.donkey.angry"),
    HORSE_GALLOP("mob.horse.gallop"),
    HORSE_BREATHE("mob.horse.breathe"),
    HORSE_STEP_WOOD("mob.horse.wood"),
    HORSE_STEP_SOFT("mob.horse.soft"),
    PIG_SAY("mob.pig.say"),
    PIG_DEATH("mob.pig.death"),
    PIG_STEP("mob.pig.step"),
    SHEEP_SAY("mob.sheep.say"),
    SHEEP_STEP("mob.sheep.step"),
    SHEEP_SHEAR("mob.sheep.shear"),
    WOLF_STEP("mob.wolf.step"),
    WOLF_GROWL("mob.wolf.growl"),
    WOLF_WHINE("mob.wolf.whine"),
    WOLF_PANTING("mob.wolf.panting"),
    WOLF_BARK("mob.wolf.bark"),
    WOLF_HURT("mob.wolf.hurt"),
    WOLF_DEATH("mob.wolf.death"),
    WOLF_SHAKE("mob.wolf.shake"),
    RANDOM_DRINK("random.drink"),
    RANDOM_EAT("random.eat"),
    RANDOM_POP("random.pop"),
    RANDOM_BOW_HIT("random.bowhit"),
    RANDOM_SPLASH("random.splash"),
    PLAYER_HURT("game.player.hurt"),
    PLAYER_HURT_BIG_FALL("game.player.hurt.fall.big"),
    PLAYER_HURT_SMALL_FALL("game.player.hurt.fall.small"),
    PLAYER_DIE("game.player.die"),
    CREEPER_PRIMED("creeper.primed"),
    CREEPER_HURT("mob.creeper.say"),
    CREEPER_DEATH("mob.creeper.death"),
    ENDERMAN_PORTAL("mob.endermen.portal"),
    ENDERMAN_SCREAM("mob.endermen.scream"),
    ENDERMAN_IDLE("mob.endermen.idle"),
    ENDERMAN_HURT("mob.endermen.hit"),
    ENDERMAN_DEATH("mob.endermen.death"),
    IRON_GOLEM_THROW("mob.irongolem.throw"),
    IRON_GOLEM_HURT("mob.irongolem.hit"),
    IRON_GOLEM_DEATH("mob.irongolem.death"),
    IRON_GOLEM_STEP("mob.irongolem.walk"),
    PIG_ZOMBIE_ANGRY("mob.zombiepig.zpigangry"),
    PIG_ZOMBIE_SAY("mob.zombiepig.zpig"),
    PIG_ZOMBIE_HURT("mob.zombiepig.zpighurt"),
    PIG_ZOMBIE_DEATH("mob.zombiepig.zpigdeath"),
    SILVER_FISH_STEP("mob.silverfish.step"),
    SILVER_FISH_SAY("mob.silverfish.say"),
    SILVER_FISH_HURT("mob.silverfish.say"),
    SILVER_FISH_DEATH("mob.silverfish.kill");
    //TODO add skeleton, slime, snowman, spider, zombie, wither, ender dragon, lava, fire, and random sounds



    public final String name;
    public final float volume;
    public final float pitch;

    private EnumMCSounds(String name)
    {
        this(name, 1.0f, 1.0f);
    }

    private EnumMCSounds(String name, float volume, float pitch)
    {
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }
}
