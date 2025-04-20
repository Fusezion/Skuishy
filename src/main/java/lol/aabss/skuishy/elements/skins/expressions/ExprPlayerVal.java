package lol.aabss.skuishy.elements.skins.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.other.SkinWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Name("Skins - Player Skin Value")
@Description("Sends the value of the player's skin .")
@Examples({
        "command send-value <player>:",
        "\ttrigger:",
        "\t\tsend arg-1's skin value"
})
@Since("1.0")

public class ExprPlayerVal extends PropertyExpression<Player, String> {

    static {
       register(ExprPlayerVal.class, String.class,
                "(texture|skin) value",
                "players"
        );
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        setExpr((Expression<Player>) exprs[0]);
        return true;
    }

    @Override
    public @NotNull String toString(Event event, boolean debug) {
        return "skin value of player";
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, Player[] source) {
        List<String> values = new ArrayList<>();
        for (Player p : source){
            values.add(SkinWrapper.getProfileProperties(p.getPlayerProfile()).getValue());
        }
        return values.toArray(String[]::new);
    }

    @Override
    public boolean isSingle() {
        return getExpr().isSingle();
    }

}
