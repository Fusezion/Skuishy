package lol.aabss.skuishy.elements.entities;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lol.aabss.skuishy.other.EnumWrapper;
import lol.aabss.skuishy.other.RegistryClassInfo;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sniffer;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Warden;

public class Types {
    static {
        if (Skript.classExists("org.bukkit.entity.Axolotl") && Classes.getExactClassInfo(Axolotl.Variant.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Axolotl.Variant.class).getClassInfo("axolotlvariant")
                    .user("axolotl ?variants?")
                    .name("Axolotl Variant")
                    .description("Represents a axolotl variant.")
                    .since("2.8")
                    .documentationId("AxolotlVariant"));
        }

        if (Skript.classExists("org.bukkit.entity.Rabbit") && Classes.getExactClassInfo(Rabbit.Type.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Rabbit.Type.class).getClassInfo("rabbitvariant")
                    .user("rabbit ?variants?")
                    .name("Rabbit Variant")
                    .description("Represents a rabbit variant.")
                    .since("2.8"));
        }

        if (Skript.classExists("org.bukkit.entity.Llama") && Classes.getExactClassInfo(Llama.Color.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Llama.Color.class).getClassInfo("llamavariant")
                    .user("llama ?variants?")
                    .name("Llama Variant")
                    .description("Represents a llama variant.")
                    .since("2.8")
                    .documentationId("TypeLlamaColor"));
        }

        if (Skript.classExists("org.bukkit.entity.Fox") && Classes.getExactClassInfo(Fox.Type.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Fox.Type.class).getClassInfo("foxvariant")
                    .user("fox ?variants?")
                    .name("Fox Variant")
                    .description("Represents a fox variant.")
                    .since("2.8"));
        }

        if (Skript.classExists("org.bukkit.entity.Boat") && Classes.getExactClassInfo(Boat.Type.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Boat.Type.class).getClassInfo("boatvariant")
                    .user("boat ?variants?")
                    .name("Boat Variant")
                    .description("Represents a boat variant.")
                    .since("2.8"));
        }

        if (Skript.classExists("org.bukkit.entity.Boat") && Classes.getExactClassInfo(Boat.Status.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Boat.Status.class).getClassInfo("boatstatus")
                    .user("boat ?status[es]")
                    .name("Boat Status")
                    .description("Represents a boat Status.")
                    .since("2.8"));
        }

        if (Skript.classExists("org.bukkit.entity.EnderDragon") && Classes.getExactClassInfo(EnderDragon.Phase.class) == null) {
            Classes.registerClass(new EnumWrapper<>(EnderDragon.Phase.class).getClassInfo("dragonphase")
                    .user("[ender ?]dragon ?phase[s]")
                    .name("Dragon Phase")
                    .description("Represents the phase of a dragon.")
                    .since("2.8"));
        }

        if (Skript.classExists("org.bukkit.entity.Frog") && Classes.getExactClassInfo(Frog.Variant.class) == null) {
            if (Skript.classExists("io.papermc.paper.registry.RegistryAccess") && Skript.classExists("io.papermc.paper.registry.RegistryKey"))
                Classes.registerClass(RegistryClassInfo.create(RegistryAccess.registryAccess().getRegistry(RegistryKey.FROG_VARIANT),
                            Frog.Variant.class, "frogvariant")
                    .user("frog ?variants?")
                    .name("Frog Variant")
                    .description("Represents a variant of a frog.")
                    .since("2.8")
                    .documentationId("TypeFrogVariant"));
        }

        if (Skript.classExists("org.bukkit.entity.Horse") && Classes.getExactClassInfo(Horse.Color.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Horse.Color.class).getClassInfo("horsecolor")
                    .user("horse ?colors?")
                    .name("Horse Color")
                    .description("Represents a color of a horse.")
                    .since("2.8")
                    .documentationId("TypeHorseColor"));
        }

        if (Skript.classExists("org.bukkit.entity.Horse") && Classes.getExactClassInfo(Horse.Style.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Horse.Style.class).getClassInfo("horsestyle")
                    .user("horse ?styles?")
                    .name("Horse Style")
                    .description("Represents a style of a horse.")
                    .since("2.8"));
        }

        if (Skript.classExists("org.bukkit.entity.MushroomCow") && Classes.getExactClassInfo(MushroomCow.Variant.class) == null) {
            Classes.registerClass(new EnumWrapper<>(MushroomCow.Variant.class).getClassInfo("mushroomcowvariant")
                    .user("mushroom ?cow ?variants?")
                    .name("Mushroom Cow Variant")
                    .description("Represents a variant of a mushroom cow.")
                    .since("2.8")
                    .documentationId("TypeMushroomCowVariant"));
        }

        if (Skript.classExists("org.bukkit.entity.Parrot") && Classes.getExactClassInfo(Parrot.Variant.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Parrot.Variant.class).getClassInfo("parrotvariant")
                    .user("parrot ?variants?")
                    .name("Parrot Variant")
                    .description("Represents a variant of a parrot.")
                    .since("2.8")
                    .documentationId("TypeParrotVariant"));
        }

        if (Skript.classExists("org.bukkit.entity.Sniffer") && Classes.getExactClassInfo(Sniffer.State.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Sniffer.State.class).getClassInfo("snifferstate")
                    .user("sniffer ?states?")
                    .name("Sniffer States")
                    .description("Represents a state of a sniffer.")
                    .since("2.8"));
        }

        if (Skript.classExists("org.bukkit.entity.TropicalFish") && Classes.getExactClassInfo(TropicalFish.Pattern.class) == null) {
            Classes.registerClass(new EnumWrapper<>(TropicalFish.Pattern.class).getClassInfo("tropicalfishpattern")
                    .user("tropical ?fish ?patterns?")
                    .name("Tropical Fish Pattern")
                    .description("Represents a pattern of a tropical fish.")
                    .since("2.8"));
        }

        if (Skript.classExists("org.bukkit.entity.Warden") && Classes.getExactClassInfo(Warden.AngerLevel.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Warden.AngerLevel.class).getClassInfo("angerlevel")
                    .user("anger ?levels?")
                    .name("Anger Level")
                    .description("Represents an anger level.")
                    .since("2.8"));
        }
    }
}
