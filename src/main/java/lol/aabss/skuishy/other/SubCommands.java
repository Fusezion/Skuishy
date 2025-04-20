package lol.aabss.skuishy.other;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import lol.aabss.skuishy.Skuishy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static lol.aabss.skuishy.Skuishy.*;
import static lol.aabss.skuishy.other.GetVersion.latestSkriptVersion;
import static lol.aabss.skuishy.other.GetVersion.latestVersion;
import static lol.aabss.skuishy.other.UpdateChecker.updateCheck;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@SuppressWarnings("deprecation")
public class SubCommands {
    public static void cmdDependencies(CommandSender sender){
        if (!sender.hasPermission("skuishy.command.dependencies")){
            sender.sendMessage(miniMessage().deserialize(instance.getConfig().getString("permission-message")));
            return;
        }
        StringBuilder builder = new StringBuilder("<dark_gray>--</dark_gray> <color:#00ff00>Skuishy</color> <gray>Dependencies:</gray> <dark_gray>--</dark_gray>").append("\n\n");
        builder.append("<color:#00ff00>Dependencies:\n</color>");
        for (String name : element_map.keySet().stream().sorted(Comparator.naturalOrder()).toList()){
            builder.append("    <gray>").append(name).append(":</gray> ");
            if (element_map.get(name)){
                builder.append("<color:#00ff00>TRUE</color>\n");
            } else {
                builder.append("<color:#ff0000>FALSE</color>\n");
            }
        }
        builder.append("\n").append("<dark_gray>----------------</dark_gray>");
        sender.sendMessage(miniMessage().deserialize(builder.toString()));
    }

    public static void cmdInfo(CommandSender sender, @Nullable String plugin){
        if (!sender.hasPermission("skuishy.command.info")) {
            sender.sendMessage(miniMessage().deserialize(instance.getConfig().getString("permission-message")));
            return;
        }
        if (plugin == null) {
            String sk = latestSkriptVersion();
            String sku = latest_version;
            String msg = getString(sku, sk);
            // addons --
            List<String> msgs = new ArrayList<>();
            List<SkriptAddon> addonlist = new ArrayList<>(Skript.getAddons().stream().toList());
            // removes skuishy from addon list
            addonlist.remove(Skuishy.addon);
            if (!addonlist.isEmpty()) {
                for (SkriptAddon addon : Skript.getAddons()) {
                    // if the loop plugin is not skuishy add a message
                    if (addon != Skuishy.addon) {
                        PluginDescriptionFile d = addon.plugin.getDescription();
                        msgs.add(
                                "    <click:open_url:'<URL>'><hover:show_text:'<gray><AUTHORS>'><gray><NAME>: <color:#00ff00><VERSION></hover></click>"
                                        .replaceAll("<URL>", (d.getWebsite() != null ? d.getWebsite() : ""))
                                        .replaceAll("<AUTHORS>", d.getAuthors() + "")
                                        .replaceAll("<NAME>", d.getName())
                                        .replaceAll("<VERSION>", d.getVersion())
                        );
                    }
                }
            }
            StringBuilder addons = new StringBuilder();
            for (String e : msgs) {
                addons.append(e).append("\n");
            }
            // dependencies --
            List<String> deps = new ArrayList<>();
            for (SkriptAddon addon : Skript.getAddons()) {
                for (String dep : addon.plugin.getDescription().getSoftDepend()) {
                    Plugin pl = Bukkit.getPluginManager().getPlugin(dep);
                    if (pl != null && pl != Skript.getInstance()) {
                        PluginDescriptionFile d = pl.getDescription();
                        String msgg =
                                "    <click:open_url:'<URL>'><hover:show_text:'<gray><AUTHORS>'><gray><NAME>: <color:#00ff00><VERSION></hover></click>"
                                        .replaceAll("<URL>", (d.getWebsite() != null ? d.getWebsite() : ""))
                                        .replaceAll("<AUTHORS>", d.getAuthors() + "")
                                        .replaceAll("<NAME>", d.getName())
                                        .replaceAll("<VERSION>", d.getVersion());
                        if (!deps.contains(msgg)) {
                            deps.add(msgg);
                        }
                    }
                }
            }
            for (String dep : Skript.getInstance().getDescription().getSoftDepend()) {
                Plugin pl = Bukkit.getPluginManager().getPlugin(dep);
                if (pl != null) {
                    PluginDescriptionFile d = pl.getDescription();
                    deps.add(
                            "    <click:open_url:'<URL>'><hover:show_text:'<gray><AUTHORS>'><gray><NAME>: <color:#00ff00><VERSION></hover></click>"
                                    .replaceAll("<URL>", (d.getWebsite() != null ? d.getWebsite() : ""))
                                    .replaceAll("<AUTHORS>", d.getAuthors() + "")
                                    .replaceAll("<NAME>", d.getName())
                                    .replaceAll("<VERSION>", d.getVersion())
                    );
                }
            }
            StringBuilder dependencies = new StringBuilder();
            for (String e : deps) {
                dependencies.append(e).append("\n");
            }
            // sending the message --
            if (dependencies.isEmpty()) {
                if (addons.isEmpty()) {
                    msg = msg + "    <color:#ff0000>N/A\n<gray>Dependencies:\n    <color:#ff0000>N/A";
                } else {
                    msg = msg + addons + "<gray>Dependencies:\n    <color:#ff0000>N/A";
                }
            } else {
                if (addons.compareTo(new StringBuilder()) == 0) {
                    msg = msg + "    <color:#ff0000>N/A\n<gray>Dependencies:\n" + dependencies;
                } else {
                    msg = msg + addons + "<gray>Dependencies:\n" + dependencies;
                }
            }
            sender.sendMessage(miniMessage().deserialize(msg +
                    "\n<dark_gray>----------------"
            ));
        } else{
            Plugin p = Bukkit.getPluginManager().getPlugin(plugin);
            if (p != null) {
                PluginDescriptionFile d = p.getDescription();
                List<String> mainl = Arrays.stream(p.getClass().getProtectionDomain().getCodeSource().getLocation().getFile().split("/")).toList();
                String main = mainl.getLast();
                sender.sendMessage(miniMessage().deserialize("""
                       \s
                        <dark_gray>-- <color:#00ff00>Skuishy <gray>Info: <dark_gray>--<reset>
                                           \s
                        <gray>Name: <color:#00ff00><NAME>
                        <gray>File Name: <color:#00ff00><FILENAME>
                        <gray>Version: <color:#00ff00><VERSION>
                        <gray>Website: <color:#00ff00><WEBSITE>
                        <gray>Authors: <color:#00ff00><AUTHORS>
                        <gray>Contributors: <color:#00ff00><CONTRIBUTORS>
                        <gray>Description: <color:#00ff00><DESCRIPTION>
                        <gray>API Version: <color:#00ff00><APIV>
                        <gray>Prefix: <color:#00ff00><PREFIX>
                        <gray>Main Class: <color:#00ff00><MAINCLASS>
                                           \s
                        <dark_gray>----------------"""
                        .replaceAll("<NAME>", d.getName())
                        .replaceAll("<FILENAME>", main.replaceAll("%20", " "))
                        .replaceAll("<VERSION>", d.getVersion())
                        .replaceAll("<WEBSITE>", (d.getWebsite() != null ? "<click:open_url:'" + d.getWebsite() + "'>" + d.getWebsite() + "</click>" : "<color:#ff0000>N/A"))
                        .replaceAll("<AUTHORS>", (!d.getAuthors().isEmpty() ? d.getAuthors() + "" : "<color:#ff0000>N/A"))
                        .replaceAll("<CONTRIBUTORS>", (!d.getContributors().isEmpty() ? d.getContributors() + "" : "<color:#ff0000>N/A"))
                        .replaceAll("<DESCRIPTION>", (d.getDescription() != null ? d.getDescription() : "<color:#ff0000>N/A"))
                        .replaceAll("<APIV>", (d.getAPIVersion() != null ? d.getAPIVersion() : "<color:#ff0000>N/A"))
                        .replaceAll("<PREFIX>", (d.getPrefix() != null ? d.getPrefix() : "<color:#ff0000>N/A"))
                        .replaceAll("<MAINCLASS>", d.getMain())
                ));
            } else{
                sender.sendMessage(miniMessage().deserialize("<red>Invalid plugin!"));
            }
        }
    }

