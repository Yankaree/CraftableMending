package me.yankaree.mending;

import net.fabricmc.api.ModInitializer;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import me.yankaree.mending.recipe.MendingBookRecipeSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CraftableMending implements ModInitializer {
	public static final String MOD_ID = "craftablemending";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			id("mending_book"),
			MendingBookRecipeSerializer.INSTANCE
		);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
