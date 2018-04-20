package vazkii.alquimia.common.item;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.alquimia.common.block.interf.IAutomatonHead;
import vazkii.alquimia.common.item.interf.IAutomatonHeadItem;
import vazkii.arl.item.ItemMod;

public class ItemAutomatonHead extends ItemAlquimia implements IAutomatonHeadItem {

	private final Supplier<IAutomatonHead> headSupplier;
	
	public ItemAutomatonHead(String name, Supplier<IAutomatonHead> headSupplier) {
		super(name);
		this.headSupplier = headSupplier;
		setMaxStackSize(1);
	}

	@Override
	public IAutomatonHead provideHead(ItemStack stack) {
		return headSupplier.get();
	}

}
