package com.redeweally.Eventos;

import com.redeweally.login.Main;
import com.redeweally.utils.NaxsiUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class onMove implements Listener {

    public Main m = Main.plugin;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (m.nlogados.contains(event.getPlayer().getName())) {
            if (!NaxsiUtils.isSameBlock(event.getFrom(), event.getTo())) {
                if (m.uname.contains(event.getPlayer().getName())) {
                    event.getPlayer().sendMessage("§cUtilize /logar <senha>");
                } else {
                    event.getPlayer().sendMessage("§cUtilize /registrar <senha> <confirmarsenha> <email>");
                }
                event.getPlayer().teleport(event.getFrom());
            }
        }
    }

}
