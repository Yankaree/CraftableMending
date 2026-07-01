package me.yankaree.mending.event;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class CraftingEventListener {

    public static void register() {
        // Không cần register ở đây, mixin sẽ handle tất cả
    }

    /**
     * Kiểm tra xem crafting input có match Mending Book recipe không
     * 4 kim cương + 4 vàng + 1 sắt
     */
    public static boolean isMendingRecipeMatch(CraftingContainer container) {
        int diamondCount = 0;
        int goldCount = 0;
        int ironCount = 0;
        int totalItems = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                totalItems++;
                if (stack.is(Items.DIAMOND)) {
                    diamondCount++;
                } else if (stack.is(Items.GOLD_INGOT)) {
                    goldCount++;
                } else if (stack.is(Items.IRON_INGOT)) {
                    ironCount++;
                } else {
                    return false; // Có item không phải của recipe
                }
            }
        }

        // Phải có đúng 9 item: 4 kim cương + 4 vàng + 1 sắt
        return totalItems == 9 && diamondCount == 4 && goldCount == 4 && ironCount == 1;
    }

    /**
     * Tạo Enchanted Book với Mending enchantment
     */
    public static ItemStack createMendingBook(RegistryAccess registryAccess) {
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        
        try {
            var enchantmentRegistry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
            var mending = enchantmentRegistry.getOrThrow(Enchantments.MENDING);
            var enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            enchantments.set(mending, 1);
            EnchantmentHelper.setEnchantments(enchantedBook, enchantments.toImmutable());
        } catch (Exception e) {
            // Nếu không tìm thấy Mending enchant, vẫn return enchanted book
            System.out.println("[CraftableMending] Lỗi khi áp dụng Mending enchant: " + e.getMessage());
        }
        
        return enchantedBook;
    }
}
