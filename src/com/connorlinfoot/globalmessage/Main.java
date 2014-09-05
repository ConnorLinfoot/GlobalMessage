package com.connorlinfoot.globalmessage;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public class Main extends JavaPlugin implements Listener {
    private static Plugin instance;
    public static Permission perms = null;

    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        Server server = getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        setupPermissions();

        console.sendMessage(ChatColor.GREEN + "========== GlobalMessage! ==========");
        console.sendMessage(ChatColor.GREEN + "=========== VERSION: 1.0 ===========");
        console.sendMessage(ChatColor.GREEN + "======== BY CONNOR LINFOOT! ========");
    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if( args.length < 2 ){
            sender.sendMessage(ChatColor.RED + "Usage: /gmessage <group> <message>");
            return false;
        }

        StringBuilder builder = new StringBuilder();
        for (String value : args) {
            builder.append(value).append(" ");
        }
        String message = builder.toString();
        message = message.replace(args[0] + " ","");

        String group = args[0];
        Player[] players = Bukkit.getOnlinePlayers();
        ArrayList<String> pl = new ArrayList<String>();
        for( Player p : players ) {
            String[] groups = perms.getPlayerGroups(p);
            for( String s : groups ){
                if(s.equalsIgnoreCase(group)){
                    pl.add(p.getName());
                    break;
                }
            }
        }

        for( String s : pl ){
            Bukkit.getPlayer(s).sendMessage(message);
        }

        sender.sendMessage("Your message was sent to " + pl.size() + " players");

        return true;
    }

    public static Plugin getInstance() {
        return instance;
    }
}
