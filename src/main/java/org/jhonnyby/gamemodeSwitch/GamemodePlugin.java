package org.jhonnyby.gamemodeSwitch;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamemodePlugin extends JavaPlugin implements TabCompleter {

    private FileConfiguration config;
    private static final String DEFAULT_PREFIX = "§3§lGamemodeSwitch §r>> ";

    @Override
    public void onEnable() {
        // Plugin-Initialisierung
        saveDefaultConfig();
        config = getConfig();
        String prefix = config.getString("prefix", DEFAULT_PREFIX);

        getLogger().info(prefix + config.getString("messages.enable", "GamemodePlugin aktiviert!"));
        getCommand("gamemode").setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        String prefix = config.getString("prefix", DEFAULT_PREFIX);
        getLogger().info(prefix + config.getString("messages.disable", "GamemodePlugin deaktiviert!"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = config.getString("prefix", DEFAULT_PREFIX);

        if (command.getName().equalsIgnoreCase("gamemode") || command.getName().equalsIgnoreCase("gm")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("gamemodeswitch.admin.reload")) {
                    reloadConfig();
                    config = getConfig();
                    sender.sendMessage(prefix + "Config wurde neu geladen!");
                    getLogger().info(prefix + "Config wurde neu geladen!");
                    return true;
                } else {
                    sender.sendMessage(prefix + config.getString("messages.noPermission", "Du hast keine Berechtigung, diesen Befehl auszuführen!"));
                    return true;
                }
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("gamemodeswitch.change")) {
                    String cmd = args.length > 0 ? args[0].toLowerCase() : "";

                    switch (cmd) {
                        case "0":
                        case "survival":
                        case "s":
                            player.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage(prefix + config.getString("messages.survival", "Spielmodus geändert zu Überleben!"));
                            break;
                        case "1":
                        case "creative":
                        case "c":
                            player.setGameMode(GameMode.CREATIVE);
                            player.sendMessage(prefix + config.getString("messages.creative", "Spielmodus geändert zu Kreativ!"));
                            break;
                        case "2":
                        case "adventure":
                        case "a":
                            player.setGameMode(GameMode.ADVENTURE);
                            player.sendMessage(prefix + config.getString("messages.adventure", "Spielmodus geändert zu Abenteuer!"));
                            break;
                        case "3":
                        case "spectator":
                        case "sp":
                            player.setGameMode(GameMode.SPECTATOR);
                            player.sendMessage(prefix + config.getString("messages.spectator", "Spielmodus geändert zu Zuschauer!"));
                            break;
                        default:
                            player.sendMessage(prefix + config.getString("messages.invalidMode", "Ungültiger Spielmodus. Bitte verwenden Sie 0, 1, 2 oder 3."));
                            break;
                    }
                } else {
                    player.sendMessage(prefix + config.getString("messages.noPermission", "Du hast keine Berechtigung, um diesen Befehl zu verwenden."));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender.isOp() && command.getName().equalsIgnoreCase("gamemode") || command.getName().equalsIgnoreCase("gm")) {
            if (args.length == 1) {
                return Arrays.asList("0", "1", "2", "3", "survival", "creative", "adventure", "spectator", "reload");
            }
        }
        return new ArrayList<>();
    }
}
