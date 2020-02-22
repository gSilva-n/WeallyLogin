package com.redeweally.Eventos;

import com.redeweally.login.Main;
import com.redeweally.utils.Captcha;
import com.redeweally.utils.NaxsiUtils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class onClick implements Listener {

    private static Main m = Main.plugin;

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR){
            return;
        }
        if(event.getInventory().getName().equalsIgnoreCase("Clique na cabeça verde.")){

            net.minecraft.server.v1_8_R3.ItemStack nmsitem = CraftItemStack.asNMSCopy(event.getCurrentItem());

            NBTTagCompound itemcompound = (nmsitem.hasTag()) ? nmsitem.getTag() : new NBTTagCompound();

            String tipo = itemcompound.getString("tipo");

            if(tipo.equalsIgnoreCase("verde")){
                m.captcha.remove(p);
                p.closeInventory();
                p.sendMessage("§aCaptcha concluido!!");
                NaxsiUtils.send(p, "§a§lBEM-VINDO", "§fRegistre-se usando: /registrar <senha> <confirmasenha> <email>", 0, 5, 0);
            }else{
                final Player player = p;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer("§c§lREDE WEALLY\n\n §r§cVocê clicou na cabeça errada.\n§cEntre novamente e clique na cabeça verde.");
                    }
                }.runTaskLater(m, 10L);
            }
            event.setCancelled(true);
        }
    }
}
