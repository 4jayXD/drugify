package net.drugify.foundation.drug.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.drugify.Drugify;
import net.drugify.content.DrugifyDataComponents;
import net.drugify.foundation.drug.DrugAdditive;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final record DrugAdditiveProperties(List<Holder<DrugAdditive>> additives)
{
    public static final Codec<DrugAdditiveProperties> CODEC;
    
    public static DrugAdditiveProperties Create(Consumer<List<Holder<DrugAdditive>>> additivesConsumer) {
        List<Holder<DrugAdditive>> additives = new ArrayList<>();
        additivesConsumer.accept(additives);
        return new DrugAdditiveProperties(additives);
    }
    
    public DrugAdditiveProperties Combine(List<Holder<DrugAdditive>> additives) {
        List<Holder<DrugAdditive>> addatives = this.additives;
        
        for (Holder<DrugAdditive> additiveHolder : additives) {
            if (!this.Contains(additiveHolder)) {
                addatives.add(additiveHolder);
            }
        }
        
        return new DrugAdditiveProperties(addatives);
    }
    
    public DrugAdditiveProperties Add(Holder<DrugAdditive> additive) {
        List<Holder<DrugAdditive>> addatives = this.additives;
        
        if (!this.Contains(additive))
            addatives.add(additive);
        
        return new DrugAdditiveProperties(additives);
    }
    
    public boolean Contains(Holder<DrugAdditive> additive) {
        
        for (Holder<DrugAdditive> drugAdditiveHolder : this.additives) {
             if (drugAdditiveHolder == additive)
                 return true;
        }
        
        return false;
    }
    
    public void BuildTooltip(Item.TooltipContext context, List<Component> tooltipComponents, ItemStack itemStack) {

        List<Holder<DrugAdditive>> additives = this.additives;
        
        
    }
    
    static {
        
        CODEC = RecordCodecBuilder.create(instance -> {
           return instance.group(DrugAdditive.CODEC.listOf().fieldOf("additives").forGetter(DrugAdditiveProperties::additives)).apply(instance, DrugAdditiveProperties::new); 
        });
    }
}