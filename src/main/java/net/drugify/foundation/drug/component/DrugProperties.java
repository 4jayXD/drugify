package net.drugify.foundation.drug.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.drugify.Drugify;
import net.drugify.content.DrugifyDataComponents;
import net.drugify.foundation.component.ExtendedConsumable;
import net.drugify.foundation.drug.Drug;
import net.drugify.foundation.drug.DrugAdditive;
import net.drugify.foundation.drug.DrugStack;
import net.drugify.foundation.entity.manager.DrugEffectManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final record DrugProperties(List<Holder<Drug>> drugs) implements ConsumableListener, ExtendedConsumable.Listener
{
    public static final Codec<DrugProperties> CODEC;
    
    private DrugAdditiveProperties additiveProperties(ItemStack itemStack) {
        return itemStack.get(DrugifyDataComponents.DRUG_ADDITIVES);
    }
    private Drug.Quality getQuality(ItemStack itemStack) {
        return itemStack.has(DrugifyDataComponents.QUALITY) ? itemStack.get(DrugifyDataComponents.QUALITY) : Drug.Quality.STANDARD;
    }
    
    public void BuildTooltip(Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag flag, ItemStack itemStack) {
        if (this.drugs.size() > 1)
            tooltipComponents.add(Component.translatable(Drugify.MODID+".tooltip.drugs"));
        
        for (Holder<Drug> drug : this.drugs) {
            drug.value().BuildTooltip(context, tooltipComponents, this.drugs.size());
        }
        
        if (!itemStack.has(DrugifyDataComponents.DRUG_ADDITIVES)) {
            List<Holder<DrugAdditive>> additives = new ArrayList<>();
            
            this.drugs.forEach(drugHolder -> {
                additives.addAll(drugHolder.value().getAdditives());
            });
            
            if (additives.size() > 1)
                tooltipComponents.add(Component.translatable(Drugify.MODID+".tooltip.drug_additives"));
            
            additives.forEach(additiveHolder -> {
                additiveHolder.value().BuildTooltip(context, tooltipComponents, additives.size());
            });
        }
    }
    
    public void onConsume(Level level, LivingEntity livingEntity, ItemStack itemStack, Consumable consumable) {
    }
    public void onConsume(Level level, LivingEntity livingEntity, ItemStack itemStack, ExtendedConsumable extendedConsumable) {
    }
    
    private void onConsume(Level level, LivingEntity livingEntity, ItemStack itemStack, Consumer<DrugStack.Consumed> conumedStackConsumer) {
        
    }
    
    private void createStacks(Level level, LivingEntity livingEntity, ItemStack itemStack, double count, double delay, Function<DrugStack.Consumed, Boolean> consumedConsumer) {
        final double split = count / drugs.size();
        DrugAdditiveProperties additiveProperties = itemStack.get(DrugifyDataComponents.DRUG_ADDITIVES);
        
        for (Holder<Drug> drugHolder : this.drugs) {
            List<Holder<DrugAdditive>> additives = drugHolder.value().getAdditives();
            
            if (additiveProperties != null) {
                additives.addAll(additiveProperties.additives());
            }
            
            DrugStack.Consumed consumedStack = new DrugStack.Consumed(new DrugStack(drugHolder, additives, (float) split), (float) delay);
            boolean result = consumedConsumer.apply(consumedStack);
            if (result) {
                
            }
        }
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(Drug.CODEC.listOf().fieldOf("drugs").forGetter(DrugProperties::drugs)).apply(instance, DrugProperties::new);
        });
    }
}