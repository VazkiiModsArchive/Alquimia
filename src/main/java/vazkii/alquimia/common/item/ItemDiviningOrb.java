package vazkii.alquimia.common.item;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.alquimia.common.handler.reagent.IReagentConsumer;
import vazkii.alquimia.common.handler.reagent.ReagentHandler;
import vazkii.alquimia.common.handler.reagent.ReagentList;

public class ItemDiviningOrb extends ItemAlquimia implements IReagentConsumer {

	private static final ReagentList REAGENTS = ReagentList.of(new ItemStack(Items.SPECTRAL_ARROW, 50));
	
	private static final Set<Block> ORES = new LinkedHashSet();
	
	public ItemDiviningOrb() {
		super("divining_orb");
		setMaxStackSize(1);
	}
	
	public void getOres() {
		String[] names = OreDictionary.getOreNames();
		for(String name : names)
			if(name.matches("^ore[A-Z].*")) {
				NonNullList<ItemStack> stacks = OreDictionary.getOres(name);
				for(ItemStack stack : stacks) 
					if(stack.getItem() instanceof ItemBlock)
						ORES.add(((ItemBlock) stack.getItem()).getBlock());
			}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(ReagentHandler.removeFromPlayer(playerIn, REAGENTS)) {
			List<BlockPos> positions = new LinkedList();
			BlockPos playerPos = playerIn.getPosition();
			int rangeH = 32;
			int rangeV = 6;
			for(int x = -rangeH; x < rangeH; x++)
				for(int z = -rangeH; z < rangeH; z++)
					for(int y = -rangeV; y < rangeV; y++) {
						BlockPos pos = playerPos.add(x, y, z);
						IBlockState state = worldIn.getBlockState(pos);
						Block block = state.getBlock();
						if(ORES.contains(block))
							positions.add(pos);
					}
			
			if(worldIn.isRemote)
				for(BlockPos pos : positions)
					;
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public ReagentList getReagentsToConsume(ItemStack stack, EntityPlayer player) {
		return REAGENTS;
	}

}
