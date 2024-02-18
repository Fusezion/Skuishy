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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static lol.aabss.skuishy.Skuishy.*;
import static lol.aabss.skuishy.other.GetVersion.latestVersion;
import static lol.aabss.skuishy.other.UpdateChecker.updateCheck;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class SubCommands {
    public static void cmdInfo(CommandSender sender, @Nullable String plugin){
        if (!sender.hasPermission("skuishy.command.info")) {
            sender.sendMessage(miniMessage().deserialize(instance.getConfig().getString("permission-message")));
            return;
        }
        if (plugin == null) {
            String sk = latest_skript_version;
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
                    if (addon != instance.getAddonInstance()) {
                        PluginDescriptionFile d = addon.plugin.getDescription();
                        msgs.add(
                                "    <click:open_url:'<URL>'><hover:show_text:'<gray><AUTHORS>'><gray><NAME>: <color:#40ff00><VERSION></hover></click>"
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
                                "    <click:open_url:'<URL>'><hover:show_text:'<gray><AUTHORS>'><gray><NAME>: <color:#40ff00><VERSION></hover></click>"
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
                            "    <click:open_url:'<URL>'><hover:show_text:'<gray><AUTHORS>'><gray><NAME>: <color:#40ff00><VERSION></hover></click>"
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
                String main = mainl.get(mainl.size()-1);
                sender.sendMessage(miniMessage().deserialize("""
                        
                        <dark_gray>-- <color:#40ff00>Skuishy <gray>Info: <dark_gray>--<reset>
                                            
                        <gray>Name: <color:#40ff00><NAME>
                        <gray>File Name: <color:#40ff00><FILENAME>
                        <gray>Version: <color:#40ff00><VERSION>
                        <gray>Website: <color:#40ff00><WEBSITE>
                        <gray>Authors: <color:#40ff00><AUTHORS>
                        <gray>Contributors: <color:#40ff00><CONTRIBUTORS>
                        <gray>Description: <color:#40ff00><DESCRIPTION>
                        <gray>API Version: <color:#40ff00><APIV>
                        <gray>Prefix: <color:#40ff00><PREFIX>
                        <gray>Main Class: <color:#40ff00><MAINCLASS>
                                            
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
        return "\n<dark_gray>-- <color:#40ff00>Skuishy <gray>Info: <dark_gray>--<reset>\n\n" +
                "<gray>Skuishy Version: <color:#40ff00>"+ skuishyv + (!Objects.equals(sku, skuishyv) ? " [Latest: "+ sku + "]" : "") +"<reset>\n" +
                "<gray>Server Version: <color:#40ff00>"+instance.getServer().getMinecraftVersion()+"<reset>\n" +
                "<gray>Server Implementation: <color:#40ff00>"+instance.getServer().getVersion()+"<reset>\n" +
                "<gray>Skript Version: <color:#40ff00>"+skriptv+(!Objects.equals(sk, skriptv) ? " [Latest: "+ sk + "]" : "") +"<reset>\n" +
                "<gray>Addons:\n";
    }

    public static void cmdReload(CommandSender sender){
        if (!sender.hasPermission("skuishy.command.reload")){
            sender.sendMessage(miniMessage().deserialize(instance.getConfig().getString("permission-message")));
            return;
        }
        instance.reloadConfig();
        instance.saveConfig();
        sender.sendMessage(miniMessage().deserialize("<color:#40ff00>Config reloaded!"));
    }

    public static void cmdUpdate(CommandSender sender){
        if (!sender.hasPermission("skuishy.command.update")){
            sender.sendMessage(miniMessage().deserialize(instance.getConfig().getString("permission-message")));
            return;
        }
        String v = latestVersion();
        if (v.equals(instance.getDescription().getVersion())){
            sender.sendMessage(miniMessage().deserialize("<color:#40ff00>You are up to date!"));
        } else{
            latest_version = v;
            updateCheck(sender);
        }
    }

    public static void cmdVersion(CommandSender sender){
        sender.sendMessage(miniMessage().deserialize("<color:#40ff00>This server is running Skuishy v" + instance.getDescription().getVersion() + " by aabss!"));
    }
}
