package lol.aabss.skuishy.elements.skins.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.other.Blueprint;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import static lol.aabss.skuishy.Skuishy.last_blueprint;

@Name("Blueprint - Last Blueprint")
@Description("Gets the last made blueprint.")
@Examples({
        "set {_blueprint} to last made blueprint"
})
@Since("2.6")
public class ExprLastBlueprint extends SimpleExpression<Blueprint> {

    static {
        Skript.registerExpression(ExprLastBlueprint.class, Blueprint.class, ExpressionType.SIMPLE,
                "[the] last [made|created] blueprint"
        );
    }

    @Override
    protected @Nullable Blueprint @NotNull [] get(@NotNull Event event) {
        return new Blueprint[]{last_blueprint};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Blueprint> getReturnType() {
        return Blueprint.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "last blueprint";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
