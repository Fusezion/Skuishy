package lol.aabss.skuishy.elements.entities.conditions.is;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import io.papermc.paper.entity.Shearable;
import lol.aabss.skuishy.other.skript.EntityCondition;

@Name("Shearable - Ready To Be Sheared")
@Description("True if the shearable is ready to be sheared.")
@Examples({
        "if {_shearable} is ready to be sheared:",
        "\tshear {_shearable}"
})
@Since("2.9")
public class CondIsShearableReadyToBeSheared extends EntityCondition<Shearable> {

    static {
        register(CondIsShearableReadyToBeSheared.class, "[shearable] ready to be sheared", "entities");
    }

    @Override
    protected boolean run(Shearable shearable) {
        return shearable.readyToBeSheared();
    }
}

