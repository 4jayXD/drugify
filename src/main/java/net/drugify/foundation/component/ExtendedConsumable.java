package net.drugify.foundation.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public final record ExtendedConsumable(int MaxUsage, int colour, ItemUseAnimation animation, Holder<Item> WasteItem)
{
    public static final Codec<ExtendedConsumable> CODEC;
    
    public InteractionResult Consume(Level level, LivingEntity livingEntity, ItemStack itemStack) {

        if (itemStack.getItem() instanceof Listener listener)
            listener.ConsumingTick(level, livingEntity, itemStack, this);

        itemStack.getAllOfType(Listener.class).forEach(listener -> {
            listener.ConsumingTick(level, livingEntity, itemStack, this);
        });
        
        return InteractionResult.FAIL;
    }
    
    public ItemStack OnConsume(Level level, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.getItem() instanceof Listener listener)
            listener.onConsume(level, livingEntity, itemStack, this);
        
        itemStack.getAllOfType(Listener.class).forEach(listener -> {
            listener.onConsume(level, livingEntity, itemStack, this);
        });
        
        if (WasteItem.value() == Items.AIR) {
            itemStack.shrink(1);
            return itemStack;
        }
        
        return itemStack;
    }
    
    public boolean infiniteUsage() {
        return MaxUsage <= 0;
    }
    
    public static final class Builder {
        
        private Holder<Item> WasteItem;
        
        Builder() {
            WasteItem = BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR);
        }
        
        
    }
    
    public static interface Listener {
        void onConsume(Level level, LivingEntity livingEntity, ItemStack itemStack, ExtendedConsumable extendedConsumable);
        default void ConsumingTick(Level level, LivingEntity livingEntity, ItemStack itemStack, ExtendedConsumable extendedConsumable) {
        }
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    Codec.INT.fieldOf("uses").forGetter(ExtendedConsumable::MaxUsage),
                    Codec.INT.fieldOf("color").forGetter(ExtendedConsumable::MaxUsage),
                    ItemUseAnimation.CODEC.fieldOf("animation").forGetter(ExtendedConsumable::animation),
                    Item.CODEC.fieldOf("waste_item").forGetter(ExtendedConsumable::WasteItem)
            ).apply(instance, ExtendedConsumable::new);
        });
    }
}