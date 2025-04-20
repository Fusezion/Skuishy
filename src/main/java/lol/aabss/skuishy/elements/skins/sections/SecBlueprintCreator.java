package lol.aabss.skuishy.elements.skins.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.other.Blueprint;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineskin.data.Variant;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;


@Name("Blueprint - Blueprint Creator")
@Description({
        "Creates a blueprint.",
        "`head: %blueprint%` - The head of the blueprint.",
        "`torso: %blueprint%` - The torso of the blueprint.",
        "`right arm: %blueprint%` - The right arm of the blueprint.",
        "`left arm: %blueprint%` - The left arm of the blueprint.",
        "`right leg: %blueprint%` - The right leg of the blueprint.",
        "`left leg: %blueprint%` - The left leg of the blueprint.",
        "`model: %skinmodel%` - The skin model of the blueprint.",
})
@Examples({
        "make blueprint stored in {_blueprint}",
        "\thead: head of blueprint from player \"aabss\"",
        "\ttorso: head of blueprint from player \"Oiiink\"",
        "\tright arm: right arm of blueprint from player \"MasterClashers\"",
        "\tleft arm: left arm of blueprint from player \"MasterClashers\"",
        "\tright leg: right leg of blueprint from player \"vWill\"",
        "\tleft leg: left leg of blueprint from player \"vWill\"",
        "\tmodel: steve"
})
@Since("2.6")

public class SecBlueprintCreator extends Section {

    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();

    static {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("DecentHolograms")){
            Skript.registerSection(SecBlueprintCreator.class,
                    "(create|make) [a] [new] blueprint stored in %object%"
            );
            ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("head", null, false, Object.class));
            ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("torso", null, false, Object.class));
            ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("right arm", null, false, Object.class));
            ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("left arm", null, false, Object.class));
            ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("right leg", null, false, Object.class));
            ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("left leg", null, false, Object.class));
            ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("model", null, false, Variant.class));
        }
    }

    private Variable<?> var;
    private Expression<Blueprint> head;
    private Expression<Blueprint> torso;
    private Expression<Blueprint> rightarm;
    private Expression<Blueprint> leftarm;
    private Expression<Blueprint> rightleg;
    private Expression<Blueprint> leftleg;
    private Expression<Variant> model;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        if (!(exprs[0] instanceof Variable<?>)) {
            Skript.error("Object must be a variable!");
            return false;
        }
        EntryContainer container = ENTRY_VALIDATOR.build().validate(sectionNode);
        if (container == null) return false;
        var = (Variable<?>) exprs[0];
        head = (Expression<Blueprint>) container.getOptional("head", false);
        torso = (Expression<Blueprint>) container.getOptional("torso", false);
        rightarm = (Expression<Blueprint>) container.getOptional("right arm", false);
        leftarm = (Expression<Blueprint>) container.getOptional("left arm", false);
        rightleg = (Expression<Blueprint>) container.getOptional("right leg", false);
        leftleg = (Expression<Blueprint>) container.getOptional("left leg", false);
        model = (Expression<Variant>) container.getOptional("model", false);
        return head != null && torso != null && rightarm != null && leftarm != null && rightleg != null && leftleg != null && model != null;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        // great code ik
        if (head != null && torso != null && rightarm != null && leftarm != null && rightleg != null && leftleg != null && model != null){
            Blueprint head = this.head.getSingle(event);
            Blueprint torso = this.torso.getSingle(event);
            Blueprint rightarm = this.rightarm.getSingle(event);
            Blueprint leftarm = this.leftarm.getSingle(event);
            Blueprint rightleg = this.rightleg.getSingle(event);
            Blueprint leftleg = this.leftleg.getSingle(event);
            Variant model = this.model.getSingle(event);
            if (head != null && torso != null && rightarm != null && leftarm != null && rightleg != null && leftleg != null && model != null){
                Blueprint blueprint = new Blueprint(model);
                blueprint.overlay(head);
                blueprint.overlay(torso);
                blueprint.overlay(rightarm);
                blueprint.overlay(leftarm);
                blueprint.overlay(rightleg);
                blueprint.overlay(leftleg);
                var.change(event, new Blueprint[]{blueprint}, Changer.ChangeMode.SET);
            }
        }
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "blueprint builder";
    }
}
