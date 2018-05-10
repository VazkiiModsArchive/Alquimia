package vazkii.alquimia.common.handler.reagent;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

public final class ReagentStack implements Comparable<ReagentStack> {

	private static final String TAG_TRUE_COUNT = "trueCount";
	public static final ReagentStack EMPTY_STACK = new ReagentStack(ItemStack.EMPTY, 0);
	
	public final ItemStack stack;
	public final int trueCount;

	public ReagentStack(ItemStack stack) {
		this(stack, stack.getCount());
	}

	public ReagentStack(ItemStack stack, int trueCount) {
		this.stack = stack.copy();
		this.trueCount = trueCount;
		this.stack.setCount(1);
	}

	public void writeToNBT(NBTTagCompound cmp) {
		stack.writeToNBT(cmp);
		cmp.setInteger(TAG_TRUE_COUNT, trueCount);
	}

	public static ReagentStack readFromNBT(NBTTagCompound cmp) {
		ItemStack stack = new ItemStack(cmp);
		int trueCount = cmp.getInteger(TAG_TRUE_COUNT);
		return new ReagentStack(stack, trueCount);
	}

	public boolean stacksEqual(ReagentStack other) {
		return stacksEqual(other.stack);
	}
	
	public boolean stacksEqual(ItemStack other) {
		return ItemStack.areItemsEqual(stack, other);
	}

	public ReagentStack merge(ReagentStack other) {
		return new ReagentStack(stack, this.trueCount + other.trueCount);
	}

	public ActionResult<ReagentStack> remove(int count) {
		int newCount = trueCount - count;
		if(newCount < 0)
			return ActionResult.newResult(EnumActionResult.FAIL, this);
		
		if(newCount == 0)
			return ActionResult.newResult(EnumActionResult.SUCCESS, EMPTY_STACK);

		return ActionResult.newResult(EnumActionResult.SUCCESS, new ReagentStack(stack, newCount));
	}
	
	public ReagentStack copy() {
		return new ReagentStack(stack.copy(), trueCount);
	}
	
	public boolean isEmpty() {
		return trueCount <= 0 || stack.isEmpty();
	}

	@Override
	public int compareTo(ReagentStack o) {
		int diff = o.trueCount - trueCount;
		if(diff != 0)
			return diff;
		
		return o.stack.getDisplayName().compareTo(stack.getDisplayName());
	}

}

