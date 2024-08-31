package lol.aabss.skuishy.elements.entities.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import lol.aabss.skuishy.other.skript.SimpleEntityEffect;
import org.bukkit.entity.Allay;

@Name("Allay - Duplicate")
@Description("Makes an allay duplicate itself.")
@Examples({
        "add \"mama\" and \"papa\" to chat completions of all players"
})
@Since("2.3")
public class EffDuplicateAllay extends SimpleEntityEffect<Allay> {

    static {
        Skript.registerEffect(EffDuplicateAllay.class,
                "make %entities% duplicate",
                "duplicate [allay] %entities%"
        );
    }

    @Override
    protected void execute(Allay allay) {
        allay.duplicateAllay();
    }
}