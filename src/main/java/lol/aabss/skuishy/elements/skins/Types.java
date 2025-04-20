package lol.aabss.skuishy.elements.skins;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import lol.aabss.skuishy.other.Blueprint;
import lol.aabss.skuishy.other.EnumWrapper;
import org.jetbrains.annotations.NotNull;
import org.mineskin.data.Variant;

public class Types {

    static {
        if (Classes.getClassInfoNoError("blueprint") == null) {
            Classes.registerClass(new ClassInfo<>(Blueprint.class, "blueprint")
                    .user("blueprints?")
                    .name("Blueprint - Blueprint")
                    .description("Represents a blueprint.")
                    .since("2.6")
                    .parser(new Parser<>() {

                        @Override
                        public boolean canParse(@NotNull ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toVariableNameString(Blueprint blueprint) {
                            return blueprint.model().getName() + " blueprint";
                        }

                        @Override
                        public @NotNull String toString(Blueprint blueprint, int flags) {
                            return toVariableNameString(blueprint);
                        }
                    })
            );
        }
        if (Classes.getClassInfoNoError("skinmodel") == null) {
            Classes.registerClass(new EnumWrapper<>(Variant.class).getClassInfo("skinmodel")
                    .user("skin ?models?")
                    .name("Skin - Skin Model")
                    .description("Represents a skin model.")
                    .since("2.6"));
        }
        if (Classes.getClassInfoNoError("blueprintpart") == null) {
            Classes.registerClass(new EnumWrapper<>(Blueprint.Part.class).getClassInfo("blueprintpart")
                    .user("blueprint ?parts?")
                    .name("Skin - Blueprint Part")
                    .description("Represents a part of a blueprint.")
                    .since("2.8"));
        }
    }

}
