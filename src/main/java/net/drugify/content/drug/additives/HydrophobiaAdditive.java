package net.drugify.content.drug.additives;

import net.drugify.foundation.drug.DrugAdditive;
import net.drugify.foundation.drug.DrugStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class HydrophobiaAdditive extends DrugAdditive implements DrugStack.ConsumedListener
{
    public HydrophobiaAdditive(Consumer<Settings.Builder> builderConsumer) {
        super(builderConsumer);
    }


    public void OnConsume(Level level, LivingEntity livingEntity, ItemStack itemStack, DrugStack.Consumed consumedStack, boolean isPlayer, boolean isClient) {
        
    }

    public void ConsumedTick(LivingEntity livingEntity, DrugStack.Consumed consumedStack, boolean isPlayer, boolean isClient) {
        
    }

    public void tick(LivingEntity livingEntity, DrugStack drugStack, boolean isPlayer, boolean isClient) {

    }

    public void applied(LivingEntity livingEntity, DrugStack drugStack, boolean isPlayer, boolean isClient) {

    }

    public void removed(LivingEntity livingEntity, DrugStack drugStack, boolean isPlayer, boolean isClient) {

    }
}