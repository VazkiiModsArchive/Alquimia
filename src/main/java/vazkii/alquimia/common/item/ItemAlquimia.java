package vazkii.alquimia.common.item;

import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.arl.item.ItemMod;

public class ItemAlquimia extends ItemMod implements IAlquimiaItem {

	public ItemAlquimia(String name) {
		super(name);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

}
