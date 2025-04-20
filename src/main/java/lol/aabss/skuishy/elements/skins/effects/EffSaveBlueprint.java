package lol.aabss.skuishy.elements.skins.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import lol.aabss.skuishy.other.Blueprint;
import org.jetbrains.annotations.NotNull;

@Name("Blueprint - Save/Load/Delete Blueprint")
@Description({"Saves/Loads/Deletes a blueprint from `plugins/Skuishy/blueprints/`.",
        "### SAVE NOTE: if the blueprint already exists, it will overwrite."})
@Examples({
        "save {_blueprint} as \"cool_print\"",
        "load saved blueprint named \"cool_print\" and store it in {_coolprint}",
        "delete saved blueprint named \"cool_print\""
})
@Since("2.6")
public class EffSaveBlueprint extends Effect {

    static {
        Skript.registerEffect(EffSaveBlueprint.class,
                "save %blueprint% (as|with name) %string%",
                "load [saved] blueprint (with name|named) %string% and (store|put) it in %object%",
                "delete [saved] blueprint (with name|named) %string%"
        );
    }

    private Expression<Blueprint> blueprint;
    private Expression<String> name;
    private Variable<?> var;

    @Override
    protected void execute(@NotNull Event event) {
        if (name == null) return;
        String name = this.name.getSingle(event);
        if (name != null) {
            if (blueprint != null) {
                Blueprint blueprint = this.blueprint.getSingle(event);
                if (blueprint != null) {
                    blueprint.save(name);
                }
            } else{
                if (var == null){
                    Blueprint.delete(name);
                } else{
                    var.change(event, new Blueprint[]{Blueprint.loadBlueprint(name)}, Changer.ChangeMode.SET);
                }
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "save/load/delete blueprint";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (matchedPattern == 0){
            blueprint = (Expression<Blueprint>) exprs[0];
            name = (Expression<String>) exprs[1];
        } else if (matchedPattern == 1){
            name = (Expression<String>) exprs[0];
            var = (Variable<?>) exprs[1];
        } else{
            name = (Expression<String>) exprs[0];
        }
        return true;
    }
}
