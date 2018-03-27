package vazkii.alquimia.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.arl.item.ItemMod;

public class ItemTestRod extends ItemMod implements IAlquimiaItem {

	public ItemTestRod() {
		super("test_rod");
		setMaxStackSize(1);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(worldIn instanceof WorldServer) {
			((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 100, 0.4, 0, 0.4, 0.01F);
			((WorldServer) worldIn).spawnParticle(EnumParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 20, 0.4, 0, 0.4, 0.05F);
		}
		
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

}
