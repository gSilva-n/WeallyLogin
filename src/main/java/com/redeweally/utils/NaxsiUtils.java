package com.redeweally.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.redeweally.login.Main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

import com.redeweally.manager.PingResponse;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class NaxsiUtils{

    private static Main m = Main.plugin;

    private static HashMap<String, PingResponse> servers = new HashMap<>();

    public static boolean isSameBlock(Location from, Location to) {
        boolean aux = true;

        int fromX = (int) from.getX();
        int fromZ = (int) from.getZ();

        int toX = (int) to.getX();
        int toZ = (int) to.getZ();

        if (fromX != toX || fromZ != toZ) {
            aux = false;
        }

        return aux;
    }

    public static void sendPlayerToServer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (Exception e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cErro ao se conectar ao " + server + "!"));
            return;
        }

        player.sendPluginMessage(m, "BungeeCord", b.toByteArray());
    }

    public static void sendLobbyBunger(Player p) {
        boolean aux = true;
        int server = 1;
        while (aux) {
            try {
                if(server > 3){
                    aux = false;
                }
                String path = "lobbies.lobby" + server;
                String lobby = m.getConfig().getString(path);
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(lobby);
                p.sendPluginMessage(m, "BungeeCord", out.toByteArray());
                aux = false;
            } catch (Exception e) {
                server++;
            }
        }

    }
    public static void loadServers() {
        HashMap<String, PingResponse> configServers = new HashMap<>();

        String server = "LOBBY";
        String ip = "0.0.0.0";
        Integer port = 25567;

        PingResponse ping = new PingResponse();
        ping.setAddress(new InetSocketAddress(ip, port));

        configServers.put(server, ping);

        servers = configServers;
    }

    public static void connect(Player p, String server) {
        try {
            PingResponse.StatusResponse response = servers.get(server.toUpperCase()).fetchData();

            if(response.getPlayers().getOnline() < response.getPlayers().getMax()) {
                connectTo(p, server.toUpperCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connectTo(Player p, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.sendPluginMessage(m, "BungeeCord", b.toByteArray());
    }

    public static void send(final Player player, final String title, final String subtitle, final int fadeInTime, final int showTime, final int fadeOutTime) {
        try {
            final Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
            final Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            final Object packet = titleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, fadeInTime, showTime, fadeOutTime);
            final Object chatsTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
            final Constructor<?> timingTitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            final Object timingPacket = timingTitleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle, fadeInTime, showTime, fadeOutTime);
            sendPacket(player, packet);
            sendPacket(player, timingPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (url.isEmpty()) return head;


        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static void sendHeaderAndFooter(Player p, String head, String foot) {
        CraftPlayer craftplayer = (CraftPlayer)p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent header = IChatBaseComponent.ChatSerializer.a("{'color': '', 'text': '" + head + "'}");
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer.a("{'color': '', 'text': '" + foot + "'}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, header);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footer);
            footerField.setAccessible(!footerField.isAccessible());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        connection.sendPacket(packet);
    }

    public static void sendMessage(Player player, String message) {
        PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
        IChatBaseComponent chat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) 2);
        con.sendPacket(packet);
    }

    public static void timer(final Player p){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(m, new Runnable() {
            int time = 30;

            @Override
            public void run() {

                if(m.nlogados.contains(p.getName())) {
                    if (time == 0) {
                        Bukkit.getScheduler().runTask(m, new Runnable() {
                            public void run() {
                                p.kickPlayer("§c§lREDE WEALLY\n\n§cVocê excedeu nosso tempo de 30 segundos.\nEntre e tente novamente.");
                            }
                        });
                    }
                    sendMessage(p, "§cVocê tem " + time + " §csegundos para se autenticar!");
                }


                this.time--;
            }
        }, 0L, 20L);
    }



}
