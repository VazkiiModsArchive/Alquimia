package vazkii.alquimia.client.lexicon.page;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;
import vazkii.alquimia.client.lexicon.page.abstr.PageWithText;
import vazkii.alquimia.common.util.ItemStackUtil;

public class PageSpotlight extends PageWithText {

	String item, title;
	boolean link_recipe;

	transient ItemStack itemStack;
	
	@Override
	public void build(LexiconEntry entry, int pageNum) {
		itemStack = ItemStackUtil.loadStackFromString(item);
		
		if(link_recipe)
			entry.addRelevantStack(itemStack, pageNum);
	}

	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		int w = 66;
		int h = 26;
		
		mc.renderEngine.bindTexture(GuiLexicon.CRAFTING_TEXTURE);
		GlStateManager.enableBlend();
		parent.drawModalRectWithCustomSizedTexture(GuiLexicon.PAGE_WIDTH / 2 - w / 2, 10, 0, 128 - h, w, h, 128, 128);
		
		parent.drawCenteredStringNoShadow(title != null && !title.isEmpty() ? title : itemStack.getDisplayName(), GuiLexicon.PAGE_WIDTH / 2, 0, 0x333333);
		renderItem(GuiLexicon.PAGE_WIDTH / 2 - 8, 15, mouseX, mouseY, itemStack);
		
		super.render(mouseX, mouseY, pticks);
	}

	@Override
	public int getTextHeight() {
		return 40;
	}
	

}
