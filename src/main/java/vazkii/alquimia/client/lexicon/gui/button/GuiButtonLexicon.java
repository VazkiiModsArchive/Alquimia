package vazkii.alquimia.client.lexicon.gui.button;

import com.google.common.base.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class GuiButtonLexicon extends GuiButton {

	GuiLexicon parent;
	int u, v;
	Supplier<Boolean> displayCondition;
	
	public GuiButtonLexicon(GuiLexicon parent, int x, int y, int u, int v, int w, int h) {
		this(parent, x, y, u, v, w, h, ()->true);
	}
	
	public GuiButtonLexicon(GuiLexicon parent, int x, int y, int u, int v, int w, int h, Supplier<Boolean> displayCondition) {
		super(0, x, y, w, h, "");
		this.parent = parent;
		this.u = u;
		this.v = v;
		this.displayCondition = displayCondition;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1F, 1F, 1F);
		enabled = visible = displayCondition.get();
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		
		if(visible)
			GuiLexicon.drawFromTexture(x, y, u + (hovered ? width : 0), v, width, height);
	}

}
