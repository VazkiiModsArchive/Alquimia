package vazkii.alquimia.common.handler.reagent;

import net.minecraft.item.ItemStack;

public interface IReagentHolder {

	public int getReagentTypeLimit(ItemStack stack);
	
	public int getReagentStackLimit(ItemStack stack);

	public default boolean isCreativeReagentHolder(ItemStack stack) {
		return false;
	}
	
}
