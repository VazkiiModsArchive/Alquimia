package vazkii.alquimia.client.lexicon.page;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;
import vazkii.alquimia.common.crafting.CrucibleRecipe;
import vazkii.alquimia.common.crafting.CrucibleRecipes;

public class PageCrucible extends LexiconPage {

	String recipe, recipe2;
	String text;
	
	transient CrucibleRecipe recipeObj1, recipeObj2;
	transient LexiconTextRenderer textRender;
	
	@Override
	public void build(LexiconEntry entry, int pageNum) {
		super.build(entry, pageNum);
		
		recipeObj1 = loadRecipe(entry, recipe);
		recipeObj2 = loadRecipe(entry, recipe2);
		
		if(recipeObj1 == null && recipeObj2 != null) {
			recipeObj1 = recipeObj2;
			recipeObj2 = null;
		}
	}
	
	@Override
	public void onDisplayed(GuiLexiconEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		
		textRender = new LexiconTextRenderer(parent, text, 0, recipeObj2 == null ? 56 : 120);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		if(recipeObj1 != null) {
			int recipeX = GuiLexicon.PAGE_WIDTH / 2 - 49;
			int recipeY = 4;
			drawRecipe(recipeObj1, recipeX, recipeY, mouseX, mouseY);
			
			if(recipeObj2 != null)
				drawRecipe(recipeObj2, recipeX, recipeY + 64, mouseX, mouseY);
		}
		
		textRender.render(mouseX, mouseY);
	}
	
	void drawRecipe(CrucibleRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		mc.renderEngine.bindTexture(GuiLexicon.CRAFTING_TEXTURE);
		GlStateManager.enableBlend();
		parent.drawModalRectWithCustomSizedTexture(recipeX, recipeY + 2, 11, 64, 100, 36, 128, 128);
		
		parent.drawCenteredStringNoShadow(recipe.output.getDisplayName(), GuiLexicon.PAGE_WIDTH / 2, recipeY - 10, 0x333333);
		
		renderItem(recipeX + 76, recipeY + 13, mouseX, mouseY, recipe.output);
		renderIngredient(recipeX + 4, recipeY + 13, mouseX, mouseY, recipe.ingredient);
		
		float secs = (float) recipe.time / 20;
		boolean whole = Math.floor(secs) == secs;
		boolean unicode = mc.fontRenderer.getUnicodeFlag();
		String s = String.format(whole ? "%.0fs" : "%.1fs", secs);
		mc.fontRenderer.setUnicodeFlag(true);
		parent.drawCenteredStringNoShadow(s, recipeX + 49, recipeY + 38, 0x555555);
		mc.fontRenderer.setUnicodeFlag(unicode);
	}
	
	CrucibleRecipe loadRecipe(LexiconEntry entry, String loc) {
		if(loc == null)
			return null;
		
		CrucibleRecipe tempRecipe = CrucibleRecipes.RECIPE_MAPPING.get(new ResourceLocation(loc));
		if(tempRecipe != null)
			entry.addRelevantStack(tempRecipe.output, pageNum);
		return tempRecipe;
	}


}
