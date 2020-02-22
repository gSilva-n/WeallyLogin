package com.redeweally.Eventos;

import com.redeweally.login.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class onCommand implements Listener {

    private Main m = Main.plugin;

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (m.nlogados.contains(event.getPlayer().getName())) {
            if (event.getMessage().contains("registrar") || event.getMessage().contains("register") || event.getMessage().contains("reg") || event.getMessage().contains("login") || event.getMessage().contains("logar")) return;
            if (m.uname.contains(event.getPlayer().getName())) {
                event.getPlayer().sendMessage("§cUtilize /logar <senha>");
            } else {
                event.getPlayer().sendMessage("§cUtilize /registrar <senha> <confirmarsenha> <email>");
            }
            Player player = event.getPlayer();
            player.playSound(player.getLocation(), Sound.VILLAGER_HAGGLE, .3f, 2f);
            event.setCancelled(true);
        }
    }

}
