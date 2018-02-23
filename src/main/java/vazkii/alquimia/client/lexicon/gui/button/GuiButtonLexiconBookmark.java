package vazkii.alquimia.client.lexicon.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.base.PersistentData.DataHolder.Bookmark;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class GuiButtonLexiconBookmark extends GuiButtonLexicon {

	public final Bookmark bookmark;
	
	public GuiButtonLexiconBookmark(GuiLexicon parent, int x, int y, Bookmark bookmark) {
		super(parent, x, y, 272, bookmark == null ? 170 : 160, 13, 10, getTooltip(bookmark));
		this.bookmark = bookmark;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.drawButton(mc, mouseX, mouseY, partialTicks);
		
		if(visible && bookmark != null && bookmark.getEntry() != null) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			int px = x * 2 + (hovered ? 6 : 2);
			int py = y * 2 + 2;
			mc.getRenderItem().renderItemIntoGUI(bookmark.getEntry().getIconItem(), px, py);
			
			GlStateManager.disableDepth();
			mc.fontRenderer.drawStringWithShadow(Integer.toString(bookmark.page + 1), px + 12, py + 10, 0xFFFFFF);
			GlStateManager.popMatrix();
		}
	}
	
	private static String[] getTooltip(Bookmark bookmark) {
		if(bookmark == null || bookmark.getEntry() == null)
			return new String[] { I18n.translateToLocal("alquimia.gui.lexicon.add_bookmark") };
		
		return new String[] {
				bookmark.getEntry().getName(),
				TextFormatting.GRAY + I18n.translateToLocal("alquimia.gui.lexicon.remove_bookmark")
		};
	}

}
