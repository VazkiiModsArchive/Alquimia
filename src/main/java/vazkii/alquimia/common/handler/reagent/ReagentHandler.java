package vazkii.alquimia.common.handler.reagent;

import java.util.List;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ReagentHandler {

	private static final WeakHashMap<ItemStack, ReagentList> REAGENT_MAP = new WeakHashMap<>();
	
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		List<String> lines = event.getToolTip();
		if(stack.getItem() instanceof IReagentHolder) {
			ReagentList list = ReagentList.getFromStack(stack);
			for(ReagentStack rstack : list.stacks) {
				float displayedCount = (float) rstack.trueCount / ReagentList.DEFAULT_MULTIPLICATION_FACTOR;
				lines.add(rstack.stack.getDisplayName() + " (" + displayedCount + ")");
			}
		}
	}
	
	public static ReagentList getReagents(ItemStack stack) {
		ReagentList list = ReagentList.getFromStack(stack); // TODO caching
		return list;
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
