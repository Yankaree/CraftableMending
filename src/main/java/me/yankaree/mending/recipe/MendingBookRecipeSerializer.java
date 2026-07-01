package me.yankaree.mending.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class MendingBookRecipeSerializer {
	public static final RecipeSerializer<MendingBookRecipe> INSTANCE = new RecipeSerializer<>(
		codec(),
		streamCodec()
	);

	private static MapCodec<MendingBookRecipe> codec() {
		return RecordCodecBuilder.mapCodec(instance ->
			instance.group(
				Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe ->
					recipe.getIngredients().stream().toList()
				)
			).apply(instance, list -> {
				NonNullList<Ingredient> ingredients = NonNullList.create();
				ingredients.addAll(list);
				return new MendingBookRecipe(ingredients);
			})
		);
	}

	private static StreamCodec<RegistryFriendlyByteBuf, MendingBookRecipe> streamCodec() {
		return StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
			recipe -> recipe.getIngredients().stream().toList(),
			list -> {
				NonNullList<Ingredient> ingredients = NonNullList.create();
				ingredients.addAll(list);
				return new MendingBookRecipe(ingredients);
			}
		);
	}
}
