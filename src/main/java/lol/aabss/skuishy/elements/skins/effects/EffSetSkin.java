package lol.aabss.skuishy.elements.skins.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.UnparsedLiteral;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import lol.aabss.skuishy.Skuishy;
import lol.aabss.skuishy.other.Blueprint;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineskin.data.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

import static lol.aabss.skuishy.other.SkinWrapper.setSkin;
import static lol.aabss.skuishy.other.SkinWrapper.uploadSkin;

@Name("Player - Set Skin")
@Description({"Sets the skin of the player to a value and a signature."})
@Examples({
        "set skin of player to \"Dinnerbone\" #A name of a player",
        "set skin of player to \"http://textures.minecraft.net/texture/9c8acc755dc6b5e1d282d528030ebc20823a7608853ad5f747ff7ec45d576555\" #Official minecraft skin site",
        "set skin of player to \"https://i.imgur.com/KWITSCB.png\" #An image from any website (nothing but the image on the site)",
        "set skin of player to \"{VALUE}\" #The full value of the skin (no signature)",
        "set skin of player to \"{VALUE}\" and \"{SIGNATURE}\" #The full value and signature of the skin.",
        "set skin of player to {_bufferedimage} # Images not supported by Skuishy, but just in case you want to use another addon like SkImage :)",
        "set skin of player to {_blueprint} #A blueprint",
        "set skin of player to \"C:/Users/User/Documents/Server/plugins/Skuishy/skin.png\" #A file"
})
@Since("2.3, 2.6/2.7 (fixed), 2.8 (file support)")

public class EffSetSkin extends Effect {

    static {
        Skript.registerEffect(EffSetSkin.class,
                "set [minecraft] skin of %players% to %object%",
                "set %players%'[s] [minecraft] skin to %object%",
                "set [minecraft] skin of %players% to [value] %string% [and [signature] %-string%]",
                "set %player%'[s] [minecraft] skin to [value] %string% [and [signature] %-string%]"
        );
    }

    private Expression<Player> player;
    private Expression<Object> skin;
    private Expression<String> value;
    private Expression<String> signature;

    @Override
    protected void execute(@NotNull Event event) {
        try {
            if (value == null || signature == null) {
                if (skin != null) {
                    Object skin = this.skin.getSingle(event);
                    if (skin != null) {
                        // noinspection all
                        if (skin instanceof String str) {
                            if (str.length() <= 16 || !str.contains("://")) {
                                for (Player p : player.getArray(event)) {
                                    setSkin(p, str);
                                }
                            } else {
                                if (str.contains("://")) {
                                    uploadSkin(str).whenCompleteAsync(getWhenComplete(event));
                                } else if (new File(str).exists()) {
                                    uploadSkin(ImageIO.read(new File(str))).whenCompleteAsync(getWhenComplete(event));
                                } else {
                                    if (value != null) {
                                        for (Player p : player.getArray(event)) {
                                            setSkin(p, value.getSingle(event), null);
                                        }
                                    }
                                }
                            }
                        } else if (skin instanceof Blueprint print) {
                            uploadSkin(print.image()).whenCompleteAsync(getWhenComplete(event));
                        } else if (skin instanceof BufferedImage image){
                            // images not supported by skuishy, but just in case you use another addon like SkImage :)
                            uploadSkin(image).whenCompleteAsync(getWhenComplete(event));
                        } else if (skin instanceof OfflinePlayer op) {
                            for (Player p : player.getArray(event)) {
                                setSkin(p, op.getName());
                            }
                        }
                    }
                }
            } else{
                String value = this.value.getSingle(event);
                String signature = this.signature.getSingle(event);
                if (value != null && signature != null){
                    for (Player p : player.getArray(event)){
                        setSkin(p, value, signature);
                    }
                }
            }
        } catch (IOException e) {
            Skuishy.Logger.exception(e);
        }
    }

    private java.util.function.BiConsumer<? super Texture, ? super Throwable> getWhenComplete(Event event){
        return (BiConsumer<Texture, Throwable>) (texture, throwable) -> {
            if (throwable == null) {
                for (Player p : player.getArray(event)) {
                    setSkin(p, texture);
                }
            } else {
                Skuishy.Logger.exception(throwable);
            }
        };
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "set skin of player";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (matchedPattern == 0 || matchedPattern == 1) {
            player = (Expression<Player>) exprs[0];
            skin = (Expression<Object>) exprs[1];
            if (skin instanceof UnparsedLiteral) {
                skin = LiteralUtils.defendExpression(skin);
            }
        } else {
            player = (Expression<Player>) exprs[0];
            value = (Expression<String>) exprs[1];
            signature = (Expression<String>) exprs[2];
        }
        if (skin != null) {
            return LiteralUtils.canInitSafely(skin);
        }
        return true;
    }
}
