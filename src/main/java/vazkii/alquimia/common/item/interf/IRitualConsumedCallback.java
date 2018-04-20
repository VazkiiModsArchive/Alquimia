package vazkii.alquimia.common.item.interf;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import vazkii.alquimia.common.ritual.Ritual;

public interface IRitualConsumedCallback {

	public ItemStack consumeForRitual(ItemStack stack, Ritual ritual, TileEntity tile);
	
}
