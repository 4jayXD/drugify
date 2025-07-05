package net.drugify.mixin.world.item;

import net.drugify.content.DrugifyDataComponents;
import net.drugify.foundation.component.ExtendedConsumable;
import net.drugify.foundation.drug.Drug;
import net.drugify.foundation.drug.component.DrugAdditiveProperties;
import net.drugify.foundation.drug.component.DrugProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin implements FeatureElement, ItemLike, IItemExtension
{
    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void drugify$finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> info) {
        ExtendedConsumable extendedConsumable = stack.get(DrugifyDataComponents.EXTENDED_CONSUMABLE);
        if (extendedConsumable != null)
            info.setReturnValue(extendedConsumable.OnConsume(level, livingEntity, stack));
    }
    
    @Inject(method = "appendHoverText", at = @At("RETURN"))
    public void drugify$appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo info) {
        Drug.Quality quality = stack.get(DrugifyDataComponents.QUALITY);
        DrugProperties drugsProperties = stack.get(DrugifyDataComponents.DRUG);
        DrugAdditiveProperties additiveComponent = stack.get(DrugifyDataComponents.DRUG_ADDITIVES);
        
        if (quality != null) {
            quality.BuildTooltip(context, tooltipComponents);
        }
        
        if (drugsProperties != null) {
            drugsProperties.BuildTooltip(context, tooltipComponents, tooltipFlag, stack);
        }
        
        if (additiveComponent != null) {
            additiveComponent.BuildTooltip(context, tooltipComponents, stack);
        }
    }
}