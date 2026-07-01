package me.yankaree.mending.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class MendingBookRecipe implements Recipe<CraftingInput> {
    private final NonNullList<Ingredient> ingredients;

    public MendingBookRecipe(NonNullList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        java.util.List<ItemStack> items = new java.util.ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }

        if (items.size() != 9) {
            return false;
        }

        int diamondCount = 0;
        int goldCount = 0;
        int ironCount = 0;

        for (ItemStack stack : items) {
            if (stack.is(Items.DIAMOND)) {
                diamondCount++;
            } else if (stack.is(Items.GOLD_INGOT)) {
                goldCount++;
            } else if (stack.is(Items.IRON_INGOT)) {
                ironCount++;
            } else {
                return false;
            }
        }

        return diamondCount == 4 && goldCount == 4 && ironCount == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, net.minecraft.core.RegistryAccess registryAccess) {
        ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
        Map<net.minecraft.world.item.enchantment.Enchantment, Integer> ench = new HashMap<>();
        ench.put(Enchantments.MENDING, 1);
        EnchantmentHelper.setEnchantments(ench, stack);
        return stack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.RegistryAccess registryAccess) {
        return new ItemStack(Items.ENCHANTED_BOOK);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategory.CRAFTING;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public RecipeSerializer<? extends Recipe<CraftingInput>> getSerializer() {
        return MendingBookRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<? extends Recipe<CraftingInput>> getType() {
        return RecipeType.CRAFTING;
    }
}