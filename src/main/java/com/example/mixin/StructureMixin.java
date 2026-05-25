package com.example.randomizer.mixin;

import com.example.randomizer.RandomizerMod;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Structure.class)
public class StructureMixin {

    @ModifyVariable(method = "postPlace", at = @At("HEAD"), argsOnly = true)
    private static RegistryEntry<Structure> injectRandomStructure(RegistryEntry<Structure> original) {
        // Intercepts the structure generation reference and swaps it
        return RandomizerMod.getShuffledStructure(original);
    }
}
