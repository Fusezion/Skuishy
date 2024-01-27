package lol.aabss.skuishy.elements.expressions.sounds;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Block - Fall Sound")
@Description("Gets the fall sound of the block.")
@Examples({
        "send fall sound of {_b}"
})
@Since("2.1")
public class ExprFallSound extends PropertyExpression<Block, String> {

    static {
        register(ExprFallSound.class, String.class,
                "[block] fall sound",
                "block");
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, Block[] source) {
        return new String[]{source[0].getBlockSoundGroup().getFallSound().name().replaceAll("_", ".").toLowerCase()};
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "fall sound";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}