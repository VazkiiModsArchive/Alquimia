package vazkii.alquimia.common.base;

import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.interf.IModBlock;

public interface IAlquimiaBlock extends IModBlock {

	@Override
	default String getModNamespace() {
		return LibMisc.MOD_ID;
	}
	
}