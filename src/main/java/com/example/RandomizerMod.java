package com.example.randomizer;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;
import java.util.*;

public class RandomizerMod implements ModInitializer {
    public static final String MOD_ID = "world_randomizer";

    private static final Map<RegistryEntry<Biome>, RegistryEntry<Biome>> BIOME_SHUFFLE_MAP = new HashMap<>();
    private static final Map<RegistryEntry<Structure>, RegistryEntry<Structure>> STRUCTURE_SHUFFLE_MAP = new HashMap<>();
    private static final List<RegistryEntry<Biome>> DISCOVERED_BIOMES = new ArrayList<>();
    private static final List<RegistryEntry<Structure>> DISCOVERED_STRUCTURES = new ArrayList<>();

    @Override
    public void onInitialize() {
        System.out.println("World Randomizer Loaded for 26.1.2!");
    }

    // Dynamically maps biomes on the fly as the game requests them
    public static RegistryEntry<Biome> getShuffledBiome(RegistryEntry<Biome> original) {
        if (!DISCOVERED_BIOMES.contains(original)) {
            DISCOVERED_BIOMES.add(original);
            
            // Re-shuffle mapping every time a new biome is discovered
            List<RegistryEntry<Biome>> shuffled = new ArrayList<>(DISCOVERED_BIOMES);
            Collections.shuffle(shuffled, new Random(42)); 
            
            for (int i = 0; i < DISCOVERED_BIOMES.size(); i++) {
                BIOME_SHUFFLE_MAP.put(DISCOVERED_BIOMES.get(i), shuffled.get(i));
            }
        }
        return BIOME_SHUFFLE_MAP.getOrDefault(original, original);
    }

    // Dynamically maps structures, ensuring Nether Fortresses don't get trapped in the End
    public static RegistryEntry<Structure> getShuffledStructure(RegistryEntry<Structure> original) {
        if (!DISCOVERED_STRUCTURES.contains(original)) {
            DISCOVERED_STRUCTURES.add(original);
            
            List<RegistryEntry<Structure>> shuffled = new ArrayList<>(DISCOVERED_STRUCTURES);
            Collections.shuffle(shuffled, new Random(101));

            // Protection Logic: Ensure Nether Fortress isn't mapped to an End-only structure slot
            for (int i = 0; i < DISCOVERED_STRUCTURES.size(); i++) {
                RegistryEntry<Structure> orig = DISCOVERED_STRUCTURES.get(i);
                RegistryEntry<Structure> target = shuffled.get(i);

                // If a nether fortress is about to be sent to the End, swap it with another slot
                if (orig.getKey().isPresent() && orig.getKey().get().getValue().getPath().contains("fortress")) {
                    if (target.getKey().isPresent() && target.getKey().get().getValue().getPath().contains("end_city")) {
                        // Simple fallback: keep it as itself or swap with index 0 to keep it out of the End
                        Collections.swap(shuffled, i, 0);
                    }
                }
            }

            for (int i = 0; i < DISCOVERED_STRUCTURES.size(); i++) {
                STRUCTURE_SHUFFLE_MAP.put(DISCOVERED_STRUCTURES.get(i), shuffled.get(i));
            }
        }
        return STRUCTURE_SHUFFLE_MAP.getOrDefault(original, original);
    }
}
