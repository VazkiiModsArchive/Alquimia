package vazkii.alquimia.common.ritual.medium;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.init.Items;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.alquimia.common.base.AlquimiaSounds;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.ritual.Ritual;
import vazkii.alquimia.common.ritual.RitualType;

public class RitualStorms extends Ritual {

	private static Collection<Object> INGREDIENTS = Arrays.asList(new Object[] {ModItems.cinnabar, ModItems.cinnabar, Items.GUNPOWDER, Items.WATER_BUCKET, "ingotIron"});
	
	public RitualStorms() {
		super("storms", RitualType.MEDIUM, INGREDIENTS);
	}
	
	@Override
	public boolean canRun(World world, BlockPos center) {
		return !world.isRaining();
	}

	@Override
	public boolean run(World world, BlockPos pos) {
		world.playSound(null, pos, AlquimiaSounds.storms, SoundCategory.BLOCKS, 1F, 1F);
		return false;
	}
	
	@Override
	public boolean tick(World world, BlockPos pos, int time) {
		if(time >= 90) {
			world.getWorldInfo().setRaining(true);
			return true;
		}
		
		if(world instanceof WorldServer && time < 50 && time > 10) {
			int tries = 8;
			for(int i = 0; i < tries; i++) {
				float speed = 1F;
				float rtime = (time - 10) + (1F / tries) * i;
				float dist = (float) rtime / 10F;
				double x = pos.getX() + 0.5 + Math.sin((float) rtime * speed) * dist;
				double y = pos.getY() + 1.5 - rtime * 0.04F;
				double z = pos.getZ() + 0.5+ Math.cos((float) rtime * speed) * dist;
				((WorldServer) world).spawnParticle(EnumParticleTypes.DRIP_WATER, x, y, z, 5, 0.05, 0.05, 0.05, 0);
			}
		}
		
		
		return false;
	}
	
}
