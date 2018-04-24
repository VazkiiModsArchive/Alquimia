package vazkii.alquimia.common.block.head;

import net.minecraft.util.math.BlockPos;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.util.AutomatonUtil;

public class HeadDrill extends BasicHead {

	@Override
	public boolean onEngageStatusStart(IAutomaton automaton) {
		return AutomatonUtil.canIntereactWithTarget(automaton) || !automaton.isUp();
	}
	
	@Override
	public void onEngageStatusEnd(IAutomaton automaton) {
		if(AutomatonUtil.canIntereactWithTarget(automaton) && automaton.isUp()) {
			BlockPos target = AutomatonUtil.getTarget(automaton);
			if(!automaton.getWorld().isRemote)
				automaton.getWorld().destroyBlock(target, true);
			automaton.getWorld().sendBlockBreakProgress(0, target, -1);
		}
	}
	
	@Override
	public boolean onRotateStart(IAutomaton automaton) {
		return !automaton.isUp();
	}
	
	@Override
	public void onTicked(IAutomaton automaton) {
		if((AutomatonUtil.canIntereactWithTarget(automaton) && automaton.isExecuting() && automaton.isUp() && !automaton.wasUp()) || !automaton.isExecuting()) {
			float fract = (float) automaton.getInstructionTime() / automaton.getSpeed();
			if(!automaton.isExecuting())
				fract = -1;
			automaton.getWorld().sendBlockBreakProgress(0, AutomatonUtil.getTarget(automaton), (int) (fract * 10));
		}
	}
	
	@Override
	public void onRemoved(IAutomaton automaton) {
		automaton.getWorld().sendBlockBreakProgress(0, AutomatonUtil.getTarget(automaton), -1);
	}
	
	@Override
	public float getRenderTranslation(float translation) {
		return translation * -0.6F;
	}
	
}
