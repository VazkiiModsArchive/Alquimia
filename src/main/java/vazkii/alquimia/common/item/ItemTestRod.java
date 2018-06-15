package vazkii.alquimia.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.handler.reagent.IReagentConsumer;
import vazkii.alquimia.common.handler.reagent.ReagentHandler;
import vazkii.alquimia.common.handler.reagent.ReagentList;

public class ItemTestRod extends ItemAlquimia implements IReagentConsumer {

	public ItemTestRod() {
		super("test_rod");
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			boolean did = ReagentHandler.removeFromPlayer(player, getReagentsToConsume(stack, player));
			
		}
		
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public ReagentList getReagentsToConsume(ItemStack stack, EntityPlayer player) {
		return ReagentList.of(new ItemStack(Items.BLAZE_POWDER, 100), new ItemStack(Items.ROTTEN_FLESH, 120), new ItemStack(Items.DIAMOND, 10));
	}

}
