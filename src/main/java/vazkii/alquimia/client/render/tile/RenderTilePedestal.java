package vazkii.alquimia.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import vazkii.alquimia.common.block.tile.TilePedestal;
import vazkii.arl.util.ClientTicker;

public class RenderTilePedestal extends TileEntitySpecialRenderer<TilePedestal> {

	@Override
	public void render(TilePedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!te.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y  + 0.5 + 1F / 32F, z + 0.5);
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			float scale = 0.75F;
			GlStateManager.scale(scale, scale, scale);
			
			float rot = te.rotation * 45F;
			GlStateManager.rotate(rot, 0F, 0F, 1F);
			
			ItemStack stack = te.getStackInSlot(0);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.popMatrix();
		}
	}
	
}
