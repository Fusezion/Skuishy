package lol.aabss.skuishy.elements.skins.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.other.Blueprint;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

@Name("Blueprint - Clone Blueprint")
@Description("Clones a blueprint.")
@Examples({
        "clone {_blueprint} and store it in {_blueprint2}"
})
@Since("2.6")
public class EffCloneBlueprint extends Effect {

    static {
        Skript.registerEffect(EffCloneBlueprint.class,
                "(clone|duplicate) %blueprint% (and store it|stored) in %object%"
        );
    }

    private Expression<Blueprint> print;
    private Variable<?> var;

    @Override
    protected void execute(@NotNull Event event) {
        if (print != null){
            Blueprint print1 = this.print.getSingle(event);
            if (print1 != null){
                var.change(event, new Blueprint[]{print1.duplicate()}, Changer.ChangeMode.SET);
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "overlay blueprints";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (exprs[1] instanceof Variable<?>) {
            print = (Expression<Blueprint>) exprs[0];
            var = (Variable<?>) exprs[1];
            return true;
        }
        Skript.error("Object must be a variable!");
        return false;
    }
}
