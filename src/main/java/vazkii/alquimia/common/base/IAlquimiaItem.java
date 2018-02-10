package vazkii.alquimia.common.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.interf.IVariantHolder;

public interface IAlquimiaItem extends IVariantHolder {
	
	@Override
	default String getModNamespace() {
		return LibMisc.MOD_ID;
	}
	
	public default ActionResult<ItemStack> succeed(EntityPlayer player, EnumHand hand) {
		return result(EnumActionResult.SUCCESS, player, hand);
	}

	public default ActionResult<ItemStack> pass(EntityPlayer player, EnumHand hand) {
		return result(EnumActionResult.PASS, player, hand);
	}
	
	public default ActionResult<ItemStack> fail(EntityPlayer player, EnumHand hand) {
		return result(EnumActionResult.FAIL, player, hand);
	}
	
	static ActionResult<ItemStack> result(EnumActionResult res, EntityPlayer player, EnumHand hand) {
		return new ActionResult<ItemStack>(res, player.getHeldItem(hand));
	}

}