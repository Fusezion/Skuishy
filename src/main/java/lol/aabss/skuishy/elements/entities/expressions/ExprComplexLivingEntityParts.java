package lol.aabss.skuishy.elements.entities.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Complex Living Entity - Parts")
@Description("Gets the parts of a complex living entity.")
@Examples({
        "set {_child::*} to complex parts of {_parent}"
})
@Since("2.9")
public class ExprComplexLivingEntityParts extends PropertyExpression<Entity, Entity> {

    static {
        register(ExprComplexLivingEntityParts.class, Entity.class, "[all [of the]] complex parts", "entities");
    }

    @Override
    protected Entity @NotNull [] get(@NotNull Event event, Entity[] entities) {
        List<Entity> entitys = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof ComplexLivingEntity) {
                entitys.addAll(((ComplexLivingEntity) entity).getParts());
            }
        }
        return entitys.toArray(Entity[]::new);
    }

    @Override
    public @NotNull Class<? extends Entity> getReturnType() {
        return Entity.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "complex entity parts";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends Entity>) expressions[0]);
        return true;
    }
}
