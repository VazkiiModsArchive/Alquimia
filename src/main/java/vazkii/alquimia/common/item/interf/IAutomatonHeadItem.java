package vazkii.alquimia.common.item.interf;

import net.minecraft.item.ItemStack;
import vazkii.alquimia.common.block.interf.IAutomatonHead;

public interface IAutomatonHeadItem {

	public IAutomatonHead provideHead(ItemStack stack);
	
}
