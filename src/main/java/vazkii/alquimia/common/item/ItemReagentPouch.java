package vazkii.alquimia.common.item;

import java.util.function.Consumer;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.crafting.recipe.RecipeRemoveReagents;
import vazkii.alquimia.common.handler.MultiblockTrackingHandler;
import vazkii.alquimia.common.handler.MultiblockTrackingHandler.MultiblockSettings;
import vazkii.alquimia.common.handler.reagent.IReagentConsumer;
import vazkii.alquimia.common.handler.reagent.IReagentHolder;
import vazkii.alquimia.common.handler.reagent.ReagentHandler;
import vazkii.alquimia.common.handler.reagent.ReagentList;
import vazkii.alquimia.common.ritual.RitualType;
import vazkii.arl.util.ItemNBTHelper;

public class ItemReagentPouch extends ItemAlquimia implements IReagentHolder, IReagentConsumer {

	private static final int TYPE_LIMIT = 64;
	private static final int STACK_LIMIT = 64 * 100 * ReagentList.DEFAULT_MULTIPLICATION_FACTOR;

	private static final String TAG_ITEM_LIST = "itemList";
	private static final String TAG_IS_CREATIVE = "isCreative";

	public ItemReagentPouch() {
		super("reagent_pouch");
		setMaxStackSize(1);
		addBooleanPropertyOverride("has_items", ItemReagentPouch::hasItems);

		new RecipeRemoveReagents();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		MultiblockSettings mbs = MultiblockTrackingHandler.get(playerIn);
		if(mbs != null && RitualType.isRitualCircle(mbs.mb)) {
			BlockPos pos = mbs.pos.add(mbs.mb.offX, 0, mbs.mb.offZ);
			BlockPos playerPos = playerIn.getPosition();

			boolean[] can = { false }; // use array to get arround "effectively final" restrictions

			mbs.mb.forEach(worldIn, pos, mbs.rot, (char) 0, (p) -> {
				if(!can[0] && p.distanceSq(playerPos) < 16)
					can[0] = true;
			});

			if(can[0] && ReagentHandler.removeFromPlayer(playerIn, getReagentsToConsume(stack, playerIn))) {
				mbs.mb.place(worldIn, pos, mbs.rot);
				playerIn.swingArm(handIn);
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		super.getSubItems(tab, subItems);

		if(isInCreativeTab(tab)) {
			ItemStack creative = new ItemStack(this);
			ItemNBTHelper.setBoolean(creative, TAG_IS_CREATIVE, true);
			subItems.add(creative);
		}
	}

	public static boolean hasItems(ItemStack stack) {
		if(ItemNBTHelper.getBoolean(stack, TAG_IS_CREATIVE, false))
			return true;

		ReagentList list = ReagentHandler.getReagents(stack);
		return !list.stacks.isEmpty();
	}

	public static ItemStack with(ItemStack... reagents) {
		ItemStack stack = new ItemStack(ModItems.reagent_pouch);
		ReagentList list = ReagentList.of(reagents);
		list.commit(stack);

		return stack;
	}

	@Override
	public int getReagentTypeLimit(ItemStack stack) {
		return TYPE_LIMIT;
	}

	@Override
	public int getReagentStackLimit(ItemStack stack) {
		return STACK_LIMIT;
	}

	@Override
	public boolean isCreativeReagentHolder(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_IS_CREATIVE, false);
	}

	@Override
	public ReagentList getReagentsToConsume(ItemStack stack, EntityPlayer player) {
		MultiblockSettings mbs = Alquimia.proxy.getVisualizingMultiblock(player);

		if(mbs != null && RitualType.isRitualCircle(mbs.mb)) {
			int[] ash = { 0 }; // use array to get arround "effectively final" restrictions

			Consumer<BlockPos> run = (p) -> {
				if(ModBlocks.ash.canPlaceBlockAt(player.world, p) && player.world.getBlockState(p).getBlock().isReplaceable(player.world, p)) 
					ash[0]++;
			};

			BlockPos pos = mbs.pos.add(mbs.mb.offX, 0, mbs.mb.offZ);
			mbs.mb.forEach(player.world, pos, mbs.rot, '0', run);
			mbs.mb.forEach(player.world, pos, mbs.rot, 'A', run);

			if(ash[0] > 0)
				return ReagentList.of(new ItemStack(ModBlocks.ash, ash[0] * ReagentList.DEFAULT_MULTIPLICATION_FACTOR));
		}

		return ReagentList.EMPTY;
	}

}
