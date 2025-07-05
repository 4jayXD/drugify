package net.drugify.foundation.drug;

import com.mojang.serialization.Codec;
import net.drugify.Drugify;
import net.drugify.foundation.DrugifyRegistries;
import net.drugify.foundation.drug.component.DrugAdditiveProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class DrugAdditive implements DrugStack.Listener 
{
    public static final Codec<Holder<DrugAdditive>> CODEC;
    protected final Settings settings;
    public DrugAdditive(Consumer<Settings.Builder> builderConsumer) {
        this.settings = Settings.create(builderConsumer);
    }

    public ResourceLocation getLocation() {
        return DrugifyRegistries.DRUG_ADDITIVE.getKey(this);
    }
    
    public void BuildTooltip(Item.TooltipContext context, List<Component> tooltipComponents, final int count) {
        
        final Component TooltipLabel = count > 1 ? Component.literal(Drugify.MODID+".tooltip.drug_additive.list") : Component.translatable(Drugify.MODID+".tooltip.drug_additive");
        
        tooltipComponents.add(TooltipLabel.copy().append(getDisplayName()));
    }
    
    public Component getDisplayName() {
        ResourceLocation location = getLocation();
        return Component.translatable(location.getNamespace()+".drug_additive."+location.getPath()).withColor(settings.colour);
    }
    public Settings getSettings() {
        return settings;
    }
    
    public static class Settings {
        
        public final int colour;
        public final Function<DrugStack, ParticleOptions> particleFactory;
        public final TriConsumer<DrugStack, ParticleOptions, LivingEntity> particleEmmition;
        
        
        protected Settings(int colour, Function<DrugStack, ParticleOptions> particleFactory, TriConsumer<DrugStack, ParticleOptions, LivingEntity> particleEmmition) {
            this.colour = colour;
            this.particleFactory = particleFactory;
            this.particleEmmition = particleEmmition;
        }
        
        protected static Builder create() {
            return new Builder();
        }
        protected static Settings create(Consumer<Builder> builderConsumer) {
            Builder builder = Settings.create();
            builderConsumer.accept(builder);
            return builder.build();
        }
        
        public static class Builder {
            protected Settings build() {
                return new Settings(this.colour, this.particleFactory, this.particleEmmition);
            }
            
            private int colour;
            private Function<DrugStack, ParticleOptions> particleFactory;
            private TriConsumer<DrugStack, ParticleOptions, LivingEntity> particleEmmition;
            
            Builder() {
                this.colour = ChatFormatting.WHITE.getColor();
            }
            
            public Builder colour(int colour) {
                this.colour = colour;
                return this;
            }
            public Builder colour(ChatFormatting colour) {
                return colour(colour.getColor());
            }
            
            public Builder particleFactory(Function<DrugStack, ParticleOptions> particleFactory) {
                this.particleFactory = particleFactory;
                return this;
            }
            public Builder particleEmmition(TriConsumer<DrugStack, ParticleOptions, LivingEntity> particleEmmition) {
                this.particleEmmition = particleEmmition;
                return this;
            }
        }
    }

    public void OnConsume(Level level, LivingEntity livingEntity, ItemStack itemStack, final boolean isPlayer, final boolean isClient) {
    }

    public void applied(LivingEntity livingEntity, final boolean isPlayer, final boolean isClient) {
    }
    public void removed(LivingEntity livingEntity, final boolean isPlayer, final boolean isClient) {
    }
    public void tick(LivingEntity livingEntity, final boolean isPlayer, final boolean isClient) {
    }
    
    static {
        CODEC = DrugifyRegistries.DRUG_ADDITIVE.holderByNameCodec();
    }
}