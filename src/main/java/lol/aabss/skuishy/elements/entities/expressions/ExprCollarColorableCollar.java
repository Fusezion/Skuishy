package lol.aabss.skuishy.elements.entities.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import io.papermc.paper.entity.CollarColorable;
import lol.aabss.skuishy.other.skript.EntityExpression;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.Nullable;


@Name("Collar Colorable - Collar Color")
@Description("Gets/sets the collar state of a collar colorable entity.")
@Examples({
        "set collar color of {_cat} to purple"
})
@Since("2.9")
public class ExprCollarColorableCollar extends EntityExpression<CollarColorable, Color> {

    static {
        register(ExprCollarColorableCollar.class, Color.class, "[collar[ ]colorable] collar color", "entities");
    }

    @Override
    public Color get(CollarColorable collarColorable) {
        return SkriptColor.fromDyeColor(collarColorable.getCollarColor());
    }

    @Override
    public void change(CollarColorable collarColorable, @Nullable Color color, Changer.ChangeMode mode) {
        if (color != null && mode == Changer.ChangeMode.SET) {
            collarColorable.setCollarColor(color.asDyeColor() == null ? DyeColor.RED : color.asDyeColor());
        }
    }


}
