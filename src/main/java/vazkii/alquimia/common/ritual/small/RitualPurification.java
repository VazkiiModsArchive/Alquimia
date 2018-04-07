package vazkii.alquimia.common.ritual.small;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.alquimia.common.base.AlquimiaSounds;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.ritual.Ritual;
import vazkii.alquimia.common.ritual.RitualType;

public class RitualPurification extends Ritual {

	private static Collection<Object> INGREDIENTS = ImmutableSet.of();
	
	public RitualPurification() {
		super("purification", RitualType.SMALL, INGREDIENTS);
	}

	@Override
	public boolean run(World world, BlockPos pos, NBTTagCompound cmp) {
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos).grow(2), 
				(item) -> item.getItem().getItem() == Item.getItemFromBlock(ModBlocks.ash));

		boolean did = false;
		for(EntityItem item : items) {
			int newCount = item.getItem().getCount() / 16;
			if(newCount == 0)
				continue;
			
			item.setItem(new ItemStack(ModItems.alchemical_ash, newCount));
			did = true;
			
			if(world instanceof WorldServer) {
				((WorldServer) world).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, item.posX, item.posY, item.posZ, 80, 0.6, 0.6, 0.6, 0.05);
				((WorldServer) world).spawnParticle(EnumParticleTypes.CRIT_MAGIC, item.posX, item.posY, item.posZ, 80, 2, 2, 2, 0.1);
				((WorldServer) world).spawnParticle(EnumParticleTypes.END_ROD, item.posX, item.posY, item.posZ, 80, 0.5, 1, 0.5, 0.1);
			}
		}

		if(did)
			world.playSound(null, pos, AlquimiaSounds.ash_infuse, SoundCategory.BLOCKS, 1.0F, 1.0F);
		
		return true;
	}

}

