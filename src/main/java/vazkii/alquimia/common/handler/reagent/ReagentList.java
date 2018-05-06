package vazkii.alquimia.common.handler.reagent;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import vazkii.arl.util.ItemNBTHelper;

public final class ReagentList {

	public static final ReagentList EMPTY = new ReagentList();
	
	private static final String TAG_REAGENT_LIST = "reagentList";
	private static final String TAG_REAGENT_COUNT = "reagentCount";
	private static final String TAG_REAGENT = "reagent";

	public List<ReagentStack> stacks = new ArrayList();
	public boolean dirty = false;
	
	public static ReagentList of(ItemStack... targets) {
		ReagentList list = new ReagentList();
		for(int i = 0; i < targets.length; i++) {
			ItemStack stack = (ItemStack) targets[i];
			list.stacks.add(new ReagentStack(stack, stack.getCount()));
		}
		
		return list;
	}

	public static ReagentList getFromStack(ItemStack stack) {
		if(stack.hasTagCompound()) {
			NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_REAGENT_LIST, true);
			if(cmp != null) {
				ReagentList list = new ReagentList();
				int count = cmp.getInteger(TAG_REAGENT_COUNT);
				for(int i = 0; i < count; i++) {
					NBTTagCompound reagentCmp = cmp.getCompoundTag(TAG_REAGENT + i);
					list.stacks.add(ReagentStack.readFromNBT(reagentCmp));
				}
				return list;
			}
		}

		return EMPTY;
	}
	
	public void commit(ItemStack stack) {
		NBTTagCompound cmp = new NBTTagCompound();
		cmp.setInteger(TAG_REAGENT_COUNT, stacks.size());
		for(int i = 0; i < stacks.size(); i++) {
			NBTTagCompound stackCmp = new NBTTagCompound();
			stacks.get(i).writeToNBT(stackCmp);
			cmp.setTag(TAG_REAGENT + i, stackCmp);
		}
		ItemNBTHelper.setCompound(stack, TAG_REAGENT_LIST, cmp);
	}
	
	public void commitIfDirty(ItemStack stack) {
		if(dirty) {
			commit(stack);
			dirty = false;
		}
	}

	public void merge(ReagentList other) {
		for(ReagentStack ostack : other.stacks) insert: {
			for(int i = 0; i < stacks.size(); i++) {
				ReagentStack stack = stacks.get(i);
				if(stack.stacksEqual(ostack)) {
					stacks.set(i, stack.merge(ostack));
					break insert;
				}

				stacks.add(ostack);
			}
		}
	}
	
	public boolean removeAll(ReagentList targets, boolean doit) {
		for(ReagentStack stack : targets.stacks) {
			if(!remove(stack.stack, stack.trueCount, doit))
				return false;
		}
		
		return true;
	}
	
	public boolean remove(ItemStack stack, int count, boolean doit) {
		boolean did = false;
		
		for(int i = 0; i < stacks.size(); i++) {
			ReagentStack rstack = stacks.get(i);
			if(rstack.stacksEqual(stack)) {
				ActionResult<ReagentStack> result = rstack.remove(count);
				if(result.getType() == EnumActionResult.SUCCESS) {
					did = true;
					if(doit) {
						dirty = true;
						stacks.set(i, result.getResult());
					}
				}
					
				break;
			}
		}
				
		if(doit && did)
			stacks.removeIf(ReagentStack::isEmpty);
		
		return did;
	}
	
}
