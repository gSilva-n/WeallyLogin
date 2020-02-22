package com.redeweally.connection;

import com.redeweally.login.Main;
import com.redeweally.manager.User;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class Connection {

    public static Main m = Main.plugin;

    public static java.sql.Connection con = null;

    public static void openConnection() {
        if (Main.MySQL) {
            String USER = "root";
            String HOST = "localhost";
            String PASS = "";
            String PORT = "3306";
            String DATA = "servidor";

            try {
                String type = "jdbc:mysql://";
                String url = type + HOST + ":" + PORT + "/" + DATA;

                con =  DriverManager.getConnection(url, USER, PASS);

                createTable();
                System.out.println("§aSuccessful MYSql connection");
            } catch (SQLException e) {
                System.out.println("§cMYSql connection error changing to SQLite // LN: 34");
                SQLite();
                e.printStackTrace();
            }
        } else {
            SQLite();
        }
    }

    public static void closeConnection(){
        try {
            con.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void SQLite() {
        File file = new File(m.getDataFolder(), "database.db");

        String URL = "jdbc:sqlite:" + file;

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(URL);
            System.out.println(" §aSucefull SQLite connection");
            createTable();
        } catch (Exception e) {
            System.out.println(" §cCritical error, disabling plugin // LN: 53");
            Bukkit.getPluginManager().disablePlugin(m);
            e.printStackTrace();
        }
    }

    public static void createTable() {
        try {
            PreparedStatement stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS `users` (`nome` VARCHAR(20) UNIQUE not null, `email` VARCHAR(60), `senha` VARCHAR(100) NOT NULL, `discord` VARCHAR(100), `uuid` TEXT NOT NULL, `ipregister` VARCHAR(200) NOT NULL, `lastip` VARCHAR(200) NOT NULL);");
            stm.execute();
            stm.close();
            System.out.println("CRIADO COM SUCESSO");
        } catch (SQLException e) {
            System.out.println(" Critical error, disabling plugin // LN: 59");
            Bukkit.getPluginManager().disablePlugin(m);
            e.printStackTrace();
        }

    }


    public static void addPlayer(User usuario){
        String nome = usuario.getNome();
        String senha = usuario.getPassword();
        String email = usuario.getEmail();
        String discord = usuario.getDiscord();
        String ipregister = usuario.getIpregister();
        UUID uiid = usuario.getUUID();
        String lastip = usuario.getLastip();

        try {
                PreparedStatement stm = con.prepareStatement("INSERT INTO `users` (`nome`, `email`, `senha`, `discord`, `uuid`, `ipregister`, `lastip`) VALUES (?, ?, ?, ?, ?, ?, ?);");
                stm.setString(1, nome);
                stm.setString(2, email);
                stm.setString(3, senha);
                stm.setString(4, discord);
                stm.setString(5, uiid.toString());
                stm.setString(6, ipregister);
                stm.setString(7, lastip);
                System.out.println(nome);
                stm.execute();
                stm.close();
        } catch (SQLException e) {
            try {
                PreparedStatement stm = con.prepareStatement("UPDATE `users` SET `nome` = ?, `email` = ?, `senha` = ?, `discord` = ?, `uuid` = ?, `ipregister` = ?, `lastip` = ? WHERE nome = ?;");
                stm.setString(1, nome);
                stm.setString(2, email);
                stm.setString(3, senha);
                stm.setString(4, discord);
                stm.setString(5, uiid.toString());
                stm.setString(6, ipregister);
                stm.setString(7, lastip);
                stm.setString(8, nome);
                System.out.println(nome);
                stm.execute();
                stm.close();
            }catch (Exception err){
                System.out.println("ERRO // LN 109");
            }
        }
    }

    public static void loadUsers() {
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM `users`");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setDiscord(rs.getString("discord"));
                u.setEmail(rs.getString("email"));
                u.setIpregister(rs.getString("ipregister"));
                u.setLastip(rs.getString("lastip"));
                u.setNome(rs.getString("nome"));
                u.setPassword(rs.getString("senha"));
                u.setUUID(UUID.fromString(rs.getString("uuid")));
                m.users.add(u);
                m.uname.add(u.getNome());
                System.out.println(u.getNome());
            }
            stm.close();
            System.out.println("§aUsuarios carregados");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.out.println("§cERRO AO PEGAR TODOS OS USUÁRIOS");
            Bukkit.getPluginManager().disablePlugin(m);
        }
    }

    public static boolean containsUser(String name) {
        boolean aux = false;
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM `users` WHERE `nome` LIKE '?'");
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                aux = true;
            }
            stm.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return aux;
    }

}
