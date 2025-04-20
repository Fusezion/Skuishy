package lol.aabss.skuishy.other;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

import static lol.aabss.skuishy.Skuishy.instance;
import static lol.aabss.skuishy.Skuishy.latest_version;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@SuppressWarnings("deprecation")
public class UpdateChecker implements Listener {

    public static boolean updateCheck(CommandSender p){
        if (!Objects.equals(latest_version, instance.getDescription().getVersion())){
            p.sendMessage(miniMessage().deserialize("""
                        
                        <click:open_url:'https://modrinth.com/plugin/skuishy/version/<NEW_VERSION>'><hover:show_text:'Click to update!'><gray>There is a new <color:#40ff00>Skuishy <gray>update! <dark_gray>(v<OLD_VERSION> -> v<NEW_VERSION>)
                        <yellow>Click <green>here</green> to download!
                        """.replaceAll("<NEW_VERSION>", latest_version)
                    .replaceAll("<OLD_VERSION>", instance.getDescription().getVersion())
            ));
            return true;
        }return false;
    }

    public static void update(){
        if (!Objects.equals(latest_version, instance.getDescription().getVersion())){
            if (GetVersion.latestFile()) {
                Bukkit.getConsoleSender().sendMessage(
                        miniMessage().deserialize("<green>Successfully updated!")
                );
            } else{
                Bukkit.getConsoleSender().sendMessage(
                        miniMessage().deserialize("<red>Something went horribly wrong!")
                );
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (instance.getConfig().getBoolean("update-checker", true)) {
            if (e.getPlayer().hasPermission("skuishy.updatechecker")) {
                Bukkit.getScheduler().runTaskLater(instance, () -> updateCheck(e.getPlayer()), 100L);
            }
        }
    }
}
