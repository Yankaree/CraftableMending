package me.yankaree.mending.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class MendingRecipeListener {

    public static void register() {
        // Lắng nghe khi player sử dụng crafting table
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClientSide && player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                
                // Kiểm tra xem player đang mở crafting table không
                if (serverPlayer.containerMenu instanceof CraftingMenu) {
                    scheduleCheck(serverPlayer);
                }
            }
            return InteractionResult.PASS;
        });
    }

    /**
     * Kiểm tra và thay thế Enchanted Book nếu recipe match
     */
    private static void scheduleCheck(ServerPlayer player) {
        // Dùng next tick để đảm bảo recipe đã được process
        player.server.submit(() -> {
            if (player.containerMenu instanceof CraftingMenu) {
                CraftingMenu craftingMenu = (CraftingMenu) player.containerMenu;
                ItemStack resultSlot = craftingMenu.resultSlots.getItem(0);
                
                // Kiểm tra xem result có phải enchanted book không
                if (!resultSlot.isEmpty() && resultSlot.is(Items.ENCHANTED_BOOK)) {
                    // Kiểm tra input có match Mending recipe không
                    if (CraftingEventHandler.isMendingRecipeMatch(craftingMenu.craftSlots)) {
                        // Check xem có Mending enchant chưa
                        if (!resultSlot.getEnchantmentLevel(player.level().registryAccess().lookupOrThrow(
                                net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(
                                net.minecraft.world.item.enchantment.Enchantments.MENDING)) > 0) {
                            
                            // Tạo Enchanted Book với Mending
                            ItemStack mendingBook = CraftingEventHandler.createMendingBook(
                                player.level().registryAccess()
                            );
                            
                            // Thay thế result
                            craftingMenu.resultSlots.setItem(0, mendingBook);
                        }
                    }
                }
            }
        });
    }
}
