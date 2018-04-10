package vazkii.alquimia.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import vazkii.alquimia.common.block.interf.IAutomatonHead;
import vazkii.alquimia.common.block.tile.TileAutomaton;

public class RenderTileAutomaton extends TileEntitySpecialRenderer<TileAutomaton> {

	private final int[] FACING_ROTATIONS = { 0, 180, 90, 270 };
	
	@Override
	public void render(TileAutomaton te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		int[] FACING_ROTATIONS = { 0, 180, 270, 90 };

		ItemStack stack = te.getStackInSlot(0);
		if(!stack.isEmpty()) {
			float compensate = 90F;
			EnumFacing currFacing = te.getCurrentFacing();
			EnumFacing prevFacing = te.getPreviousFacing();
			float rot = FACING_ROTATIONS[(currFacing.ordinal() - 2)] + compensate;
			
			float fract = ((float) te.getInstructionTime() + partialTicks) / TileAutomaton.INSTRUCTION_TIME;
			fract = -fract * (fract - 2);
			
			if(te.isExecuting() && currFacing != prevFacing) {
				float prevRot = FACING_ROTATIONS[(prevFacing.ordinal() - 2)] + compensate;
				float diff = rot - prevRot;
				if(diff < -90F)
					diff = 360F + diff;
				if(diff > 90F)
					diff = 180F - diff;
				
				rot = prevRot + diff * fract;
			}
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.5F + 1F / 64F, z + 0.5);
			
			RenderItem render = Minecraft.getMinecraft().getRenderItem();
			IBakedModel model = render.getItemModelWithOverrides(stack, getWorld(), null);
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			GlStateManager.rotate(rot, 0F, 0F, 1F);
			
			boolean isUp = te.isUp();
			boolean wasUp = te.wasUp();
			float extend = -0.25F;
			float translate = (isUp ? extend : 0F);
			 
			if(te.isExecuting() && isUp != wasUp) {
				float prevTranslate = (wasUp ? extend : 0F);
				float diff = translate - prevTranslate;
				
				translate = prevTranslate + diff * fract;
			}
			
			IAutomatonHead head = te.getHead();
			boolean renderItem = true;
			if(head != null)
				renderItem = head.render(te, translate, partialTicks);
			
			if(renderItem) {
				GlStateManager.translate(translate, 0F, 0F);
				render.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
			}
			
			GlStateManager.popMatrix();
		}
	}
	
}
