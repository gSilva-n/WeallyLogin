package com.redeweally.commands;

import com.redeweally.connection.Connection;
import com.redeweally.login.Main;
import com.redeweally.manager.User;
import com.redeweally.utils.NaxsiUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class login implements CommandExecutor {

    private Main m = Main.plugin;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        if (cmd.getLabel().equalsIgnoreCase("logar")) {

            Player player = (Player) sender;

            if (!m.uname.contains(player.getName())) {
                player.sendMessage("§cUse /registrar <senha> <confirmarsenha> <email>");
                return false;
            }
            if(m.nlogados.contains(player.getName())){
                if (args.length == 1) {
                    String senha = "";
                    for (int i = 0; i < m.users.size(); i++) {
                        User u = m.users.get(i);
                        if (u.getNome().equalsIgnoreCase(player.getName())) {
                            senha = u.getPassword();
                        }
                    }

                    if (args[0].equals(senha)) {
                        m.nlogados.remove(player.getName());
                        m.uname.add(player.getName());

                        player.playSound(player.getLocation(), Sound.LEVEL_UP, .3f, 2f);
                        NaxsiUtils.send(player, "§a§lAUTENTICADO!!", "§fVocê está sendo enviado para o lobby...", 0, 5, 0);

                        NaxsiUtils.connect(player, "LOBBY");

                        User p = new User();

                        for (int i = 0; i < m.users.size(); i++) {
                            if(m.users.get(i).getNome().equalsIgnoreCase(player.getName())){
                                p = m.users.get(i);
                            }
                        }

                        Connection.addPlayer(p);

                    } else {
                        int tents = m.tents.get(player);
                        m.tents.put(player, tents - 1);
                        if (tents == 1) {
                            final Player p = player;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.kickPlayer("§c§lREDE WEALLY \n\n §cVocê errou a senha mais de 3 vezes. \nEntre e tente novamente.");
                                }
                            }.runTaskLater(m, 10L);
                        } else {
                            player.sendMessage("§cSenha incorreta. Você tem mais " + m.tents.get(player) + " tentativas");
                        }
                    }
                } else {
                    player.sendMessage("§cUtilize /logar <senha>");
                }
            } else {
                player.sendMessage("§cVocê já está logado.");
            }
        }

        return false;
    }
}
