package vazkii.alquimia.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.arl.block.tile.TileSimpleInventory;

public class TilePedestal extends TileSimpleInventory {

	private static final String TAG_ROTATION = "rotation";
	
	public int rotation = 0;
	
	@Override
	public void writeSharedNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeSharedNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_ROTATION, rotation);
	}
	
	@Override
	public void readSharedNBT(NBTTagCompound par1nbtTagCompound) {
		super.readSharedNBT(par1nbtTagCompound);
		rotation = par1nbtTagCompound.getInteger(TAG_ROTATION);
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
}
