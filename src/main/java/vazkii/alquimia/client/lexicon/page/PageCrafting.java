package vazkii.alquimia.client.lexicon.page;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.crafting.IShapedRecipe;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.util.RenderHelper;

public class PageCrafting extends LexiconPage {

	public static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/lexicon/crafting.png"); 
	
	String recipe, recipe2;
	String text;
	
	transient IRecipe recipeObj1, recipeObj2;
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
	public void onDisplayed(GuiLexiconEntry parent) {
		super.onDisplayed(parent);
		
		textRender = new LexiconTextRenderer(parent, text, 0, 72);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		if(recipeObj1 != null) {
			int recipeX = GuiLexicon.PAGE_WIDTH / 2 - 49;
			int recipeY = 4;
			drawRecipe(recipeObj1, recipeX, recipeY, mouseX, mouseY);
			
			if(recipeObj2 != null)
				drawRecipe(recipeObj2, recipeX, recipeY + 78, mouseX, mouseY);
		}
		
		if(recipeObj2 == null)
			textRender.render(mouseX, mouseY);
	}
	
	void drawRecipe(IRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		mc.renderEngine.bindTexture(OVERLAY_TEXTURE);
		GlStateManager.enableBlend();
		parent.drawModalRectWithCustomSizedTexture(recipeX, recipeY, 0, 0, 98, 60, 128, 128);
		
		boolean shaped = recipe instanceof IShapedRecipe;
		if(!shaped) {
			int iconX = recipeX + 62;
			int iconY = recipeY + 2;
			parent.drawModalRectWithCustomSizedTexture(iconX, iconY, 0, 60, 11, 11, 128, 128);
			if(parent.isMouseInRelativeRange(mouseX, mouseY, iconX, iconY, 11, 11))
				parent.setTooltip(I18n.translateToLocal("alquimia.gui.lexicon.shapeless"));
		}

		ItemStack output = recipe.getRecipeOutput();
		parent.drawCenteredStringNoShadow(output.getDisplayName(), GuiLexicon.PAGE_WIDTH / 2, recipeY - 10, 0x333333);
		
		renderItem(recipeX + 79, recipeY + 22, mouseX, mouseY, output);
		
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		int wrap = 3;
		if(shaped)
			wrap = ((IShapedRecipe) recipe).getRecipeWidth();
		
		for(int i = 0; i < ingredients.size(); i++)
			renderIngredient(recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
	}
	
	IRecipe loadRecipe(LexiconEntry entry, String loc) {
		if(loc == null)
			return null;
		
		IRecipe tempRecipe = CraftingManager.getRecipe(new ResourceLocation(loc));
		if(tempRecipe != null)
			entry.addRelevantStack(tempRecipe.getRecipeOutput(), pageNum);
		return tempRecipe;
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		textRender.click(mouseX, mouseY, mouseButton);
	}

}
