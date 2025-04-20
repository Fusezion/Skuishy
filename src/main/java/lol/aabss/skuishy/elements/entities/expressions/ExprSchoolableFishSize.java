package lol.aabss.skuishy.elements.entities.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import io.papermc.paper.entity.SchoolableFish;
import lol.aabss.skuishy.other.skript.EntityExpression;

@Name("Schoolable Fish - School Size")
@Description("Gets the school size of a schoolable fish.")
@Examples({
        "send max school size of {_fish}"
})
@Since("2.9")
public class ExprSchoolableFishSize extends EntityExpression<SchoolableFish, Integer> {

    static {
        register(ExprSchoolableFishSize.class, Integer.class, "[schoolable[ ]fish] [:max] school size", "entities");
    }

    @Override
    public Integer get(SchoolableFish schoolableFish) {
        return tags.contains("max") ? schoolableFish.getMaxSchoolSize() : schoolableFish.getSchoolSize();
    }
}
