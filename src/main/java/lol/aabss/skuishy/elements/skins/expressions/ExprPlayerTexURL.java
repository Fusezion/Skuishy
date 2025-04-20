package lol.aabss.skuishy.elements.skins.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Name("Skins - Player Skin Texture (URL)")
@Description("Gets the player's skin as a url.")
@Examples({
        "command send-texture <player>:",
        "\ttrigger:",
        "\t\tsend texture url of arg-1"
})
@Since("1.0")

public class ExprPlayerTexURL extends PropertyExpression<Player, String> {

    static {
        if (Skript.methodExists(PlayerProfile.class, "getTextures")) {
            register(ExprPlayerTexURL.class, String.class,
                    "[skin] texture url",
                    "players"
            );
        }
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        setExpr((Expression<? extends Player>) exprs[0]);
        return true;
    }

    @Override
    public @NotNull String toString(Event event, boolean debug) {
        return "skin texture url of player";
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, Player @NotNull [] source) {
        List<String> skins = new ArrayList<>();
        for (Player p : source){
            URL skin = p.getPlayerProfile().getTextures().getSkin();
            if (skin != null) {
                skins.add(skin.toString());
            }
        }
        return skins.toArray(String[]::new);
    }

    @Override
    public boolean isSingle() {
        return getExpr().isSingle();
    }
}