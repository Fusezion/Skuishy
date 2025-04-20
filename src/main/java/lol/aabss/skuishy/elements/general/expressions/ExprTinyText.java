package lol.aabss.skuishy.elements.general.expressions;

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
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.other.Text;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Other - Tiny Text")
@Description("Returns a text in tiny caps.")
@Examples({
        "send \"hi\" in tiny caps"
})
@Since("2.7")
public class ExprTinyText extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprTinyText.class, String.class, ExpressionType.COMBINED,
                "[:super] tiny [(text|caps)] %objects%",
                "%objects% in [:super] tiny (text|caps)"
        );
    }

    private Expression<Object> text;
    private boolean superTiny;

    @Override
    protected String @NotNull [] get(@NotNull Event event) {
        List<String> string = new ArrayList<>();
        for (Object s : text.getArray(event)){
            string.add(Text.tinyText(Classes.toString(s), superTiny));
        }
        return string.toArray(String[]::new);
    }

    @Override
    public boolean isSingle() {
        return text.isSingle();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "tiny text";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, @NotNull Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        text = (Expression<Object>) exprs[0];
        if (text instanceof UnparsedLiteral) {
            text = LiteralUtils.defendExpression(text);
        }
        superTiny = parseResult.hasTag("super");
        return LiteralUtils.canInitSafely(text);
    }
}
