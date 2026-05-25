package com.example.randomizer.mixin;

import com.example.randomizer.RandomizerMod;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.mixin.overwrite.Overwrite;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeSource.class)
public class BiomeSourceMixin {

    @Inject(method = "getBiome", at = @At("RETURN"), cancellable = true)
    private void injectRandomBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noiseSampler, CallbackInfoReturnable<RegistryEntry<Biome>> cir) {
        RegistryEntry<Biome> originalBiome = cir.getReturnValue();
        // Swap the returned biome with our shuffled counterpart
        cir.setReturnValue(RandomizerMod.getShuffledBiome(originalBiome));
    }
}
