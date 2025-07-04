package com.boxyplayz.backrooms;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BoxysBackrooms.MODID)
public class BoxysBackrooms {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "backrooms";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger ();
    // Create a Deferred Register to hold Blocks which will all be registered under the "backrooms" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks (MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "backrooms" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems (MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "backrooms" namespace
    public static final DeferredRegister <CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create (Registries.CREATIVE_MODE_TAB , MODID);

    // Creates a new Block with the id "backrooms:error_block", combining the namespace and path
    public static final DeferredBlock <Block> ERROR_BLOCK = BLOCKS.registerSimpleBlock ("error_block" , BlockBehaviour.Properties.of ().mapColor (MapColor.STONE));
    // Creates a new BlockItem with the id "backrooms:ERROR_BLOCK", combining the namespace and path
    public static final DeferredItem <BlockItem> ERROR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem ("error_block" , ERROR_BLOCK);

    // Creates a new Block with the id "backrooms:error_block", combining the namespace and path
    public static final DeferredBlock <Block> WAY_BACK = BLOCKS.registerSimpleBlock ("the_way_back" , BlockBehaviour.Properties.of ().mapColor (MapColor.STONE));
    // Creates a new BlockItem with the id "backrooms:ERROR_BLOCK", combining the namespace and path
    public static final DeferredItem <BlockItem> WAY_BACK_ITEM = ITEMS.registerSimpleBlockItem ("the_way_back" , WAY_BACK);

    // Creates a new food item with the id "backrooms:example_id", nutrition 1 and saturation 2
    public static final DeferredItem <Item> ALMOND_WATER = ITEMS.registerSimpleItem ("almond_water" , new Item.Properties ().food (new FoodProperties.Builder ().nutrition (14).saturationModifier (6f).effect (() -> new MobEffectInstance (MobEffects.REGENERATION , 500 , 1) , 1.0f).build ()));

    // Creates a creative tab with the id "backrooms:creative_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder <CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register ("creative_tab" , () -> CreativeModeTab.builder ().title (Component.translatable ("itemGroup.backrooms")) //The language key for the title of your CreativeModeTab
            .withTabsBefore (CreativeModeTabs.COMBAT).icon (() -> WAY_BACK_ITEM.get ().getDefaultInstance ()).displayItems ((parameters , output) -> {
                output.accept (ALMOND_WATER.get ());
                output.accept (ERROR_BLOCK.get ());
                output.accept (WAY_BACK);
            }).build ());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public BoxysBackrooms(IEventBus modEventBus , ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener (this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register (modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register (modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register (modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (BoxysBackrooms) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register (this);

        // Register the item to a creative tab
        modEventBus.addListener (this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig (ModConfig.Type.COMMON , Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info ("HELLO FROM COMMON SETUP");
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        //event.accept (ALMOND_WATER.get ());
        //event.accept (ERROR_BLOCK_ITEM.get ());
        //event.accept (WAY_BACK_ITEM.get ());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info ("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info ("HELLO FROM CLIENT SETUP");
            LOGGER.info ("MINECRAFT NAME >> {}" , Minecraft.getInstance ().getUser ().getName ());
        }
    }
}
