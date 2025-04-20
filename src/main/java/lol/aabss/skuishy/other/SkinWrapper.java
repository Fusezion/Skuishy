package lol.aabss.skuishy.other;

import ch.njol.skript.Skript;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lol.aabss.skuishy.Skuishy;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;
import org.mineskin.GenerateOptions;
import org.mineskin.Java11RequestHandler;
import org.mineskin.MineSkinClient;
import org.mineskin.data.Texture;
import org.mineskin.data.Variant;
import org.mineskin.data.Visibility;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SkinWrapper {

    public static MineSkinClient client = MineSkinClient.builder().userAgent("Skuishy-Agent").requestHandler(Java11RequestHandler::new).build();
    public static GenerateOptions options = GenerateOptions.create().name("Skuishy-Upload").variant(Variant.AUTO).visibility(Visibility.UNLISTED);

    public static ProfileProperty getProfileProperties(PlayerProfile p) {
        return p.getProperties().iterator().next();
    }

    public static void setSkin(Player player, String skin){
        if (Skript.classExists("com.destroystokyo.paper.profile.PlayerProfile")
                && Skript.classExists("org.bukkit.profile.PlayerTextures")
        ) {
            if (player.getName().equals(skin)) {
                PlayerProfile e = player.getPlayerProfile();
                e.getTextures().clear();
                PlayerTextures t = e.getTextures();
                t.clear();
                e.setTextures(t);
                player.setPlayerProfile(e);
            }
            PlayerProfile newprofile = Bukkit.createProfile(skin);
            newprofile.setTextures(newprofile.getTextures());
            newprofile.complete();
            PlayerProfile profile = player.getPlayerProfile();
            profile.setProperties(newprofile.getProperties());
            player.setPlayerProfile(profile);
        }
    }

    public static void setSkin(Player player, Texture texture) {
        setSkin(player, texture.value(), texture.signature());
    }

    public static void setSkin(Player player, String value, @Nullable String signature){
        Bukkit.getScheduler().runTask(Skuishy.instance, () -> {
            PlayerProfile profile = player.getPlayerProfile();
            profile.removeProperties(profile.getProperties());
            profile.setProperty(new ProfileProperty("textures", value, signature));
            player.setPlayerProfile(profile);
        });
    }

    private static BufferedImage getHead(String name, boolean helm) throws IOException {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        if (player.getPlayerProfile().getTextures().getSkin() == null){
            return ImageIO.read(URI.create("https://minotar.net/"+(helm ? "helm" : "avatar")+"/"+name+"/8.png").toURL());
        }
        BufferedImage skin = ImageIO.read(player.getPlayerProfile().getTextures().getSkin());
        BufferedImage front = skin.getSubimage(8, 8, 8, 8);
        if (helm) {
            front.getGraphics().drawImage(skin.getSubimage(40, 8, 8, 8), 0, 0, null);
        }
        return front;
    }

    @SuppressWarnings("deprecation")
    public static String sendHead(String name, boolean helm, String... texts) {
        try {
            List<String> textList = new ArrayList<>();
            for (String s : texts) {
                textList.addAll(List.of(s.split("\n")));
            }
            BufferedImage img = getHead(name, helm);
            String[] result = new String[8];
            for (int y = 0; y < 8; y++) {
                result[y] = "";
                for (int x = 0; x < 8; x++) {
                    ChatColor c = ChatColor.of(new Color(img.getRGB(x, y)));
                    result[y] += (c.toString() + "\u2588").replaceAll("\\?", "");
                }
                if (textList.size() > y) {
                    result[y] += " " + ChatColor.RESET + textList.get(y);
                }
            }
            return String.join("\n", result);
        } catch (IOException e) {
            Skuishy.Logger.exception(e);
            return null;
        }
    }

    public static CompletableFuture<Texture> uploadSkin(BufferedImage image) throws IOException {
        return client.generateUpload(image, options.variant(Blueprint.getVariant(image)))
                .thenApply(result -> result.getSkin().data().texture());
    }

    public static CompletableFuture<Texture> uploadSkin(String url) {
        return client.generateUrl(url, options)
                .thenApply(result -> result.getSkin().data().texture());
    }

}
