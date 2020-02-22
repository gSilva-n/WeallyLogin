package com.redeweally.Eventos;

import com.redeweally.login.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onChat implements Listener {

    private Main m = Main.plugin;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        event.setCancelled(true);
            event.getPlayer().sendMessage("§cO bate-papo é desativado aqui.");
    }

}
