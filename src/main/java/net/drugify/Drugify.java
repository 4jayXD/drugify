package net.drugify;

import com.mojang.logging.LogUtils;
import net.drugify.content.DrugAdditives;
import net.drugify.content.DrugifyItems;
import net.drugify.content.Drugs;
import net.drugify.foundation.DrugifyRegistries;
import net.drugify.content.DrugifyDataComponents;
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
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Drugify.MODID)
public final class Drugify {
    public static final String MODID = "drugify";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Drugify(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        modEventBus.register(DrugifyRegistries.class);

        DrugifyDataComponents.Init(modEventBus);
        
        Drugs.Init(modEventBus);
        DrugAdditives.Init(modEventBus);
        DrugifyItems.Init(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
 
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
       
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        
    }
    
    
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            
        }
    }
}
