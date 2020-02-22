package com.redeweally.login;

import com.redeweally.Eventos.*;
import com.redeweally.commands.login;
import com.redeweally.commands.registrar;
import com.redeweally.connection.Connection;
import com.redeweally.manager.User;
import com.redeweally.utils.NaxsiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    public static Main plugin;
    public static boolean MySQL = false;
    public List<User> users = new ArrayList<>();
    public List<String> uname = new ArrayList<>();
    public List<String> nlogados = new ArrayList<>();
    public List<Player> captcha = new ArrayList<>();
    public HashMap<Player, Integer> tents = new HashMap<>();

    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        plugin = this;
        if(getConfig().getBoolean("MySQL.state")){
            MySQL = true;
        }
        saveDefaultConfig();
        registerEvents();
        getCommand("registrar").setExecutor(new registrar());
        getCommand("logar").setExecutor(new login());
        Connection.openConnection();
        System.out.println("§aCarregando os usuarios");
        Connection.loadUsers();
        NaxsiUtils.loadServers();
    }

    public void onDisable() {
        for (int i = 0; i < users.size(); i++) {
                Connection.addPlayer(users.get(i));
        }
        Connection.closeConnection();
    }

    public void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new onJoin(), this);
        pm.registerEvents(new onCommand(), this);
        pm.registerEvents(new onClick(), this);
        pm.registerEvents(new onClose(), this);
        pm.registerEvents(new onChat(), this);
    }
}
