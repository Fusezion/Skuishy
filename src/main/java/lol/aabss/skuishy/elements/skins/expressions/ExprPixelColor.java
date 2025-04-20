package lol.aabss.skuishy.elements.skins.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import lol.aabss.skuishy.other.Blueprint;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Name("Blueprint - Pixel Color")
@Description("Gets the color of a pixel.")
@Examples({
        "send color at 1, 2 of {_print}"
})
@Since("2.6")
public class ExprPixelColor extends PropertyExpression<Blueprint, Color> {

    static {
        register(ExprPixelColor.class, Color.class,
                "color [of pixel] at [x] %integer%( and|,) [y] %integer%",
                "blueprints"
        );
    }

    private Expression<Integer> x;
    private Expression<Integer> y;

    @Override
    protected Color @NotNull [] get(@NotNull Event event, Blueprint @NotNull [] source) {
        if (x != null && y != null){
            Integer x = this.x.getSingle(event);
            Integer y = this.y.getSingle(event);
            if (x != null && y != null){
                List<Color> colors = new ArrayList<>();
                for (Blueprint print : source){
                    colors.add(print.pixelColor(x, y));
                }
                return colors.toArray(Color[]::new);
            }
        }
        return new Color[]{null};
    }

    @Override
    public @NotNull Class<? extends Color> getReturnType() {
        return Color.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "color at pixel of blueprint";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            x = (Expression<Integer>) exprs[0];
            y = (Expression<Integer>) exprs[1];
            setExpr((Expression<? extends Blueprint>) exprs[2]);
            return true;
        }
        setExpr((Expression<? extends Blueprint>) exprs[0]);
        x = (Expression<Integer>) exprs[1];
        y = (Expression<Integer>) exprs[2];
        return true;
    }

    @Override
    public @Nullable Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Color.class);
        }
        return null;
    }

    @Override
    public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (x != null && y != null && delta[0] != null){
            Integer x = this.x.getSingle(event);
            Integer y = this.y.getSingle(event);
            if (x != null && y != null){
                for (Blueprint print : getExpr().getArray(event)){
                    print.pixelColor(x, y, (Color) delta[0]);
                }
            }
        }
    }
}
