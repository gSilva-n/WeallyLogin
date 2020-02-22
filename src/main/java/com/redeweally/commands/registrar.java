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

public class registrar implements CommandExecutor {

    private Main m = Main.plugin;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getLabel().equalsIgnoreCase("registrar")) {

            Player player = (Player) sender;

            if(!m.uname.contains(player.getName())){

                if (args.length == 3) {
                    if(args[0].length() < 6){
                        player.sendMessage("§cSua senha deve conter pelo menos 6 caracteres.");
                        return false;
                    }
                    if (args[2].contains("@") && args[2].contains(".com")) {
                        if (args[0].equals(args[1])) {
                            User p = new User();
                            p.setUUID(player.getUniqueId());
                            p.setPassword(args[0]);
                            p.setNome(player.getName());
                            p.setLastip("" + player.getAddress().getAddress().getHostAddress());
                            p.setIpregister("" + player.getAddress().getAddress().getHostAddress());
                            p.setEmail(args[2]);
                            p.setDiscord("nenhum");

                            m.users.add(p);
                            m.nlogados.remove(p.getNome());
                            m.uname.add(p.getNome());

                            player.playSound(player.getLocation(), Sound.LEVEL_UP, .3f, 2f);
                            NaxsiUtils.send(player, "§a§lAUTENTICADO!!", "§fVocê está sendo enviado para o lobby...", 0, 5, 0);

                            NaxsiUtils.sendPlayerToServer(player, "Lobby");

                            Connection.addPlayer(p);
                        } else {
                            player.sendMessage("§cAs senhas que você inseriu, não são iguais.");
                        }
                    } else {
                        player.sendMessage("§cEste email não é valido.");
                    }

                } else {
                    player.sendMessage("§cUtilize /registrar <senha> <confirmarsenha> <email>");
                }
            } else {
                player.sendMessage("§cVocê já se encontra registrado em nosso servidor.");
            }
        }

        return false;
    }
}
