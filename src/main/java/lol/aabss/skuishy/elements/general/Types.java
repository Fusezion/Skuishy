package lol.aabss.skuishy.elements.general;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import io.papermc.paper.datapack.Datapack;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lol.aabss.skuishy.Skuishy;
import lol.aabss.skuishy.other.EnumWrapper;
import lol.aabss.skuishy.other.RegistryClassInfo;
import org.bukkit.Art;
import org.bukkit.Rotation;
import org.bukkit.Statistic;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class Types {
    static{
        if (Skript.classExists("io.papermc.paper.datapack.Datapack") && Classes.getExactClassInfo(Datapack.class) == null) {
            Classes.registerClass(new ClassInfo<>(Datapack.class, "datapack")
                    .user("data ?packs?")
                    .name("Datapack")
                    .description("Represents a datapack.")
                    .since("1.7")
                    .parser(new Parser<>() {

                        @Override
                        public boolean canParse(@NotNull ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toVariableNameString(Datapack data) {
                            return data.getName().toLowerCase().replaceAll("_", " ");
                        }

                        @Override
                        public @NotNull String toString(Datapack data, int flags) {
                            return toVariableNameString(data);
                        }
                    })
            );
        }
        if (Skript.classExists("org.bukkit.entity.SpawnCategory") && Classes.getExactClassInfo(SpawnCategory.class) == null) {
            Classes.registerClass(new EnumWrapper<>(SpawnCategory.class).getClassInfo("spawncategory")
                    .user("spawn ?categor(y|ies)")
                    .name("Spawn Category")
                    .description("Represents a spawn category.")
                    .since("1.7.5")
            );
        }

        if (Skript.classExists("org.bukkit.potion.PotionType") && Classes.getExactClassInfo(PotionType.class) == null) {
            Classes.registerClass(new EnumWrapper<>(PotionType.class).getClassInfo("potionitemtype")
                    .user("potion[ ]item[ ]type")
                    .name("Potion Item Type")
                    .description("Represents a potion item type that matches each potion state that can be obtained from the Creative mode inventory.")
                    .since("2.1")
            );
        }
        if (Skript.classExists("org.bukkit.Statistic") && Classes.getExactClassInfo(Statistic.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Statistic.class).getClassInfo("statistic")
                    .user("statistics?")
                    .name("Statistic")
                    .description("Represents a statistic of a player.")
                    .since("2.8")
            );
        }
        if (Skript.classExists("org.bukkit.event.world.TimeSkinEvent.SkipReason") && Classes.getExactClassInfo(TimeSkipEvent.SkipReason.class) == null) {
            Classes.registerClass(new EnumWrapper<>(TimeSkipEvent.SkipReason.class).getClassInfo("skipreason")
                    .user("skip ?reasons?")
                    .name("Time Skip Reason")
                    .description("Represents a reason for a time skip.")
                    .since("2.8")
            );
        }

        if (Skript.classExists("org.bukkit.Art") && Classes.getExactClassInfo(Art.class) == null) {
            ClassInfo<?> art_class_info = null;
            if (Skript.classExists("io.papermc.paper.registry.RegistryKey") && Skript.fieldExists(RegistryKey.class, "PAINTING_VARIANT")) {
                RegistryAccess registryAccess = RegistryAccess.registryAccess();
                art_class_info = RegistryClassInfo.create(registryAccess.getRegistry(RegistryKey.PAINTING_VARIANT), Art.class, "art");
            } else if (Art.class.isEnum()) {
                try {
                    Class<?> art_class = Class.forName("org.bukkit.Art");
                    // noinspection unchecked,rawtypes
                    Class<? extends Enum> art_class_enum = (Class<? extends Enum>) art_class;
                    // noinspection unchecked,rawtypes
                    art_class_info = new EnumWrapper<>(art_class_enum).getClassInfo("art");
                } catch (ClassNotFoundException exception) {
                    Skuishy.Logger.exception(exception);
                }
            }
            if (art_class_info != null) {
                Classes.registerClass(art_class_info
                        .user("arts?")
                        .name("Art")
                        .description("Represents a piece of art.")
                        .since("2.8")
                );
            }
        }

        if (Skript.classExists("org.bukkit.Rotation") && Classes.getExactClassInfo(Rotation.class) == null) {
            Classes.registerClass(new EnumWrapper<>(Rotation.class).getClassInfo("rotation")
                    .user("rotations?")
                    .name("Rotation")
                    .description("Represents a rotation.")
                    .since("2.8")
            );
        }

    }
}
