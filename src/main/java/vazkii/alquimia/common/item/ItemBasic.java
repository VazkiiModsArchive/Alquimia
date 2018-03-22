package vazkii.alquimia.common.item;

import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.arl.item.ItemMod;

public class ItemBasic extends ItemMod implements IAlquimiaItem {

	public ItemBasic(String name) {
		super(name);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

}
