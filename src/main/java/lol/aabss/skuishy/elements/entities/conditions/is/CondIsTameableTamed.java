package lol.aabss.skuishy.elements.entities.conditions.is;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import lol.aabss.skuishy.other.skript.EntityCondition;
import org.bukkit.entity.Tameable;

@Name("Tameable - Is Tamed")
@Description("True if the tameable is tamed.")
@Examples({
        "if {_wolf} is tamed:",
        "\tset tamed state of {_wolf} to false"
})
@Since("2.9")
public class CondIsTameableTamed extends EntityCondition<Tameable> {

    static {
        register(CondIsTameableTamed.class, "[tameable] tamed", "entities");
    }

    @Override
    protected boolean run(Tameable tameable) {

        return tameable.isTamed();
    }
}
