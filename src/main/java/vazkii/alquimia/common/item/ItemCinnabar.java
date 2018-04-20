package vazkii.alquimia.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import vazkii.alquimia.common.item.interf.IRitualConsumedCallback;
import vazkii.alquimia.common.ritual.Ritual;

public class ItemCinnabar extends ItemAlquimia implements IRitualConsumedCallback {

	public ItemCinnabar() {
		super("cinnabar");
	}

	@Override
	public ItemStack consumeForRitual(ItemStack stack, Ritual ritual, TileEntity tile) {
		if(stack.getItem() == ModItems.cinnabar) {
			WorldServer world = (WorldServer) tile.getWorld(); 
			BlockPos pos = tile.getPos();
			world.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 20, 0.3, 0.7, 0.3, 0F);
		}
		
		return ItemStack.EMPTY;
	}

}
