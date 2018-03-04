package vazkii.alquimia.common.multiblock;

import java.util.HashMap;

import net.minecraft.block.BlockFire;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.alquimia.common.multiblock.Multiblock.StateMatcher;

public class ModMultiblocks {

	public static final HashMap<ResourceLocation, Multiblock> MULTIBLOCKS = new HashMap();

	public static Multiblock crucible;

	public static void init() {
		crucible = registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "crucible"), 
				new Multiblock(new String[][] {
					{ "   ", " 0 ", "   " },
					{ "SSS", "SFS", "SSS" }},
						'0', Blocks.CAULDRON,
						'F', Blocks.FIRE,
						'S', Blocks.STONEBRICK,
						' ', new StateMatcher()))
				.setSymmetrical(true);
	}

	private static Multiblock registerMultiblock(ResourceLocation location, Multiblock multiblock) {
		MULTIBLOCKS.put(location, multiblock);
		return multiblock;
	}

}
