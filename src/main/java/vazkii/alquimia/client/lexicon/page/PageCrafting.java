package vazkii.alquimia.client.lexicon.page;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
	
	String recipe;
	String text;
	
	transient IRecipe recipeObj;
	transient LexiconTextRenderer textRender;
	
	@Override
	public void build(LexiconEntry entry, int pageNum) {
		super.build(entry, pageNum);
		
		recipeObj = CraftingManager.getRecipe(new ResourceLocation(recipe));
		if(recipeObj != null)
			entry.addRelevantStack(recipeObj.getRecipeOutput(), pageNum);
	}
	
	@Override
	public void onDisplayed(GuiLexiconEntry parent) {
		super.onDisplayed(parent);
		
		textRender = new LexiconTextRenderer(parent, text, 0, 100);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		if(recipeObj != null) {
			mc.renderEngine.bindTexture(OVERLAY_TEXTURE);
			GlStateManager.enableBlend();
			
			int recipeX = GuiLexicon.PAGE_WIDTH / 2 - 49;
			int recipeY = 25;
			parent.drawModalRectWithCustomSizedTexture(recipeX, recipeY, 0, 0, 98, 60, 128, 128);
			
			ItemStack output = recipeObj.getRecipeOutput();
			parent.drawCenteredStringNoShadow(output.getDisplayName(), GuiLexicon.PAGE_WIDTH / 2, 0, 0x333333);
			parent.drawSeparator(0, 12);
			
			renderItem(recipeX + 79, recipeY + 22, mouseX, mouseY, output);
			
			NonNullList<Ingredient> ingredients = recipeObj.getIngredients();
			int wrap = 3;
			if(recipeObj instanceof IShapedRecipe)
				wrap = ((IShapedRecipe) recipeObj).getRecipeWidth();
			
			for(int i = 0; i < ingredients.size(); i++)
				renderIngredient(recipeX + (i % wrap) * 19 + 3, recipeY + (i / wrap) * 19 + 3, mouseX, mouseY, ingredients.get(i));
		}
		
		textRender.render(mouseX, mouseY);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		textRender.click(mouseX, mouseY, mouseButton);
	}

}
