package lol.aabss.skuishy.elements.entities.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import lol.aabss.skuishy.other.skript.EntityExpression;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.LivingEntity;

@Name("Complex Entity Part - Parent")
@Description("Gets the parent of a complex entity part.")
@Examples({
        "set {_parent} to complex parent of {_child::1}"
})
@Since("2.9")
public class ExprComplexEntityPartParent extends EntityExpression<ComplexEntityPart, LivingEntity> {

    static {
        register(ExprComplexEntityPartParent.class, LivingEntity.class, "complex [entity part] parent", "entities");
    }

    @Override
    public LivingEntity get(ComplexEntityPart complexEntityPart) {
        return complexEntityPart.getParent();
    }
}
