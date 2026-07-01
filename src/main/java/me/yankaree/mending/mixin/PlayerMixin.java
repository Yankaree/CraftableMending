package me.yankaree.mending.mixin;

import me.yankaree.mending.event.CraftingEventListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    /**
     * Lắng nghe khi player thay đổi crafting input
     * Nếu input match Mending recipe và result là enchanted book → apply Mending enchant
     */
    @Inject(method = "containerChanged", at = @At("HEAD"))
    private void onContainerChanged(net.minecraft.world.inventory.AbstractContainerMenu container, CallbackInfo ci) {
        try {
            Player player = (Player) (Object) this;
            
            // Kiểm tra xem container có phải crafting menu không
            if (container instanceof CraftingMenu) {
                CraftingMenu craftingMenu = (CraftingMenu) container;
                CraftingContainer craftingContainer = craftingMenu.craftSlots;
                
                // Kiểm tra xem input có match Mending recipe không
                if (CraftingEventListener.isMendingRecipeMatch(craftingContainer)) {
                    // Lấy result slot
                    ItemStack resultSlot = craftingMenu.resultSlots.getItem(0);
                    
                    // Nếu result là enchanted book thường → replace bằng Mending book
                    if (!resultSlot.isEmpty() && resultSlot.is(Items.ENCHANTED_BOOK)) {
                        ItemStack mendingBook = CraftingEventListener.createMendingBook(
                            player.level().registryAccess()
                        );
                        
                        // Copy components từ mending book
                        if (mendingBook.getComponentsPatch() != null) {
                            resultSlot.applyComponentsAndValidate(mendingBook.getComponentsPatch());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[CraftableMending] Lỗi trong PlayerMixin: " + e.getMessage());
        }
    }
}
