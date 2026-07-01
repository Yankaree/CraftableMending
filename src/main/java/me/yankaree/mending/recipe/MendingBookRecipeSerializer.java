package me.yankaree.mending.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonOps;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MendingBookRecipeSerializer implements RecipeSerializer<MendingBookRecipe> {
	public static final MendingBookRecipeSerializer INSTANCE = new MendingBookRecipeSerializer();

	private MendingBookRecipeSerializer() {}

	@Override
	public MendingBookRecipe fromJson(Identifier id, JsonObject json) {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "ingredients");
		for (int i = 0; i < jsonArray.size(); i++) {
			Ingredient ingredient = Ingredient.CODEC.parse(JsonOps.INSTANCE, jsonArray.get(i)).result().orElseThrow();
			if (!ingredient.isEmpty()) {
				ingredients.add(ingredient);
			}
		}
		return new MendingBookRecipe(ingredients);
	}

	@Override
	public MendingBookRecipe fromNetwork(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		NonNullList<Ingredient> ingredients = NonNullList.create();
		for (int i = 0; i < size; i++) {
			ingredients.add(buf.readWithCodec(NbtOps.INSTANCE, Ingredient.CODEC, NbtAccounter.unlimitedHeap()));
		}
		return new MendingBookRecipe(ingredients);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, MendingBookRecipe recipe) {
		buf.writeVarInt(recipe.getIngredients().size());
		for (Ingredient ingredient : recipe.getIngredients()) {
			buf.writeWithCodec(NbtOps.INSTANCE, Ingredient.CODEC, ingredient);
		}
	}
}
