package net.drugify.foundation.drug;

import net.drugify.content.DrugifyDataComponents;
import net.drugify.foundation.drug.component.DrugAdditiveProperties;
import net.drugify.foundation.drug.component.DrugProperties;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DrugStack
{
    private final Holder<Drug> drug;
    private final List<Holder<DrugAdditive>> additives;
    private float count;
    
    public DrugStack(Holder<Drug> drug, List<Holder<DrugAdditive>> additives, float count) {
        this.drug = drug;
        
        this.additives = additives;
        this.count = count;
    }
    
    public boolean canMerge(DrugStack otherStack) {
        return otherStack.drug == drug;
    }
    public boolean containsAdditive(Holder<DrugAdditive> additiveHolder) {

        for (Holder<DrugAdditive> additive : this.additives) {
            if (additive == additiveHolder)
                return true;
        }

        return false;
    }
    
    public Drug getDrug() {
        return drug.value();
    }
        
    public void merge(DrugStack otherStack) {
        if (this.canMerge(otherStack)) {
            otherStack.additives.forEach(additive -> {
                if (!this.containsAdditive(additive))
                    additives.add(additive);
            });
            
            this.grow(otherStack.count);
        }
    }
    public void applied(LivingEntity livingEntity, final boolean isPlayer, final boolean isClient) {

    }
    public void shrink(float amount) {
        this.count -= amount;
    }
    public void grow(float amount) {
        this.count += amount;
    }
    
    public static interface Listener {
        private static List<Listener> getListeners(DrugStack drugStack, boolean additivesOnly) {
            List<Listener> listeners = new ArrayList<>();

            if (!additivesOnly && drugStack.drug.value() instanceof Listener listener)
                listeners.add(listener);

            for (Holder<DrugAdditive> additiveHolder : drugStack.additives) {
                if (additiveHolder instanceof Listener listener)
                    listeners.add(listener);
            }
            
            return listeners;
        }

        void tick(LivingEntity livingEntity, DrugStack drugStack, boolean isPlayer, boolean isClient);
        void applied(LivingEntity livingEntity, DrugStack drugStack, boolean isPlayer, boolean isClient);
        void removed(LivingEntity livingEntity, DrugStack drugStack, boolean isPlayer, boolean isClient);
    }
    public static interface ConsumedListener {
        private static List<ConsumedListener> getListeners(DrugStack drugStack, boolean additivesOnly) {
            List<ConsumedListener> listeners = new ArrayList<>();
            
            if (!additivesOnly && drugStack.drug.value() instanceof ConsumedListener listener)
                listeners.add(listener);
            
            for (Holder<DrugAdditive> additiveHolder : drugStack.additives) {
                if (additiveHolder instanceof ConsumedListener listener)
                    listeners.add(listener);
            }
            
            return listeners;
        } 
        
        void OnConsume(Level level, LivingEntity livingEntity, ItemStack itemStack, Consumed consumedStack, boolean isPlayer, boolean isClient);
        void ConsumedTick(LivingEntity livingEntity, Consumed consumedStack, boolean isPlayer, boolean isClient);
    }
    
    public static final class Consumed {
        public final DrugStack drugStack;
        public final float maxDelay;
        private float delay;
        
        public float getDelay() {
            return delay;
        }
        public float delayTicks() {
            return delay * 20;
        }
        public float maxDelayTicks() {
            return maxDelay * 20;
        }
        
        public void shrinkDelay(float amount) {
            this.delay -= amount;
        }
        public void growDelay(float amount) {
            this.delay += amount;
        }
        
        public void tick(LivingEntity livingEntity) {
            ConsumedListener.getListeners(drugStack, false).forEach(listener -> {
                listener.ConsumedTick(livingEntity, this, livingEntity instanceof Player, livingEntity.level().isClientSide);
            });
        }
        
        public Consumed(DrugStack drugStack, float delay) {
            this.drugStack = drugStack;
            this.maxDelay = delay;
            this.delay = delay;
        }
        
        public void OnConsume(Level level, LivingEntity livingEntity, ItemStack itemStack) {
            ConsumedListener.getListeners(drugStack, false).forEach(listener -> {
                listener.OnConsume(level, livingEntity, itemStack, this, livingEntity instanceof Player, level.isClientSide);
            });
        }

        public boolean canMerge(DrugStack otherStack) {
            return drugStack.canMerge(otherStack);
        }
        public boolean containsAdditive(Holder<DrugAdditive> additiveHolder) {
            return drugStack.containsAdditive(additiveHolder);
        }
    }
}