package com.redeweally.Eventos;

import com.redeweally.login.Main;
import com.redeweally.utils.Captcha;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class onClose implements Listener {

    private static Main m = Main.plugin;

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if(event.getInventory().getName().equalsIgnoreCase("Clique na cabeça verde.")){
            Player p = (Player) event.getPlayer();
            if(m.captcha.contains(p)){
                final Player player = p;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer("§c§lREDE WEALLY\n\n §r§cVocê cancelou o captcha.\n§cEntre novamente e faça o captcha.");
                    }
                }.runTaskLater(m, 10L);
            }
        }
    }

}
