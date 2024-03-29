package me.xflyiwnl.cities.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {

    public static int countItems(Material material, Inventory inventory) {

        int has = 0;

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() != material) continue;
            has += itemStack.getAmount();
        }

        return has;
    }

    public static boolean hasItems(Material material, int count, Inventory inventory) {

        int has = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() != material) continue;
            has += itemStack.getAmount();

            if (has >= count) return true;
        }

        return false;
    }

    public static void takeItems(Material material, int amount, Inventory inventory) {
        int deleted = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) continue;
            if (deleted == amount) break;
            if (itemStack.getType() != material) continue;

            if (deleted + itemStack.getAmount() > amount) {
                itemStack.setAmount(itemStack.getAmount() - (amount - deleted));
                deleted = amount;
            } else {
                deleted += itemStack.getAmount();
                itemStack.setAmount(0);
            }

        }

    }

    public static void takeItems(Material material, Inventory inventory) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() != material) continue;
            itemStack.setAmount(0);
        }

    }

}
