package net.drugify.foundation.drug;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.drugify.Drugify;
import net.drugify.foundation.DrugifyRegistries;
import net.drugify.foundation.drug.component.DrugAdditiveProperties;
import net.drugify.foundation.drug.component.DrugProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public abstract class Drug implements DrugStack.Listener
{
    public static final Codec<Holder<Drug>> CODEC;
    
    protected final Settings settings;
    
    public Drug(Consumer<Settings.Builder> builderConsumer) {
        Settings.Builder builder = new Settings.Builder();
        builderConsumer.accept(builder);
        
        this.settings = builder.build();
    }
    
    protected ResourceLocation getLocation() {
        return DrugifyRegistries.DRUG.getKey(this);
    }
    protected Holder<Drug> getHolder() {
        return DrugifyRegistries.DRUG.wrapAsHolder(this);
    }
    public Component getDisplayText() {
        final ResourceLocation location = getLocation();
        return Component.translatable(location.getNamespace()+".drug."+location.getPath()).withColor(settings.colour);
    }
    public Settings getSettings() {
        return settings;
    }
    
    public List<Holder<DrugAdditive>> getAdditives() {
        return settings.additives;
    }
    
    public void BuildTooltip(Item.TooltipContext context, List<Component> Tooltip, final int count) {
        
        final Component tooltipLabel = count > 1 ? Component.translatable(Drugify.MODID+".tooltip.drug.list") : Component.translatable(Drugify.MODID+".tooltip.drug");
        Tooltip.add(tooltipLabel.copy().append(getDisplayText()));
    }
    
    public static class Settings {
        
        public final int colour;
        public final List<Holder<DrugAdditive>> additives;
        
        protected Settings(int colour, List<Holder<DrugAdditive>> additives) {
            this.colour = colour;
            this.additives = additives;
        }
        
        public static class Builder {
            
            private int colour;
            private List<Holder<DrugAdditive>> additives;
            
            public Builder() {
                additives = new ArrayList<>();
                colour = ChatFormatting.WHITE.getColor();
            }
            
            protected Settings build() {
                return new Settings(colour, additives);
            }
            
            public Builder colour(int colour) {
                this.colour = colour;
                return this;
            }
            public Builder colour(ChatFormatting chatColour) {
                return colour(chatColour.getColor());
            }
            
            public Builder additive(Holder<DrugAdditive> additive) {
                this.additives.add(additive);
                return this;
            }
            public Builder additives(Supplier<List<Holder<DrugAdditive>>> additives) {
                this.additives.addAll(additives.get());
                return this;
            }
        }
    }
    
    static {
        CODEC = DrugifyRegistries.DRUG.holderByNameCodec();
    }
    
    public static enum ConsumptionMethod implements StringRepresentable {
        INJECT("inject", 1, 1, false, null, null),
        SMOKE("smoke", 1, 1, false, null, null),
        SNORT("snort", 1, 1, true, null, null);

        public static final Codec<ConsumptionMethod> CODEC;
        
        public final String name;
        public final float consumptionSeconds, consumptionAmount;
        public final boolean deleteOnConsumed;
        public final Holder<SoundEvent> consumedSound, consumingSound;
        
        private ConsumptionMethod(String name, double consumptionSeconds, double consumptionAmount, boolean deleteOnConsumed, Holder<SoundEvent> consumedSound, Holder<SoundEvent> consumingSound) {
            this.name = name;
            this.consumptionSeconds = (float) consumptionSeconds;
            this.consumptionAmount = (float)  consumptionAmount;
            this.deleteOnConsumed = deleteOnConsumed;
            this.consumedSound = consumedSound;
            this.consumingSound = consumingSound;
        }
        
        public String getSerializedName() {
            return name;
        }
        
        public Component getText() {
            return Component.translatable(Drugify.MODID+".consumption_method."+name);
        }
        
        static {
            CODEC = StringRepresentable.fromValues(ConsumptionMethod::values);
        }
    }
    public static enum Quality implements StringRepresentable {
        TRASH("trash", ChatFormatting.DARK_GREEN, .5, .5, 0),
        STANDARD("standard", ChatFormatting.BLUE, 1, 1, 1),
        GOOD("good", ChatFormatting.LIGHT_PURPLE,1.5, 1.25, 2),
        AMAZING("amazing", ChatFormatting.YELLOW, 1.75, 1.75, 3);
        
        public static final Codec<Quality> CODEC;
        public static final IntFunction<Quality> BY_ID;
        public static final StreamCodec<ByteBuf, Quality> STREAM_CODEC;
        public static final Component Tooltip;
        
        public final String name;
        public final float timeMultiplier;
        public final float strengthMultiplier;
        public final int colour;
        public final int id;
        
        private Quality(String name, ChatFormatting colour, double timeMultiplier, double strengthMultiplier, int id) {
            this.name = name;
            this.timeMultiplier = (float) timeMultiplier;
            this.strengthMultiplier = (float) strengthMultiplier;
            this.colour = colour.getColor();
            this.id = id;
        }
        private Quality(String name, int colour, double timeMultiplier, double strengthMultiplier, int id) {
            this.name = name;
            this.timeMultiplier = (float) timeMultiplier;
            this.strengthMultiplier = (float) strengthMultiplier;
            this.colour = colour;
            this.id = id;
        }
        
        public String getSerializedName() {
            return name;
        }

        public Component GetDisplayName(ItemStack stack) {
            
            Component itemName = stack.get(DataComponents.ITEM_NAME);
            
            return Component.translatable(Drugify.MODID+".quality."+name+" ").withColor(colour).append(itemName);
        }
        public Component GetTextComponent() {
            return Component.translatable("drugify.tooltip.quality."+name);
        }
        public void BuildTooltip(Item.TooltipContext context, List<Component> tooltipComponents) {
            tooltipComponents.add(Drug.Quality.Tooltip.copy().append(GetTextComponent().copy().withColor(colour)));
        }
        
        static {
            CODEC = StringRepresentable.fromValues(Quality::values);
            BY_ID = ByIdMap.continuous(ins -> {
                return ins.id;
            }, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
            STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ins -> {
                return ins.id;
            });
            
            Tooltip = Component.translatable(Drugify.MODID+".tooltip.quality");
        }
    }
}