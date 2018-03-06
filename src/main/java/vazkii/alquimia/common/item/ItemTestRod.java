package vazkii.alquimia.common.item;

import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import vazkii.alquimia.client.handler.MultiblockVisualizationHandler;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.alquimia.common.multiblock.ModMultiblocks;
import vazkii.alquimia.common.multiblock.Multiblock;
import vazkii.arl.item.ItemMod;

public class ItemTestRod extends ItemMod implements IAlquimiaItem {

	public ItemTestRod() {
		super("test_rod");
		setMaxStackSize(1);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote) try {
			Multiblock mb = new Multiblock(new String[][] {
						{ "   ", "   ", "   ", "   ", " D " },
						{ "   ", "   ", "   ", "   ", " X " },
						{ "XXX", "X0X", "XXX", " X ", " X " }},
							'X', Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y),
							'D', Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST))
					.offset(0, -1, 0);
			
			MultiblockVisualizationHandler.setMultiblock(mb, "Test Majig", null, false);
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
