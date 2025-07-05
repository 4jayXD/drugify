package net.drugify.content.drug;

import net.drugify.foundation.drug.Drug;
import net.drugify.foundation.drug.DrugStack;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class EnderineDrug extends Drug
{
    public EnderineDrug(Consumer<Settings.Builder> builderConsumer) {
        super(builderConsumer);
    }

    public void tick(LivingEntity livingEntity, DrugStack drugStack, boolean isPlayer, boolean isClient) {
        
    }

    public void applied(LivingEntity livingEntity, DrugStack drugStack, boolean isPlayer, boolean isClient) {

    }

    public void removed(LivingEntity livingEntity, DrugStack drugStack, boolean isPlayer, boolean isClient) {

    }
}