package vazkii.alquimia.client.lexicon.page;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;
import vazkii.alquimia.common.util.ItemStackUtil;

public class PageSpotlight extends LexiconPage {

	String text;
	String item;
	boolean link_recipe;

	transient LexiconTextRenderer textRender;
	transient ItemStack itemStack;
	
	@Override
	public void build(LexiconEntry entry, int pageNum) {
		itemStack = ItemStackUtil.loadStackFromString(item);
		
		if(link_recipe)
			entry.addRelevantStack(itemStack, pageNum);
	}

	@Override
	public void onDisplayed(GuiLexiconEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		
		textRender = new LexiconTextRenderer(parent, text, 0, 40);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		int w = 66;
		int h = 26;
		
		mc.renderEngine.bindTexture(GuiLexicon.CRAFTING_TEXTURE);
		GlStateManager.enableBlend();
		parent.drawModalRectWithCustomSizedTexture(GuiLexicon.PAGE_WIDTH / 2 - w / 2, 10, 0, 128 - h, w, h, 128, 128);
		parent.drawCenteredStringNoShadow(itemStack.getDisplayName(), GuiLexicon.PAGE_WIDTH / 2, 0, 0x333333);
		renderItem(GuiLexicon.PAGE_WIDTH / 2 - 8, 14, mouseX, mouseY, itemStack);
		
		
		textRender.render(mouseX, mouseY);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		textRender.click(mouseX, mouseY, mouseButton);
	}

}
