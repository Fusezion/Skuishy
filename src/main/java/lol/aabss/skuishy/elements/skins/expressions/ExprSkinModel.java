package lol.aabss.skuishy.elements.skins.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import lol.aabss.skuishy.other.Blueprint;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineskin.data.Variant;

import java.awt.image.BufferedImage;

@Name("Skins - Skin Model")
@Description("Gets the skin model of a skin.")
@Examples({
        "send skin model of player",
        "send skin model of {_print}"
})
@Since("2.6")
public class ExprSkinModel extends SimplePropertyExpression<Object, Variant> {

    static {
        register(ExprSkinModel.class, Variant.class,
                "skin (model|variant)", "objects"
        );
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "skin model";
    }

    @Override
    public @Nullable Variant convert(Object object) {
        if (object instanceof OfflinePlayer p){
            return Variant.valueOf(Bukkit.createProfile(p.getName()).getTextures().getSkinModel().name());
        } else if (object instanceof Blueprint print){
            if (print.model() == Variant.AUTO){
                return Blueprint.getVariant(print.image());
            }
            return print.model();
        } else if (object instanceof BufferedImage img){
            return Blueprint.getVariant(img);
        }
        return null;
    }

    @Override
    public @NotNull Class<? extends Variant> getReturnType() {
        return Variant.class;
    }
}
