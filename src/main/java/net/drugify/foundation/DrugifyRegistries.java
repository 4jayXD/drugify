package net.drugify.foundation;

import net.drugify.Drugify;
import net.drugify.foundation.drug.Drug;
import net.drugify.foundation.drug.DrugAdditive;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class DrugifyRegistries 
{
    public static final class Keys {
        public static final ResourceKey<Registry<Drug>> DRUG;
        public static final ResourceKey<Registry<DrugAdditive>> DRUG_ADDITIVE;
        
        static {
            DRUG = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Drugify.MODID, "drugs"));
            DRUG_ADDITIVE = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Drugify.MODID, "drug_additive"));
        }
    }
    
    public static final Registry<Drug> DRUG;
    public static final Registry<DrugAdditive> DRUG_ADDITIVE;
    
    static {
        DRUG = new RegistryBuilder<>(Keys.DRUG).sync(true).create();
        DRUG_ADDITIVE = new RegistryBuilder<>(Keys.DRUG_ADDITIVE).sync(true).create();
    }
    
    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(DRUG);
        event.register(DRUG_ADDITIVE);
    }
}