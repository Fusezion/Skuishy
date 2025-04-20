package lol.aabss.skuishy.elements.entities.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import lol.aabss.skuishy.other.skript.EntityExpression;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.Nullable;

@Name("Tameable - Tamed")
@Description("Gets/sets the tamed state of a tameable.")
@Examples({
        "set tamed state of {_cat} to true"
})
@Since("2.9")
public class ExprTameableTamed extends EntityExpression<Tameable, Boolean> {

    static {
        register(ExprTameableTamed.class, Boolean.class, "[tameable] tamed [state|mode]", "entities");
    }

    @Override
    public Boolean get(Tameable tameable) {
        return tameable.isTamed();
    }

    @Override
    public void change(Tameable tameable, @Nullable Boolean aBoolean, Changer.ChangeMode mode) {
        if (aBoolean != null && mode == Changer.ChangeMode.SET) {
            tameable.setTamed(aBoolean);
        }
    }
}
