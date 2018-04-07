package vazkii.alquimia.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BuiltInModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import vazkii.alquimia.common.block.tile.TilePedestal;

public class RenderTilePedestal extends TileEntitySpecialRenderer<TilePedestal> {

	@Override
	public void render(TilePedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!te.isEmpty()) {
			ItemStack stack = te.getStackInSlot(0);
			float rot = te.rotation * 45F;

			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.75F + 1F / 64F, z + 0.5);
			
			RenderItem render = Minecraft.getMinecraft().getRenderItem();
			IBakedModel model = render.getItemModelWithOverrides(stack, getWorld(), null);
			if(!model.getClass().getName().contains("VanillaModelWrapper")) {
				GlStateManager.rotate(90F, 1F, 0F, 0F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(rot, 0F, 0F, 1F);
			} else {
				GlStateManager.translate(0F, 0.11F, 0F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(rot, 0F, 1F, 0F);
			}

			render.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.popMatrix();
		}
	}

}
