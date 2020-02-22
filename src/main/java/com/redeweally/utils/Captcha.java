package com.redeweally.utils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Captcha {

    public static void executeCaptcha(Player p) {
        Inventory inv = Bukkit.createInventory(p, 5 * 9, "Clique na cabeça verde.");

        ItemStack cverde = NaxsiUtils.getSkull("http://textures.minecraft.net/texture/22d145c93e5eac48a661c6f27fdaff5922cf433dd627bf23eec378b9956197");
        ItemMeta cverdemeta = cverde.getItemMeta();
        cverdemeta.setDisplayName("§7Cabeça");
        cverde.setItemMeta(cverdemeta);

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(cverde);
        NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();

        itemCompound.set("tipo", new NBTTagString("verde"));

        nmsItem.setTag(itemCompound);

        cverde = CraftItemStack.asBukkitCopy(nmsItem);

        ItemStack ccinza = NaxsiUtils.getSkull("http://textures.minecraft.net/texture/2a17e97037ce353f85f5c65df435d29449a88da4442e4361cf99abbe1f892fb");
        ItemMeta ccinzameta = ccinza.getItemMeta();
        ccinzameta.setDisplayName("§7Cabeça");
        ccinza.setItemMeta(ccinzameta);

        net.minecraft.server.v1_8_R3.ItemStack nmsItem1 = CraftItemStack.asNMSCopy(ccinza);
        NBTTagCompound itemCompound1 = (nmsItem1.hasTag()) ? nmsItem1.getTag() : new NBTTagCompound();

        itemCompound1.set("tipo", new NBTTagString("cinza"));

        nmsItem1.setTag(itemCompound1);

        ccinza = CraftItemStack.asBukkitCopy(nmsItem1);


        int verde = new Random().nextInt(44);
        while (verde <= 9 || verde == 16 || verde == 17 || verde == 24 || verde == 25 || verde >= 32) {
            verde = new Random().nextInt(44);
        }
        int slot = 10;
        inv.setItem(verde, cverde);
        for (int i = 0; i < 45; i++) {
            if (slot <= 9 || slot == 17 || slot == 18 || slot == 26 || slot == 27 || slot >= 35 || slot == verde) {
                slot++;
            } else {
                inv.setItem(slot, ccinza);
                slot++;
            }
        }

        p.openInventory(inv);


    }

}
