package me.yankaree.mending.mixin;

import me.yankaree.mending.event.CraftingEventListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingContainer.class)
public class CraftingContainerMixin {

    /**
     * Intercept crafting result update
     * Khi player craft, check xem có phải Mending Book recipe không
     * Nếu đúng → thay thế result bằng Enchanted Book với Mending
     */
    @Inject(method = "slotChanged", at = @At("HEAD"))
    private void onSlotChanged(CallbackInfo ci) {
        try {
            CraftingContainer container = (CraftingContainer) (Object) this;
            
            // Kiểm tra xem recipe có match không
            if (CraftingEventListener.isMendingRecipeMatch(container)) {
                // Không làm gì ở đây, để ResultContainer handle ở mixin khác
                // hoặc để vanilla recipe tạo enchanted book thường
            }
        } catch (Exception e) {
            System.out.println("[CraftableMending] Lỗi trong CraftingContainerMixin: " + e.getMessage());
        }
    }
}
