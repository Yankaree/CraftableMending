package me.yankaree.mending.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MendingBookRecipeSerializer extends RecipeSerializer<MendingBookRecipe> {
	public static final MendingBookRecipeSerializer INSTANCE = new MendingBookRecipeSerializer();

	public MendingBookRecipe fromJson(Identifier id, JsonObject json) {
		NonNullList<Ingredient> ingredients = new NonNullList<>(Ingredient.class, 9);
		JsonArray jsonArray = json.getAsJsonArray("ingredients");
		for (int i = 0; i < jsonArray.size(); i++) {
			Ingredient ingredient = Ingredient.CODEC.parse(jsonArray.get(i));
			if (!ingredient.isEmpty()) {
				ingredients.add(ingredient);
			}
		}
		if (ingredients.isEmpty()) {
			return null;
		}
		return new MendingBookRecipe(ingredients);
	}

	public MendingBookRecipe fromNetwork(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		NonNullList<Ingredient> ingredients = new NonNullList<>(Ingredient.class, size);
		for (int i = 0; i < size; i++) {
			ingredients.add(Ingredient.CODEC.decode(buf.readWithCodec(Ingredient.CODEC)));
		}
		return new MendingBookRecipe(ingredients);
	}

	public void toNetwork(FriendlyByteBuf buf, MendingBookRecipe recipe) {
		buf.writeVarInt(recipe.getIngredients().size());
		for (Ingredient ingredient : recipe.getIngredients()) {
			buf.writeWithCodec(Ingredient.CODEC, ingredient);
		}
	}
}
