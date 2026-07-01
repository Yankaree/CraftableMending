package me.yankaree.mending.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MendingBookRecipeSerializer implements RecipeSerializer<MendingBookRecipe> {
	public static final MendingBookRecipeSerializer INSTANCE = new MendingBookRecipeSerializer();

	private MendingBookRecipeSerializer() {
	}

	@Override
	public MendingBookRecipe fromJson(Identifier id, JsonObject json) {
		NonNullList<Ingredient> ingredients = readIngredients(json);
		return new MendingBookRecipe(ingredients);
	}

	@Override
	public MendingBookRecipe fromNetwork(Identifier id, FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
		for (int i = 0; i < size; i++) {
			ingredients.set(i, Ingredient.fromNetwork(buf));
		}
		return new MendingBookRecipe(ingredients);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, MendingBookRecipe recipe) {
		buf.writeVarInt(recipe.getIngredients().size());
		for (Ingredient ingredient : recipe.getIngredients()) {
			ingredient.toNetwork(buf);
		}
	}

	private static NonNullList<Ingredient> readIngredients(JsonObject json) {
		JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "ingredients");
		NonNullList<Ingredient> ingredients = NonNullList.create();
		for (int i = 0; i < jsonArray.size(); i++) {
			Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
			if (!ingredient.isEmpty()) {
				ingredients.add(ingredient);
			}
		}
		return ingredients;
	}
}
