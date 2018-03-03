package vazkii.alquimia.common.item;

import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.alquimia.common.base.multiblock.Multiblock;
import vazkii.arl.item.ItemMod;

public class ItemTestRod extends ItemMod implements IAlquimiaItem {

	public ItemTestRod() {
		super("test_rod");
		setMaxStackSize(1);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		try {

			Multiblock mb = new Multiblock(new String[][] {
				{ "   ", " 0 ", "   " },
				{ "SSS", "SFS", "SSS" }},
					'0', Blocks.CAULDRON,
					'F', Blocks.FIRE.getDefaultState().withProperty(BlockFire.UPPER, true),
					'S', Blocks.STONEBRICK).offset(0, -1, 0);

			mb.place(worldIn, pos);

		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

}
