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
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.util.AutomatonUtil;

public class HeadBlade extends BasicHead {

	private static final int DAMAGE = 5;
	
	@Override
	public boolean onRotateStart(IAutomaton automaton) {
		return !automaton.isUp() || !AutomatonUtil.hasObstruction(automaton, true);
	}
	
	@Override
	public void onTicked(IAutomaton automaton) {
		if(automaton.isUp()) {
			int time = automaton.getInstructionTime();
			World world = automaton.getWorld();
			boolean rotating = automaton.getCurrentRotation() != Rotation.NONE;

			EnumFacing endFacing = automaton.getCurrentFacing();
			EnumFacing facing = !rotating ? endFacing : automaton.getCurrentRotation().rotate(endFacing.getOpposite());
			
			BlockPos current = automaton.getPos();
			BlockPos target = current.offset(facing);

			BlockPos end = current.offset(endFacing);
			BlockPos diag = end.offset(facing);
			
			int halfwayPoint = automaton.getSpeed() / 2;
			if(time == 0 || (rotating && time == halfwayPoint)) {
				BlockPos attackSurface = rotating ? diag : target;

				if(time == halfwayPoint)
					attackSurface = end;
				
				List<EntityLivingBase> entities = automaton.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(attackSurface)); 
				entities.forEach((e) -> e.attackEntityFrom(DamageSource.GENERIC, DAMAGE));
			}
		}
	}

	@Override
	public float getRenderTranslation(float translation) {
		return translation * -0.45F;
	}
	
	@Override
	public float getRenderExtraRotation() {
		return 45F;
	}
	
}
