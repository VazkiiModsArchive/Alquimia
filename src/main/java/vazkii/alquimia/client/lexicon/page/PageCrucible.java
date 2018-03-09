package vazkii.alquimia.client.lexicon.page;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;
import vazkii.alquimia.client.lexicon.page.abstr.PageDoubleRecipe;
import vazkii.alquimia.client.lexicon.page.abstr.PageWithText;
import vazkii.alquimia.common.crafting.CrucibleRecipe;
import vazkii.alquimia.common.crafting.CrucibleRecipes;

public class PageCrucible extends PageDoubleRecipe<CrucibleRecipe> {

	@Override
	protected void drawRecipe(CrucibleRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
		mc.renderEngine.bindTexture(GuiLexicon.CRAFTING_TEXTURE);
		GlStateManager.enableBlend();
		parent.drawModalRectWithCustomSizedTexture(recipeX, recipeY + 2, 11, 64, 100, 36, 128, 128);
		
		parent.drawCenteredStringNoShadow(recipe.output.getDisplayName(), GuiLexicon.PAGE_WIDTH / 2, recipeY - 10, 0x333333);
		
		renderItem(recipeX + 76, recipeY + 13, mouseX, mouseY, recipe.output);
		renderIngredient(recipeX + 4, recipeY + 13, mouseX, mouseY, recipe.ingredient);
		
		float secs = (float) recipe.time / 20;
		boolean whole = Math.floor(secs) == secs;
		boolean unicode = mc.fontRenderer.getUnicodeFlag();
		String s = I18n.translateToLocalFormatted("alquimia.gui.lexicon.seconds_" + (whole ? "whole" : "fract"), secs);
		mc.fontRenderer.setUnicodeFlag(true);
		parent.drawCenteredStringNoShadow(s, recipeX + 49, recipeY + 38, 0x555555);
		mc.fontRenderer.setUnicodeFlag(unicode);
	}
	
	@Override
	protected CrucibleRecipe loadRecipe(LexiconEntry entry, String loc) {
		if(loc == null)
			return null;
		
		CrucibleRecipe tempRecipe = CrucibleRecipes.RECIPE_MAPPING.get(new ResourceLocation(loc));
		if(tempRecipe != null)
			entry.addRelevantStack(tempRecipe.output, pageNum);
		return tempRecipe;
	}
	
	@Override
	protected int getRecipeHeight() {
		return 64;
	}

}
