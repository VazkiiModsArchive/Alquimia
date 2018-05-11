package vazkii.alquimia.common.handler.reagent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IReagentConsumer {

	public ReagentList getReagentsToConsume(ItemStack stack, EntityPlayer player);
	
}
