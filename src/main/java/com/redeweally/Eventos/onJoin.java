package com.redeweally.Eventos;

import com.redeweally.login.Main;
import com.redeweally.utils.Captcha;
import com.redeweally.utils.NaxsiUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static com.redeweally.utils.NaxsiUtils.sendHeaderAndFooter;

public class onJoin implements Listener {

    public Main m = Main.plugin;

    @EventHandler
    public void aoLogar(PlayerJoinEvent event){
        event.setJoinMessage("");
        Player p =  event.getPlayer();
        p.setGameMode(GameMode.ADVENTURE);
        final Player player = p;
        m.nlogados.add(p.getName());
        if(m.uname.contains(p.getName())){
            m.tents.put(p, 3);
            NaxsiUtils.send(p, "§a§lBEM-VINDO", "§fLogue-se usando: /login <senha>", 0, 0, 0);
        }else{
            m.captcha.add(p);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Captcha.executeCaptcha(player);
                }
            }.runTaskLater(m, 20L);
        }

        NaxsiUtils.timer(player);

        sendHeaderAndFooter(event.getPlayer(), "\n§6§lREDE WEALLY\n§fwww.redeweally.com\n", "\n§6Discord:§7 http://bit.ly/RedeWeallyDC\n§6Twitter:§7 twitter.com/redeweally\\n§6Fórum:§7 www.redeweally.com/forum\\n\\n§6   Adquira VIP e Cash acessando: §fwww.redeweally.com\\n");

    }

    @EventHandler
    public void aoSair(PlayerQuitEvent event){
        event.setQuitMessage("");
    }

    @EventHandler
    public void aoTomarDano(EntityDamageEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void aoTerFome(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }



}
