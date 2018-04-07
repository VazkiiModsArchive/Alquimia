package vazkii.alquimia.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.entity.EntityRitualLogic;
import vazkii.alquimia.common.ritual.Ritual;

public class RenderRitual extends Render<EntityRitualLogic> {

	public RenderRitual(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityRitualLogic entity, double x, double y, double z, float entityYaw, float partialTicks) {
		Ritual ritual = entity.getRitual();
		if(ritual != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			ritual.render(entity.world, entity.getPosition(), entity.getTime(), partialTicks);
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityRitualLogic entity) {
		return null;
	}

}
