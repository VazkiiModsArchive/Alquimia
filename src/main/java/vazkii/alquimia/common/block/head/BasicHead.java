package vazkii.alquimia.common.block.head;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.block.interf.IAutomatonHead;

public abstract class BasicHead implements IAutomatonHead {

	@Override
	@SideOnly(Side.CLIENT)
	public void render(IAutomaton automaton, ItemStack stack, float translation, float partTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		RenderItem render = mc.getRenderItem(); 

		translation = getRenderTranslation(translation);
		GlStateManager.translate(translation , 0F, 0F);
		GlStateManager.rotate(getRenderExtraRotation(), 0F, 0F, 1F);
		render.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.translate(-translation , 0F, 0F);
	}
	
	@SideOnly(Side.CLIENT)
	public abstract float getRenderTranslation(float translation);
	
	@SideOnly(Side.CLIENT)
	public float getRenderExtraRotation() {
		return 0F;
	}
	
}
