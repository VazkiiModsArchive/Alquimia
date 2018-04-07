package vazkii.alquimia.client.lexicon.page;

import java.util.function.Function;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.base.PersistentData;
import vazkii.alquimia.client.base.PersistentData.DataHolder.Bookmark;
import vazkii.alquimia.client.handler.MultiblockVisualizationHandler;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconEye;
import vazkii.alquimia.client.lexicon.page.abstr.PageWithText;
import vazkii.alquimia.common.ritual.ModRituals;
import vazkii.alquimia.common.ritual.Ritual;

public class PageRitual extends PageWithText {

	String ritual;
	String title;
	
	transient Ritual ritualObj;
	transient GuiButton visualizeButton;
	
	@Override
	public void build(LexiconEntry entry, int pageNum) {
		super.build(entry, pageNum);
		
		ritualObj = ModRituals.rituals.get(new ResourceLocation(ritual));
	}
	
	@Override
	public void onDisplayed(GuiLexiconEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);

		adddButton(visualizeButton = new GuiButtonLexiconEye(parent, GuiLexicon.PAGE_WIDTH / 2 - 5, 75));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		super.render(mouseX, mouseY, pticks);
		
		parent.drawSeparator(0, 12);
		parent.drawCenteredStringNoShadow(title, GuiLexicon.PAGE_WIDTH / 2, 0, 0x333333);
		
		mc.renderEngine.bindTexture(GuiLexicon.RITUAL_CIRCLE_TEXTURE);
		int w = 66;
		int h = w;
		int x = GuiLexicon.PAGE_WIDTH / 2 - w / 2;
		int y = 35;
		parent.drawModalRectWithCustomSizedTexture(x, y, 0, 0, w, h, 128, 128);
		
		if(ritualObj != null) {
			int ingrs = ritualObj.ingredients.size();
			int cx = x + w / 2;
			int cy = y + h / 2;
			int r = w / 2 + 9;
			double anglePer = Math.PI * 2 / ingrs;
			double startAngle = -Math.PI / 2;
			
			for(int i = 0; i < ingrs; i++) {
				int rx = cx + (int) (Math.cos(i * anglePer + startAngle) * r) - 8;
				int ry = cy + (int) (Math.sin(i * anglePer + startAngle) * r) - 8;
				renderIngredient(rx, ry, mouseX, mouseY, ritualObj.ingredients.get(i));
			}
			
			String name = I18n.translateToLocal(ritualObj.type.name);
			String[] tokens = name.split("\\|");
			for(int i = 0; i < tokens.length; i++)
				parent.drawCenteredStringNoShadow(tokens[i].trim(), cx, cy - 14 + i * 10, 0x333333);
		}
	}
	
	@Override
	protected void onButtonClicked(GuiButton button) {
		if(button == visualizeButton) {
			String entryKey = parent.getEntry().getResource().toString();
			Bookmark bookmark = new Bookmark(entryKey, pageNum / 2);
			String name = I18n.translateToLocal(ritualObj.type.name).replaceAll("\\|", " ");

			Function<BlockPos, BlockPos> centerApplier = ritualObj.type.centerApplier.andThen(BlockPos::up);
			MultiblockVisualizationHandler.setMultiblock(ritualObj.type.mutliblock[0], name, bookmark, true, centerApplier);
			parent.addBookmarkButtons();
			
			if(!PersistentData.data.clickedVisualize) {
				PersistentData.data.clickedVisualize = true;
				PersistentData.save();
			}
				
		}
	}
	
	@Override
	public int getTextHeight() {
		return 122;
	}

}
