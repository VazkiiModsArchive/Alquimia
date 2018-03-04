package vazkii.alquimia.common.handler;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.alquimia.common.crafting.CrucibleRecipe;
import vazkii.alquimia.common.crafting.CrucibleRecipes;
import vazkii.alquimia.common.handler.ItemTickHandler.EntityItemTickEvent;
import vazkii.alquimia.common.multiblock.ModMultiblocks;

public class CrucibleHandler {

	private static final String TAG_BURNING_TIME = "alquimia:burning_time";
	
	@SubscribeEvent
	public static void tickLivingEntity(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if(ModMultiblocks.crucible.validate(entity.world, entity.getPosition())) 
			entity.setFire(3);
	}
	
	@SubscribeEvent
	public static void tickItemEntity(EntityItemTickEvent event) {
		EntityItem entity = event.getEntityItem();
		if(!ModMultiblocks.crucible.validate(entity.world, entity.getPosition()))
			return;

		int time = entity.getEntityData().getInteger(TAG_BURNING_TIME);
		if(!entity.getEntityData().hasKey(TAG_BURNING_TIME))
			time = -160;
		
		BlockPos pos = entity.getPosition();
		IBlockState state = entity.world.getBlockState(pos);
		int level = (int) state.getProperties().get(BlockCauldron.LEVEL);
		if(level > 0 && entity.world.getTotalWorldTime() % 10 == 0)
			entity.world.setBlockState(pos, state.withProperty(BlockCauldron.LEVEL, level - 1));
		
		ItemStack stack = entity.getItem().copy();
		boolean matched = false;
		for(CrucibleRecipe recipe : CrucibleRecipes.CRUCIBLE_RECIPES) {
			matched = recipe.tick(entity, time);
			if(matched) {
				ItemStack stack2 = entity.getItem();
				if(!ItemStack.areItemStacksEqual(stack, stack2))
					time = 0;

				break;
			}
		}

		entity.getEntityData().setInteger(TAG_BURNING_TIME, time + 1);
		if(!matched && time > 200) {
			stack.shrink(1);
			entity.setItem(stack);
		}
	}
	
}
