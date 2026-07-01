package me.yankaree.mending.mixin;

import me.yankaree.mending.event.CraftingEventListener;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultContainer.class)
public class ResultContainerMixin {

    @Shadow
    private ItemStack[] itemStacks;

    /**
     * Intercept result container setItem
     * Khi result được set, check xem có phải enchanted book từ Mending recipe không
     * Nếu đúng → replace bằng Enchanted Book với Mending enchant
     */
    @Inject(method = "setItem", at = @At("HEAD"), cancellable = true)
    private void onSetItem(int slot, ItemStack itemStack, CallbackInfo ci) {
        try {
            // Chỉ xử lý khi đó là enchanted book (từ Bedrock recipe)
            if (!itemStack.isEmpty() && itemStack.is(Items.ENCHANTED_BOOK)) {
                // Đây là enchanted book thường từ vanilla recipe
                // Cần thêm Mending enchant nếu đó là từ Mending Book recipe
                // Sẽ xử lý ở mixin khác hoặc event listener
            }
        } catch (Exception e) {
            System.out.println("[CraftableMending] Lỗi trong ResultContainerMixin: " + e.getMessage());
        }
    }
}
