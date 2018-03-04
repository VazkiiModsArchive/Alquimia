package vazkii.alquimia.common.crafting;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class CrucibleRecipe {

	public final Ingredient ingredient;
	public final ItemStack output;
	public final int time;
	
	public CrucibleRecipe(Ingredient ingredient, ItemStack output, int time) {
		this.ingredient = ingredient;
		this.output = output;
		this.time = time;
	}
	
	public boolean tick(EntityItem entity, int burnTime) {
		ItemStack entityStack = entity.getItem();
		boolean matches = ingredient.apply(entityStack);
		if(matches && !entityStack.isEmpty()) {
			BlockPos pos = entity.getPosition();

			EnumParticleTypes smoke = EnumParticleTypes.SMOKE_NORMAL;
			int particleCount = 20;
			if(burnTime > time) {
				entityStack.shrink(1);
				entity.setItem(entityStack);
				
				EntityItem newItem = new EntityItem(entity.world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, output.copy());
				
				double angle = Math.random() * Math.PI * 2;
				double speed = 0.07;
				newItem.motionX = Math.sin(angle) * speed;
				newItem.motionY = 0.4;
				newItem.motionZ = Math.cos(angle) * speed;
				
				entity.world.spawnEntity(newItem);
				
				smoke = EnumParticleTypes.SMOKE_LARGE;
				particleCount = 60;
				entity.world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 0.6F + (float) Math.random() * 0.4F);
			}
			
			if(time % 10 == 0)
				entity.world.playSound(null, pos.down(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5F, 0.6F + (float) Math.random() * 0.4F);
			
			if(Math.random() < 0.1) {
				smoke = EnumParticleTypes.SMOKE_LARGE;
				if(Math.random() < 0.3) {
					smoke = EnumParticleTypes.LAVA;
					particleCount = 2;
				}
			}
			((WorldServer) entity.world).spawnParticle(smoke, pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5, particleCount, 0.1, 0.1, 0.1, 0.02);
		}
		
		return matches;
	}
	
}
