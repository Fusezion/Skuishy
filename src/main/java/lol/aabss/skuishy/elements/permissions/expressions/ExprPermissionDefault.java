package lol.aabss.skuishy.elements.permissions.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

@Name("Permissions - Permission Default")
@Description("Gets/Sets the permission default of a permission.")
@Examples({
        "send permission default of {_p}"
})
@Since("2.1")
public class ExprPermissionDefault extends SimpleExpression<PermissionDefault> {

    static{
        Skript.registerExpression(ExprPermissionDefault.class, PermissionDefault.class, ExpressionType.COMBINED,
                "[the] ([permission] default|default [permission]) [value] of %permission%",
                "%permission%'s ([permission] default|default [permission]) [value]"
        );
    }

    private Expression<Permission> perm;

    @Override
    protected PermissionDefault @NonNull [] get(@NonNull Event e) {
        Permission perm = this.perm.getSingle(e);
        if (perm != null) {
            return new PermissionDefault[]{perm.getDefault()};
        }
        return new PermissionDefault[0];
    }

    @Override
    public Class<?> @NonNull [] acceptChange(Changer.@NonNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(PermissionDefault.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(@NonNull Event e, Object @NonNull [] delta, Changer.@NonNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET){
            Permission perm = this.perm.getSingle(e);
            if (perm != null) {
                perm.setDefault((PermissionDefault) delta[0]);
            }
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NonNull Class<? extends PermissionDefault> getReturnType() {
        return PermissionDefault.class;
    }

    @Override
    public @NonNull String toString(@Nullable Event e, boolean debug) {
        return "permission default of permission";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NonNull Kleenean isDelayed, SkriptParser.@NonNull ParseResult parseResult) {
        perm = (Expression<Permission>) exprs[0];
        return true;
    }
}