package net.drugify.content;

import net.drugify.Drugify;
import net.drugify.content.drug.EnderineDrug;
import net.drugify.foundation.DrugifyRegistries;
import net.drugify.foundation.drug.Drug;
import net.minecraft.core.Holder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class Drugs 
{
    private static final DeferredRegister<Drug> DRUGS;
    
    public static final Holder<Drug> Enderine;
    
    private static Holder<Drug> register(String name, Drug drug) {
        return DRUGS.register(name, () -> drug);
    }
    
    static {
        DRUGS = DeferredRegister.create(DrugifyRegistries.DRUG, Drugify.MODID);
        
        Enderine = register("enderine", new EnderineDrug(settings -> {
            settings.colour(0x008080);
            settings.additive(DrugAdditives.HYDROPHOBIA);
        }));

        register("test", new EnderineDrug(settings -> {
            settings.colour(0x008080);
            settings.additive(DrugAdditives.HYDROPHOBIA);
        }));
        register("test_1", new EnderineDrug(settings -> {
            settings.colour(0x008080);
            settings.additive(DrugAdditives.HYDROPHOBIA);
        }));
    }
    
    public static void Init(IEventBus bus) {
        DRUGS.register(bus);
    }
}