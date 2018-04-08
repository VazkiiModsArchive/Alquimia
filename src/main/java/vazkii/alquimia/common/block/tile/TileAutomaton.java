package vazkii.alquimia.common.block.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Rotation;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.block.interf.IAutomatonHead;
import vazkii.alquimia.common.item.interf.IAutomatonHeadItem;
import vazkii.alquimia.common.item.interf.IAutomatonInstruction;
import vazkii.arl.block.tile.TileMod;
import vazkii.arl.block.tile.TileSimpleInventory;

public class TileAutomaton extends TileSimpleInventory implements IAutomaton, ITickable {

	// TODO increase to 12
	public static final int INSTRUCTION_SLOTS = 6;
	public static final int INSTRUCTION_TIME = 10;
	
	protected IAutomatonHead head = null;
	protected EnumFacing facing = EnumFacing.NORTH;
	protected EnumFacing prevFacing = EnumFacing.NORTH;
	protected boolean up = false;
	protected boolean prevUp = false;
	protected Rotation rotation = Rotation.NONE;
	protected boolean executing = false;
	protected int clock = 0;
	protected int selection = 1;
	
	@Override
	public void update() {
//		ItemStack stack = getStackInSlot(0);
//		if(stack.isEmpty()) {
//			if(head != null)
//				head.onRemoved(this);
//			head = null;
//		} else if(head == null && stack.getItem() instanceof IAutomatonHeadItem) {
//			head = ((IAutomatonHeadItem) stack.getItem()).provideHead(stack);
//			head.onAttached(this);
//		}
//		
//		if(!isEnabled())
//			clock = INSTRUCTION_TIME;
//		else {
//			if(clock == INSTRUCTION_TIME)
//				executeCurrentInstruction();
//			else clock++;
//		}
	}
	
	protected void startExecuting() {
		executing = true;
		clock = 0;
	}
	
	protected void executeCurrentInstruction() {
		nextInstruction();

		boolean executed = false;
		while(selection < getSizeInventory()) {
			ItemStack stack = getStackInSlot(selection);
			if(stack.getItem() instanceof IAutomatonInstruction) {
				
			} else selection++;
		}
		startExecuting();
	}
	
	protected void nextInstruction() {
		selection++;
		if(selection >= getSizeInventory())
			selection = 1;
	}
	
	@Override
	public IAutomatonHead getHead() {
		return head;
	}

	@Override
	public EnumFacing getCurrentFacing() {
		return facing;
	}

	@Override
	public EnumFacing getPreviousFacing() {
		return facing;
	}
	
	@Override
	public Rotation getCurrentRotation() {
		return rotation;
	}

	@Override
	public void rotate(Rotation rotation) {
		if(!isExecuting()) {
			this.rotation = rotation;
			prevFacing = facing;
			facing = rotation.rotate(facing);
		}
	}

	@Override
	public boolean isUp() {
		return up;
	}
	
	@Override
	public boolean wasUp() {
		return prevUp;
	}

	@Override
	public void setUp(boolean up) {
		if(!isExecuting()) {
			prevUp = up;
			this.up = up;
		}
	}

	@Override
	public boolean isEnabled() {
		return true; // TODO redstone
	}

	@Override
	public boolean isExecuting() {
		return executing;
	}

	@Override
	public int getInstructionTime() {
		return clock;
	}
	
	@Override
	public int getSizeInventory() {
		return INSTRUCTION_SLOTS + 1;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
	@Override
	public boolean isAutomationEnabled() {
		return false;
	}

}
