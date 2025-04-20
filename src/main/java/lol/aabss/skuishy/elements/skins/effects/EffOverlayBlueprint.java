package lol.aabss.skuishy.elements.skins.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.other.Blueprint;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

@Name("Blueprint - Overlay Blueprint")
@Description("Overlays a blueprint on another blueprint.")
@Examples({
        "overlay {_blueprint2} on {_blueprint1}"
})
@Since("2.6")
public class EffOverlayBlueprint extends Effect {

    static {
        Skript.registerEffect(EffOverlayBlueprint.class,
                "(overlay|put) %blueprint% on [top of] %blueprints%"
        );
    }

    private Expression<Blueprint> bottom;
    private Expression<Blueprint> top;

    @Override
    protected void execute(@NotNull Event event) {
        if (bottom != null){
            Blueprint bottom = this.bottom.getSingle(event);
            if (bottom != null){
                for (Blueprint top : this.top.getArray(event)) {
                    top.overlay(bottom);
                }
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "overlay blueprints";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        top = (Expression<Blueprint>) exprs[0];
        bottom = (Expression<Blueprint>) exprs[1];
        return true;
    }
}
