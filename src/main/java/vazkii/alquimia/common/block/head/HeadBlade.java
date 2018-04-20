package vazkii.alquimia.common.block.head;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.common.block.BlockAutomaton;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.block.interf.IAutomatonHead;
import vazkii.alquimia.common.util.AutomatonUtil;

public class HeadBlade implements IAutomatonHead {

	private static final int DAMAGE = 5;
	
	@Override
	public boolean onRotateStart(IAutomaton automaton) {
		return !automaton.isUp() || !AutomatonUtil.hasObstruction(automaton, true);
	}
	
	@Override
	public void onTicked(IAutomaton automaton) {
		if(automaton.isUp() && automaton.getCurrentRotation() != Rotation.NONE) {
			int time = automaton.getInstructionTime();
			World world = automaton.getWorld();
			EnumFacing endFacing = automaton.getCurrentFacing();
			EnumFacing facing = automaton.getCurrentRotation().rotate(endFacing.getOpposite());

			BlockPos current = automaton.getPos();
			BlockPos target = current.offset(facing);

			BlockPos end = current.offset(endFacing);
			BlockPos diag = end.offset(facing);
			
			int halfwayPoint = automaton.getSpeed() / 2;
			if(time == 0 || time == halfwayPoint) {
				BlockPos attackSurface = diag;
				EnumFacing bladeFacing = facing;
				if(time == halfwayPoint) {
					attackSurface = end;
					bladeFacing = endFacing;
				}
				
				List<EntityLivingBase> entities = automaton.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(attackSurface)); 
				entities.forEach((e) -> e.attackEntityFrom(DamageSource.GENERIC, DAMAGE));
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void render(IAutomaton automaton, ItemStack stack, float translation, float partTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		RenderItem render = mc.getRenderItem(); 

		translation *= -0.45F;
		GlStateManager.translate(translation , 0F, 0F);
		GlStateManager.rotate(45F, 0F, 0F, 1F);
		render.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.translate(-translation , 0F, 0F);
	}
	
}
