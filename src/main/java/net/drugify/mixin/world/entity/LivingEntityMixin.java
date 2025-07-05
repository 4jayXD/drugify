package net.drugify.mixin.world.entity;

import net.drugify.foundation.entity.manager.DrugEffectManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ILivingEntityExtension, DrugEffectManager.Implementation
{
    protected final DrugEffectManager drugConsumptionManager = new DrugEffectManager((LivingEntity)(Entity)this);
    
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "tick", at = @At("RETURN"))
    public void drugify$tick(CallbackInfo info) {
        this.drugConsumptionManager.tick();
    }
    
    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void drugify$readCompoundTag(CompoundTag compound, CallbackInfo info) {
        this.drugConsumptionManager.readTag(compound);
    }
    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void drugify$writeCompoundTag(CompoundTag compound, CallbackInfo info) {
        this.drugConsumptionManager.writeTag(compound);
    }
    
    public DrugEffectManager getDrugEffectManager() {
        return drugConsumptionManager;
    }
}