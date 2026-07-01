package me.yankaree.mending.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MendingBookRecipeSerializer implements RecipeSerializer<MendingBookRecipe> {
	public static final MendingBookRecipeSerializer INSTANCE = new MendingBookRecipeSerializer();

	public MendingBookRecipe fromJson(Identifier id, JsonObject json) {
		NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
		JsonArray jsonArray = json.getAsJsonArray("ingredients");
		for (int i = 0; i < Math.min(jsonArray.size(), 9); i++) {
			Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
			if (!ingredient.isEmpty()) {
				ingredients.set(i, ingredient);
			}
		}
		return new MendingBookRecipe(ingredients);
	}

	public MendingBookRecipe fromNetwork(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
		for (int i = 0; i < size; i++) {
			ingredients.set(i, Ingredient.fromNetwork(buf));
		}
		return new MendingBookRecipe(ingredients);
	}

	public void toNetwork(FriendlyByteBuf buf, MendingBookRecipe recipe) {
		buf.writeVarInt(recipe.getIngredients().size());
		for (Ingredient ingredient : recipe.getIngredients()) {
			ingredient.toNetwork(buf);
		}
	}
}
