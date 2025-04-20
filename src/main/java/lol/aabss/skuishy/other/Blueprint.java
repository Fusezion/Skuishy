package lol.aabss.skuishy.other;

import ch.njol.skript.util.Color;
import ch.njol.skript.util.ColorRGB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lol.aabss.skuishy.Skuishy;
import org.jetbrains.annotations.NotNull;
import org.mineskin.data.Variant;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import static lol.aabss.skuishy.Skuishy.last_blueprint;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Blueprint {

    private BufferedImage image = null;
    private @NotNull Variant model = Variant.AUTO;
    public static JsonObject json;
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Blueprint(@NotNull Variant model) {
        this.model = model;
        this.image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        last_blueprint = this;
    }

    public Blueprint(Color color, @NotNull Variant model) {
        this.model = model;
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setColor(new java.awt.Color(color.asBukkitColor().asARGB()));
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
        graphics.dispose();
        this.image = img;
        last_blueprint = this;
    }

    public Blueprint(BufferedImage image, @NotNull Variant model) {
        this.model = model;
        this.image = image;
        last_blueprint = this;
    }

    public Blueprint(URL url, @NotNull Variant model) {
        this.model = model;
        try {
            this.image = ImageIO.read(url);
        } catch (IOException e) {
            Skuishy.Logger.exception(e);
            return;
        }
        last_blueprint = this;
    }

    public Blueprint(String player) {
        try {
            this.image = ImageIO.read(URI.create("https://minotar.net/skin/" + player).toURL());
            this.model = getVariant(image);
        } catch (IOException e) {
            Skuishy.Logger.exception(e);
            return;
        }
        last_blueprint = this;
    }

    public Blueprint part(Part p, boolean layer){
        Variant model = this.model();
        if (this.model() == Variant.AUTO){
            model = getVariant(this.image());
        }
        BufferedImage image = new Blueprint(model).image();
        Graphics2D img = image.createGraphics();
        if (model == Variant.CLASSIC){
            img.drawImage(this.image().getSubimage(p.x, p.y, p.w, p.h), p.x, p.y, null);
            if (layer){
                img.drawImage(this.image().getSubimage(p.lx, p.ly, p.w,p.h), p.x, p.y, null);
            }
            return new Blueprint(image, Variant.CLASSIC);
        }
        img.drawImage(this.image().getSubimage(p.x, p.y, p.sw, p.h), p.x, p.y, null);
        if (layer) {
            img.drawImage(this.image().getSubimage(p.lx, p.ly, p.sw, p.h), p.x, p.y, null);
        }
        return new Blueprint(image, Variant.SLIM);
    }

    public void save(String name) {
        File file = new File(Skuishy.instance.getDataFolder().getAbsolutePath()+"/blueprints/");
        if (!file.exists()){
            if (!file.mkdirs()){
                Skuishy.instance.getLogger().warning("Failed to make blueprints directory!");
            }
        }
        try {
            File print = new File(file.getAbsolutePath()+"/"+name+".png");
            ImageIO.write(image, "png", print);
            json.remove(name);
            json.addProperty(name, model.name());
            saveJson();
        } catch (IOException e) {
            Skuishy.Logger.exception(e);
        }
    }

    public void overlay(Blueprint blueprint) {
        Graphics2D graphics = blueprint.image.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
    }

    public void replaceColor(Color before, Color after) {
        for (int x = 0; x < image.getWidth(null); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getRGB(x, y) == before.asBukkitColor().asARGB()) {
                    image.setRGB(x, y, after.asBukkitColor().asARGB());
                }
            }
        }
    }

    public Color pixelColor(int x, int y){
        return ColorRGB.fromString(String.valueOf(image.getRGB(x, y)));
    }

    public void pixelColor(int x, int y, Color color){
        image.setRGB(x, y, color.asBukkitColor().asARGB());
    }

    public BufferedImage image(){
        return this.image;
    }

    public Variant model(){
        return this.model;
    }

    public Blueprint duplicate(){
        return new Blueprint(image, model);
    }

    public enum Part {
        HEAD(0, 0, 32, 0, 32, 32, 16),
        TORSO(16, 16, 16, 32, 24, 24, 16),
        RIGHT_ARM(40, 16, 40, 32, 14, 16,16),
        LEFT_ARM(32, 48, 48, 48, 14, 16, 16),
        RIGHT_LEG(0, 16, 0, 32, 16, 16, 16),
        LEFT_LEG(16, 48, 0, 48, 16, 16, 16);

        public final int x;
        public final int y;
        public final int lx;
        public final int ly;
        public final int sw;
        public final int w;
        public final int h;

        Part(int x, int y, int layerx, int layery, int slimwidth, int width, int height) {
            this.x = x;
            this.y = y;
            this.lx = layerx;
            this.ly = layery;
            this.sw = slimwidth;
            this.w = width;
            this.h = height;
        }
    }

    public static void loadJson(){
        try {
            File file = new File(Skuishy.instance.getDataFolder().getAbsolutePath() + "/blueprints/");
            if (!file.exists()) {
                if (file.mkdirs()) {
                    File jsonfile = new File(file.getAbsolutePath() + "/blueprints.json");
                    if (jsonfile.createNewFile()) {
                        FileWriter writer = new FileWriter(jsonfile);
                        writer.write(gson.toJson(new JsonObject()));
                        json = new JsonObject();
                        writer.close();
                    }
                }
            }
        } catch (IOException e){
            Skuishy.Logger.exception(e);
            return;
        }
        if (json == null) json = getJson();
    }

    @Nullable
    public static Blueprint loadBlueprint(String name) {
        File file = new File(Skuishy.instance.getDataFolder().getAbsolutePath()+"/blueprints/"+name+".png");
        if (!file.exists()){
            return null;
        }
        try {
            JsonObject json = getJson();
            if (json != null && json.has(name)) {
                return new Blueprint(ImageIO.read(file), Variant.valueOf(getJson().get(name).getAsString()));
            }
        } catch (IOException e){
            Skuishy.Logger.exception(e);
            return null;
        }
        return null;
    }

    public static void delete(String name) {
        File file = new File(Skuishy.instance.getDataFolder().getAbsolutePath()+"/blueprints/"+name+".png");
        if (!file.delete()) {
            Skuishy.instance.getLogger().warning("Failed to delete image file of "+name+"!");
        }
        json.remove(name);
        saveJson();
    }

    private static JsonObject getJson(){
        try {
            File jsonfile = new File(Skuishy.instance.getDataFolder().getAbsolutePath() + "/blueprints/blueprints.json");
            Scanner scan = new Scanner(jsonfile);
            StringBuilder jsonstr = new StringBuilder();
            while (scan.hasNextLine()) {
                jsonstr.append(scan.nextLine());
            }
            return JsonParser.parseString(jsonstr.toString()).getAsJsonObject();
        } catch (IOException e){
            Skuishy.Logger.exception(e);
            return null;
        }
    }

    public static void saveJson(){
        try {
            File jsonfile = new File(Skuishy.instance.getDataFolder().getAbsolutePath() + "/blueprints/blueprints.json");
            jsonfile.delete();
            jsonfile.createNewFile();
            FileWriter writer = new FileWriter(jsonfile);
            writer.write(gson.toJson(json));
            writer.close();
        } catch (IOException e){
            Skuishy.Logger.exception(e);
        }
    }

    private static Blueprint fromImage(BufferedImage image){
        return new Blueprint(image, Variant.AUTO);
    }

    // useful for auto variant
    public static Variant getVariant(BufferedImage image){
        return new java.awt.Color(image.getRGB(39, 52)).getAlpha() == 255 ? Variant.SLIM : Variant.CLASSIC;
    }

}
