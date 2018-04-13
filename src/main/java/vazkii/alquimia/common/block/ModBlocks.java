package vazkii.alquimia.common.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.alquimia.common.block.decor.BlockAlchemicalAshBlock;
import vazkii.alquimia.common.block.decor.BlockAshBlock;
import vazkii.alquimia.common.block.decor.BlockCinnabarBlock;
import vazkii.alquimia.common.block.decor.BlockOrichalcum;
import vazkii.alquimia.common.block.tile.TileAutomaton;
import vazkii.alquimia.common.block.tile.TilePedestal;
import vazkii.alquimia.common.lib.LibMisc;

public class ModBlocks {

	public static Block ash;
	public static Block pedestal;
	public static Block ash_block;
	public static Block alchemical_ash_block;
	public static Block cinnabar_block;
	public static Block automaton;
	public static Block placeholder;
	public static Block orichalcum_block;

	public static void preInit() {
		ash = new BlockAsh();
		pedestal = new BlockPedestal();
		ash_block = new BlockAshBlock();
		alchemical_ash_block = new BlockAlchemicalAshBlock();
		cinnabar_block = new BlockCinnabarBlock();
		automaton = new BlockAutomaton();
		placeholder = new BlockPlaceholder();
		orichalcum_block = new BlockOrichalcum();
		
		initTileEntities();
	}
	
	private static void initTileEntities() {
		registerTile(TilePedestal.class, "pedestal");
		registerTile(TileAutomaton.class, "automaton");
	}
	
	private static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntity(clazz, LibMisc.PREFIX_MOD + key);
	}
	
}
