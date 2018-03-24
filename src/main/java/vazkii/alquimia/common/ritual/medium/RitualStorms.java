package vazkii.alquimia.common.ritual.medium;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.ritual.Ritual;
import vazkii.alquimia.common.ritual.RitualType;

public class RitualStorms extends Ritual {

	private static Collection<Object> INGREDIENTS = Arrays.asList(new Object[] {ModItems.cinnabar, ModItems.cinnabar, Items.GUNPOWDER, Items.GUNPOWDER, "ingotIron"});
	
	public RitualStorms() {
		super("storms", RitualType.MEDIUM, INGREDIENTS);
	}

	@Override
	public void run(World world, BlockPos pos) {
		world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false));
	}
	
	@Override
	public boolean canRun(World world, BlockPos center) {
		return world.isRaining() && world.canBlockSeeSky(center);
	}

}
