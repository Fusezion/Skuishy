package lol.aabss.skuishy.elements.skins.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.UnparsedLiteral;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.Skuishy;
import lol.aabss.skuishy.other.Blueprint;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineskin.data.Variant;

import java.net.MalformedURLException;
import java.net.URL;

@Name("Blueprint - New Blueprint")
@Description("Returns a new blueprint.")
@Examples({
        "set {_empty} to classic empty blueprint",
        "set {_blueprint} to slim new blueprint from player \"aabss\"",
        "set {_blueprint} to slim blueprint from url \"https://i.imgur.com/KWITSCB.png\"",
        "set {_color} to classic red blueprint"
})
@Since("2.6")
public class ExprNewBlueprint extends SimpleExpression<Blueprint> {

    static {
        Skript.registerExpression(ExprNewBlueprint.class, Blueprint.class, ExpressionType.COMBINED,
                "[a] [new] [(:slim|:classic)] empty blueprint",
                "[a] [new] blueprint from player %offlineplayer/string%",
                "[a] [new] [(:slim|:classic)] blueprint from url %string%",
                "[a] [new] [(:slim|:classic)] %color% blueprint"
        );
    }

    private Expression<Object> skin;
    private int pattern;
    private Variant variant;

    @SuppressWarnings("deprecation")
    @Override
    protected Blueprint @NotNull [] get(@NotNull Event event) {
        if (pattern == 0) {
            return new Blueprint[]{new Blueprint(variant)};
        } else if (pattern == 1){
            if (skin != null) {
                Object skin = this.skin.getSingle(event);
                if (skin != null) {
                    return new Blueprint[]{new Blueprint(skin instanceof OfflinePlayer ? ((OfflinePlayer) skin).getName() : (String) skin)};
                }
            }
        } else if (pattern == 2){
            if (skin != null) {
                Object skin = this.skin.getSingle(event);
                if (skin != null) {
                    try {
                        return new Blueprint[]{new Blueprint(new URL((String) skin), variant)};
                    } catch (MalformedURLException ex) {
                        Skuishy.Logger.exception(ex);
                        return null;
                    }
                }
            }
        } else if (pattern == 3){
            if (skin != null) {
                Object skin = this.skin.getSingle(event);
                if (skin != null) {
                    return new Blueprint[]{new Blueprint((Color)skin, variant)};
                }
            }
        }
        return new Blueprint[]{null};
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
        return "new blueprint";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        pattern = matchedPattern;
        if (parseResult.hasTag("slim")){
            variant = Variant.SLIM;
        } else if (parseResult.hasTag("classic")){
            variant = Variant.CLASSIC;
        } else{
            variant = Variant.AUTO;
        }
        if (matchedPattern == 0) return true;
        skin = (Expression<Object>) exprs[0];
        if (skin instanceof UnparsedLiteral) {
            skin = LiteralUtils.defendExpression(skin);
        }
        return LiteralUtils.canInitSafely(skin);
    }
}
