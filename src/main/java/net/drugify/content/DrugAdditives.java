package net.drugify.content;

import net.drugify.Drugify;
import net.drugify.content.drug.additives.HydrophobiaAdditive;
import net.drugify.foundation.DrugifyRegistries;
import net.drugify.foundation.drug.DrugAdditive;
import net.minecraft.core.Holder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class DrugAdditives 
{
    private static final DeferredRegister<DrugAdditive> DRUG_ADDITIVES;
    
    public static final Holder<DrugAdditive> HYDROPHOBIA;
    
    private static <T extends DrugAdditive> DeferredHolder<DrugAdditive, T> register(String name, T drug) {
        return DRUG_ADDITIVES.register(name, () -> drug);
    }
    
    static {
        DRUG_ADDITIVES = DeferredRegister.create(DrugifyRegistries.DRUG_ADDITIVE, Drugify.MODID);
        
        HYDROPHOBIA = register("hydrophobia", new HydrophobiaAdditive(settings -> {
            settings.colour(0x009C9C);
        }));
    }
    
    public static void Init(IEventBus bus) {
        DRUG_ADDITIVES.register(bus);
    }
}