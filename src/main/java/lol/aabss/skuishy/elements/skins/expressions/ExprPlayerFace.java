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
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.other.SkinWrapper;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Skins - Player Head Text")
@Description("Gets the head of a player as a string, with optionally the outer layer and text next to the head.")
@Examples({
        "on join:",
        "\tsend player head of player with \"\", \"&dWelcome %player%!\""
})
@Since("2.7")
public class ExprPlayerFace extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprPlayerFace.class, String.class, ExpressionType.COMBINED,
                "player (head|face|skull) of %offlineplayers/strings% [without:without [the] [outer] layer] [with [text] %-strings%]"
        );
    }

    private Expression<Object> player;
    private Expression<String> texts;
    private boolean helmet;

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        String[] strings = {};
        if (texts != null){
            strings = texts.getArray(e);
        }
        List<String> heads = new ArrayList<>();
        for (Object p : player.getArray(e)){
            if (p instanceof OfflinePlayer) {
                heads.add(SkinWrapper.sendHead(((OfflinePlayer) p).getName(), helmet, strings));
            } else {
                heads.add(SkinWrapper.sendHead(p.toString(), helmet, strings));
            }
        }
        return heads.toArray(String[]::new);
    }

    @Override
    public boolean isSingle() {
        return player.isSingle();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "player head of player";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Object>) exprs[0];
        if (player instanceof UnparsedLiteral) {
            player = LiteralUtils.defendExpression(player);
        }
        texts = (Expression<String>) exprs[1];
        helmet = !parseResult.hasTag("without");
        return LiteralUtils.canInitSafely(player);
    }
}
