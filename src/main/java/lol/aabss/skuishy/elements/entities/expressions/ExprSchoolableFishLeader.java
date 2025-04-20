package lol.aabss.skuishy.elements.entities.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import io.papermc.paper.entity.SchoolableFish;
import lol.aabss.skuishy.other.skript.EntityExpression;
import org.bukkit.entity.Entity;

@Name("Schoolable Fish - School Leader")
@Description("Gets the school leader of a schoolable fish.")
@Examples({
        "send school leader of {_fish}"
})
@Since("2.9")
public class ExprSchoolableFishLeader extends EntityExpression<SchoolableFish, Entity> {

    static {
        register(ExprSchoolableFishLeader.class, Entity.class, "[schoolable[ ]fish] school leader", "entities");
    }

    @Override
    public Entity get(SchoolableFish schoolableFish) {
        return schoolableFish.getSchoolLeader();
    }
}
