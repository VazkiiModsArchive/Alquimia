package vazkii.alquimia.common.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.alquimia.common.block.tile.TilePedestal;
import vazkii.alquimia.common.lib.LibMisc;

public class ModBlocks {

	public static Block ash;
	public static Block pedestal;

	public static void preInit() {
		ash = new BlockAsh();
		pedestal = new BlockPedestal();
		
		initTileEntities();
	}
	
	private static void initTileEntities() {
		registerTile(TilePedestal.class, "pedestal");
	}
	
	private static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntity(clazz, LibMisc.PREFIX_MOD + key);
	}
	
}
