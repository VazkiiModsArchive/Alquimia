package vazkii.alquimia.client.lexicon.page.abstr;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.common.crafting.CrucibleRecipe;
import vazkii.alquimia.common.crafting.CrucibleRecipes;

public abstract class PageDoubleRecipe<T> extends PageWithText {

	String recipe, recipe2;

	protected transient T recipeObj1, recipeObj2;
	
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
	public void render(int mouseX, int mouseY, float pticks) {
		if(recipeObj1 != null) {
			int recipeX = getX();
			int recipeY = getY();
			drawRecipe(recipeObj1, recipeX, recipeY, mouseX, mouseY, false);
			
			if(recipeObj2 != null)
				drawRecipe(recipeObj2, recipeX, recipeY + getRecipeHeight(), mouseX, mouseY, true);
		}
		
		super.render(mouseX, mouseY, pticks);
	}
	
	@Override
	public int getTextHeight() {
		return getY() + getRecipeHeight() * (recipeObj2 == null ? 1 : 2) - 13;
	}
	
	@Override
	public boolean shouldRenderText() {
		return getTextHeight() + 10 < GuiLexicon.PAGE_HEIGHT;
	}
	
	protected abstract void drawRecipe(T recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second);
	protected abstract T loadRecipe(LexiconEntry entry, String loc);
	protected abstract int getRecipeHeight();
	
	protected int getX() {
		return GuiLexicon.PAGE_WIDTH / 2 - 49;
	}
	
	protected int getY() {
		return 4;
	}

}
