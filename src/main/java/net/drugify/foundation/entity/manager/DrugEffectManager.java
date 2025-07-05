package net.drugify.foundation.entity.manager;

import net.drugify.foundation.drug.DrugStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public final class DrugEffectManager 
{
    public static DrugEffectManager Get(LivingEntity livingEntity) {
        return ((Implementation) livingEntity).getDrugEffectManager();
    }
    public static interface Implementation {
        DrugEffectManager getDrugEffectManager();
        default Consumption getDrugConsumptionManager() {
            return getDrugEffectManager().consumptionManager;
        }
    }
    
    protected final LivingEntity owner;
    protected final Consumption consumptionManager = new Consumption(this);
    
    public DrugEffectManager(LivingEntity owner) {
        this.owner = owner;
    }
    
    public void readTag(CompoundTag tag) {
        this.consumptionManager.readTag(tag);
    }
    public void writeTag(CompoundTag tag) {
        this.consumptionManager.writeTag(tag);
    }
    
    public void tick() {
        this.consumptionManager.tick();
    }
    
    static class Consumption {
        final DrugEffectManager manager;
        final List<DrugStack.Consumed> consumedStacks = new ArrayList<>();
        
        public Consumption(DrugEffectManager manager) {
            this.manager = manager;
        }
        

        public void readTag(CompoundTag tag) {
            
        }
        public void writeTag(CompoundTag tag) {

        }

        public void tick() {

        }
    }
}