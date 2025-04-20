package lol.aabss.skuishy;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.bstats.bukkit.Metrics;
import ch.njol.skript.bstats.charts.SimplePie;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lol.aabss.skuishy.other.Blueprint;
import lol.aabss.skuishy.other.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static lol.aabss.skuishy.other.GetVersion.latestVersion;
import static lol.aabss.skuishy.other.SubCommands.*;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@SuppressWarnings("deprecation")
public class Skuishy extends JavaPlugin {

    public static Skuishy instance;
    public static SkriptAddon addon;
    public static long start;
    public static PermissionAttachment last_permission_attachment;
    public static Permission last_permission;
    public static Blueprint last_blueprint;
    public static String latest_version;
    public static String data_path;
    public static HashMap<String, Boolean> element_map = new HashMap<>();
    public static final String prefix = ChatColor.of("#00ff00") + "[Skuishy] " + ChatColor.RESET;
    private static Metrics metrics;
    public static final boolean skript_reflect_supported = Skript.classExists("com.btk5h.skriptmirror.ObjectWrapper");

    @SuppressWarnings("UnstableApiUsage")
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Blueprint.loadJson();
        getServer().getPluginManager().registerEvents(new UpdateChecker(), this);
        metrics = new Metrics(this, 20162);
        try {
            addon = Skript.registerAddon(this);
            addon.setLanguageFileDirectory("lang");
            registerPluginElements("DecentHolograms", "DecentHolograms");
            registerPluginElements("Vulcan", "Vulcan");
            registerElements("Entity", "Entities");
            registerElements("General", "General");
            registerElements("Note", "Notes");
            registerElements("Permission", "Permissions");
            registerElements("Persistence", "Persistence");
            registerElements("Plugin", "Plugins");
            registerElements("Skin", "Skins");
        } catch (IOException e) {
            Logger.exception(e);
        }
        metrics.addCustomChart(new SimplePie("skript_version", () -> Skript.getVersion().toString()));
        start = System.currentTimeMillis()/50;
        Logger.success("Skuishy has been enabled!");
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            latest_version = latestVersion();
            if (getConfig().getBoolean("version-check-msg")) Logger.warn("Got latest version."); // not a warn just want yellow
        }, 0L, 144000L);
        data_path = this.getDataFolder().getAbsolutePath();

        LifecycleEventManager<@org.jetbrains.annotations.NotNull Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            commands.register(
                    Commands.literal("skuishy")
                            .then(Commands.argument("arg", StringArgumentType.word())
                                    .then(Commands.argument("plugin", StringArgumentType.word())
                                            .suggests((ctx, builder) -> {
                                                if (ctx.getInput().split(" ").length > 1 && ctx.getInput().split(" ")[1].startsWith("info")) {
                                                    for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                                                        builder.suggest(p.getName());
                                                    }
                                                    return builder.buildFuture();
                                                }
                                                return null;
                                            })
                                            .executes(ctx -> {
                                                if (ctx.getArgument("arg", String.class).equals("info")) {
                                                    cmdInfo(ctx.getSource().getSender(), ctx.getArgument("plugin", String.class));
                                                }
                                                return 1;
                                            }))
                                    .suggests((ctx, builder) -> builder
                                            .suggest("dependencies")
                                            .suggest("info")
                                            .suggest("reload")
                                            .suggest("update")
                                            .suggest("version").buildFuture())
                                    .executes(ctx -> {
                                        switch (ctx.getArgument("arg", String.class)) {
                                            case "dependencies" -> cmdDependencies(ctx.getSource().getSender());
                                            case "info" -> cmdInfo(ctx.getSource().getSender(), null);
                                            case "reload" -> cmdReload(ctx.getSource().getSender());
                                            case "update" -> cmdUpdate(ctx.getSource().getSender());
                                            case "version" -> cmdVersion(ctx.getSource().getSender());
                                        }
                                        return 1;
                                    })
                            )
                            .executes(ctx -> {
                                ctx.getSource().getSender().sendMessage(miniMessage().deserialize(
                                        "<red>/skuishy <" +
                                                "<click:run_command:'/skuishy dependencies'><hover:show_text:'<green>/skuishy dependencies'>dependencies</hover></click>" +
                                                " | " +
                                                "<click:run_command:'/skuishy info'><hover:show_text:'<green>/skuishy info'>info</hover></click>" +
                                                " | " +
                                                "<click:run_command:'/skuishy reload'><hover:show_text:'<green>/skuishy reload'>reload</hover></click>" +
                                                " | " +
                                                "<click:run_command:'/skuishy update'><hover:show_text:'<green>/skuishy update'>update</hover></click>" +
                                                " | " +
                                                "<click:run_command:'/skuishy version'><hover:show_text:'<green>/skuishy version'>version</hover></click>" +
                                                ">"
                                ));
                                return 1;
                            })
                            .build(),
                    "The main command for Skuishy."
            );
        });
    }

    @Override
    public void onDisable(){
        getServer().getScheduler().cancelTasks(this);
        if (getConfig().getBoolean("auto-update", false)){
            if (new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile()).delete()) {
                UpdateChecker.update();
            }
        }
    }

    public void registerPluginElements(String pluginName, String name) throws IOException {
        element_map.put(name, false);
        if (Bukkit.getServer().getPluginManager().isPluginEnabled(pluginName)) {
            addon.loadClasses("lol.aabss.skuishy.elements."+name.toLowerCase());
            Logger.success(name+" elements loaded.");
            element_map.put(name, true);
        } else Logger.error(name+" elements not loaded.");
        metrics.addCustomChart(new SimplePie(name, () -> Boolean.toString(element_map.get(name))));
    }

    public void registerElements(String name, String plural) throws IOException {
        element_map.put(plural, false);
        if (getConfig().getBoolean(name.toLowerCase()+"-elements", true)){
            addon.loadClasses("lol.aabss.skuishy.elements."+plural.toLowerCase());
            Logger.success(name+" elements loaded.");
            element_map.put(plural, true);
        } else Logger.error(name+" elements not loaded.");
    }

    @Nullable
    public static NamespacedKey namespacedKeyFromObject(Object object){
        if (object instanceof String string) {
            String[] split = string.split(":");
            if (split.length > 1) {
                return new NamespacedKey(split[0], split[1]);
            } else {
                return new NamespacedKey(Skuishy.instance, string);
            }
        } else if (object instanceof NamespacedKey){
            return (NamespacedKey) object;
        }
        return null;
    }

    public static int index(int index) {
        boolean skriptIndex = instance.getConfig().getBoolean("prefer-skript-index", false);
        return (skriptIndex ? index+1 : index);
    }

    public static class Logger{

        public static void success(@Nullable Object message){
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + (message != null ? message.toString() : "null"));
        }

        public static void log(@Nullable Object message){
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.WHITE + (message != null ? message.toString() : "null"));
        }

        public static void warn(@Nullable Object message){
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + (message != null ? message.toString() : "null"));
        }

        public static void error(@Nullable Object message){
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + (message != null ? message.toString() : "null"));
        }

        public static void exception(Throwable throwable) {
            Bukkit.getConsoleSender().sendMessage(prefix+ChatColor.DARK_RED+
                    "An unexpected error occurred! See the stacktrace below:"
            );
            Bukkit.getConsoleSender().sendMessage(
                    prefix+ChatColor.RED+throwable+"\n"+ChatColor.DARK_RED
            );
            ComponentBuilder stackTrace = new ComponentBuilder();
            Bukkit.getConsoleSender().sendMessage(
                    prefix+"Stacktrace Message: "+ChatColor.RED+throwable.getMessage()
            );
            Bukkit.getConsoleSender().sendMessage(
                    prefix+"Cause: "+ChatColor.RED+throwable.getCause()
            );
            for (StackTraceElement element : throwable.getStackTrace()) {
                Bukkit.getConsoleSender().sendMessage(
                        prefix+"| "+ChatColor.RED+element
                );
                stackTrace.append(" "+element+"\n").color(ChatColor.RED);
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.isOp()) {
                    ComponentBuilder component = new ComponentBuilder();
                    component
                            .append("[Skuishy] ").color(ChatColor.of("#00ff00"))
                            .append("Something went wrong! See the problem in console.").color(ChatColor.RED)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, stackTrace.create()));
                    p.sendMessage(component.build());
                }
            }
        }

        public static void exception(Class<? extends Throwable> exceptionClass, String message) {
            for (Constructor<?> constructor : exceptionClass.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 1 && constructor.getParameters()[0].getType().equals(String.class)) {
                    try {
                        exception((Throwable) constructor.newInstance(message));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        exception(e);
                    }
                }
            }
        }

        public static void exception(String message) {
            exception(new RuntimeException(message));
        }
    }
}
