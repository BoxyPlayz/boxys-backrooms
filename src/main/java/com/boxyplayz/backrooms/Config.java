package com.boxyplayz.backrooms;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue NETHER_ENTRANCE = BUILDER
            .comment("Have a chance to go to the backrooms when crossing dimensions")
            .define("netherRooms", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}