    @NotNull
    private static String getString(String sku, String sk) {
        String skuishyv = instance.getDescription().getVersion();
        String skriptv = Skript.getInstance().getDescription().getVersion();
        return "\n<dark_gray>-- <color:#00ff00>Skuishy <gray>Info: <dark_gray>--<reset>\n\n" +
                "<gray>Skuishy Version: <click:open_url:'https://github.com/SkriptLang/Skript/releases/tag/"+sku+"'><color:#00ff00>"+ skuishyv + (!Objects.equals(sku, skuishyv) ? " [Latest: "+ sku + "]" : "") +"<reset>\n" +
                "<gray>Server Version: <color:#00ff00>"+instance.getServer().getMinecraftVersion()+"<reset>\n" +
                "<gray>Server Implementation: <color:#00ff00>"+instance.getServer().getVersion()+"<reset>\n" +
                "<gray>Skript Version: <click:open_url:'https://github.com/SkriptLang/Skript/releases/tag/"+sk+"'><color:#00ff00>"+skriptv+(!Objects.equals(sk, skriptv) ? " [Latest: "+ sk + "]" : "") +"<reset>\n" +
                "<gray>Addons:\n";
    }

    public static void cmdReload(CommandSender sender){
        if (!sender.hasPermission("skuishy.command.reload")){
            sender.sendMessage(miniMessage().deserialize(instance.getConfig().getString("permission-message")));
            return;
        }
        instance.reloadConfig();
        instance.saveConfig();
        sender.sendMessage(miniMessage().deserialize("<color:#00ff00>Config reloaded!"));
    }

    public static void cmdUpdate(CommandSender sender){
        if (!sender.hasPermission("skuishy.command.update")){
            sender.sendMessage(miniMessage().deserialize(instance.getConfig().getString("permission-message")));
            return;
        }
        String v = latestVersion();
        if (v.equals(instance.getDescription().getVersion())){
            sender.sendMessage(miniMessage().deserialize("<color:#00ff00>You are up to date!"));
        } else{
            latest_version = v;
            updateCheck(sender);
        }
    }

    public static void cmdVersion(CommandSender sender){
        sender.sendMessage(miniMessage().deserialize("<color:#00ff00>This server is running Skuishy v" + instance.getDescription().getVersion() + " by aabss!"));
    }
}
