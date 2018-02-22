package vazkii.alquimia.client.base;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import vazkii.alquimia.common.base.AlquimiaConfig;
import vazkii.alquimia.common.lib.LibMisc;

public class GuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {
		// NO-OP
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GuiAlquimiaConfig(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}
	
	public class GuiAlquimiaConfig extends GuiConfig {

		public GuiAlquimiaConfig(GuiScreen parentScreen) {
			super(parentScreen, new ConfigElement(AlquimiaConfig.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), LibMisc.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(AlquimiaConfig.config.toString()));
		}

	}

}
