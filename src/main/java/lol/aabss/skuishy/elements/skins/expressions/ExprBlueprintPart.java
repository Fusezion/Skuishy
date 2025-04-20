package lol.aabss.skuishy.elements.skins.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.other.Blueprint;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Name("Blueprint - Blueprint Part")
@Description("Gets a part of a blueprint.")
@Examples({
        "set {_head} to head of {_blueprint}",
        "set {_torso} to torso of {_blueprint}",
        "set {_leftarm} to left arm without the outer layer of {_blueprint}",
})
@Since("2.6")
public class ExprBlueprintPart extends PropertyExpression<Blueprint, Blueprint> {

    static {
        register(ExprBlueprintPart.class, Blueprint.class,
                "[blueprint] %blueprintpart% part [without:without [the] outer layer]",
                "blueprints"
        );
    }

    private Expression<Blueprint.Part> part;
    private boolean layer;

    @Override
    protected Blueprint @NotNull [] get(@NotNull Event event, Blueprint @NotNull [] source) {
        if (part == null){
            return new Blueprint[]{};
        }
        List<Blueprint> parts = new ArrayList<>();
        for (Blueprint print : source) {
            parts.add(print.part(part.getSingle(event), layer));
        }
        return parts.toArray(Blueprint[]::new);
    }

    @Override
    public @NotNull Class<? extends Blueprint> getReturnType() {
        return Blueprint.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "blueprint";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            part = (Expression<Blueprint.Part>) exprs[0];
            setExpr((Expression<? extends Blueprint>) exprs[1]);
        } else {
            setExpr((Expression<? extends Blueprint>) exprs[0]);
            part = (Expression<Blueprint.Part>) exprs[1];
        }
        layer = !parseResult.hasTag("without");
        return true;
    }
}
