package net.drugify.content;

import net.drugify.Drugify;
import net.drugify.foundation.drug.Drug;
import net.drugify.foundation.drug.component.DrugAdditiveProperties;
import net.drugify.foundation.drug.component.DrugProperties;
import net.drugify.foundation.component.ExtendedConsumable;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public final class DrugifyDataComponents 
{
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS;

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ExtendedConsumable>> EXTENDED_CONSUMABLE;
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Drug.Quality>> QUALITY;
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DrugProperties>> DRUG;
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DrugAdditiveProperties>> DRUG_ADDITIVES;
    
    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }
    
    static {
        DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Drugify.MODID);
        
        EXTENDED_CONSUMABLE = register("extended_consumable", b -> {
            return b.persistent(ExtendedConsumable.CODEC);
        });
        
        QUALITY = register("quality", b -> {
            return b.persistent(Drug.Quality.CODEC).networkSynchronized(Drug.Quality.STREAM_CODEC);
        });
        
        DRUG = register("drug_properties", b -> {
            return b.persistent(DrugProperties.CODEC);
        });
        
        DRUG_ADDITIVES = register("drug_additives", b -> {
            return b.persistent(DrugAdditiveProperties.CODEC);
        });
    }
    
    public static void Init(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}