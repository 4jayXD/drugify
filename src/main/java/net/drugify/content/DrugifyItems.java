package net.drugify.content;

import net.drugify.Drugify;
import net.drugify.foundation.drug.component.DrugAdditiveProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public final class DrugifyItems 
{
    private static final DeferredRegister.Items ITEMS;
    public static final DeferredItem<Item> JOINT, SYRINGE;
    
    public static DeferredItem<Item> register(String name, Function<Item.Properties, ? extends Item> itemFunction) {
        return ITEMS.registerItem(name, itemFunction);
    }
    
    static {
        ITEMS = DeferredRegister.createItems(Drugify.MODID);
        
        JOINT = register("joint", Item::new);
        SYRINGE = register("syringe", Item::new);
        
        register("test", properties -> {
            
            properties.component(DrugifyDataComponents.DRUG_ADDITIVES, DrugAdditiveProperties.Create(additives -> additives.add(DrugAdditives.HYDROPHOBIA)));
            
            return new Item(properties);
        });
    }
    
    public static void Init(IEventBus bus) {
        ITEMS.register(bus);
    }
}   