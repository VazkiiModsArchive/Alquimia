package vazkii.alquimia.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vazkii.alquimia.common.block.tile.TilePedestal;

public class RenderTilePedestal extends TileEntitySpecialRenderer<TilePedestal> {

	@Override
	public void render(TilePedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!te.isEmpty()) {
			ItemStack stack = te.getStackInSlot(0);
			float rot = te.rotation * 45F;
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.75F + 1F / 32F, z + 0.5);
			if(stack.getItem() instanceof ItemBlock) {
				GlStateManager.translate(0F, 0.22F, 0F);
				GlStateManager.rotate(rot, 0F, 1F, 0F);
			} else {
				GlStateManager.rotate(90F, 1F, 0F, 0F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(rot, 0F, 0F, 1F);
			}
			
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.popMatrix();
		}
	}
	
}
