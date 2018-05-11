package vazkii.alquimia.common.handler.reagent;

import java.util.List;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ReagentHandler {

	private static final WeakHashMap<ItemStack, ReagentList> CACHE = new WeakHashMap<>();

	public static ReagentList getReagents(ItemStack stack) {
		if(CACHE.containsKey(stack))
			return CACHE.get(stack);
		
		ReagentList list = ReagentList.getFromStack(stack);
		CACHE.put(stack, list);
		return list;
	}
	
	public static int getCount(EntityPlayer player, ItemStack target) {
		InventoryPlayer inv = player.inventory;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stackAt = inv.getStackInSlot(i);
			if(stackAt.getItem() instanceof IReagentHolder) {
				ReagentList list = getReagents(stackAt);
				int count = list.getCount(target);
				if(count > 0)
					return count;
			}
		}
		
		return 0;
	}
	
	public static boolean removeFromPlayer(EntityPlayer player, ItemStack... targets) {
		return removeFromPlayer(player, ReagentList.of(targets));
	}
	
	public static boolean removeFromPlayer(EntityPlayer player, ReagentList targets) {
		InventoryPlayer inv = player.inventory;
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stackAt = inv.getStackInSlot(i);
			if(stackAt.getItem() instanceof IReagentHolder) {
				ReagentList list = getReagents(stackAt);
				if(list.removeAll(targets, false)) {
					list.removeAll(targets, true);
					list.commitIfDirty(stackAt);
					return true;
				}
			}
		}
		
		return false;
	}

}
