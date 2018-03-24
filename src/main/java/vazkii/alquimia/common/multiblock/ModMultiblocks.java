package vazkii.alquimia.common.multiblock;

import java.util.HashMap;

import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.block.BlockAsh;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.alquimia.common.multiblock.Multiblock.StateMatcher;

public class ModMultiblocks {

	public static final HashMap<ResourceLocation, Multiblock> MULTIBLOCKS = new HashMap();

	public static Multiblock crucible;
	public static Multiblock small_ritual_circle;
	public static Multiblock small_ritual_circle_lit;
	public static Multiblock weird_bone_thing;

	public static void preInit() {
		crucible = registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "crucible"), 
				new Multiblock(new String[][] {
					{ "   ", " 0 ", "   " },
					{ "SSS", "SFS", "SSS" }},
						'0', Blocks.CAULDRON,
						'F', Blocks.FIRE,
						'S', Blocks.STONEBRICK,
						' ', StateMatcher.ANY))
				.setSymmetrical(true);



		weird_bone_thing = registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "weird_bone_thing"), 
				new Multiblock(new String[][] {
					{ "   ", "   ", "   ", "   ", " D " },
					{ "   ", "   ", "   ", "   ", " X " },
					{ "XXX", "X0X", "XXX", " X ", " X " }},
						'X', Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y),
						'D', Blocks.DIAMOND_BLOCK))
				.offset(0, -1, 0);
		
		 buildRitualCircles();
	}
	
	public static void buildRitualCircles() {
		StateMatcher lit = StateMatcher.fromPredicate(ModBlocks.ash, BlockAsh.LIT_PREDICATE);
		String[][] small = new String[][] {{
			    "       ", 
				"  A0A  ", 
				" AA AA ", 
				" A   A ", 
				" AA AA ", 
				"  AAA  ", 
				"       " }};
		Object[] litPattern = new Object[]{
				'0', lit,
				'A', lit
		};
		Object[] unlitPattern = new Object[]{
				'0', ModBlocks.ash,
				'A', ModBlocks.ash
		};

		small_ritual_circle = registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "small_ritual_circle"), 
				new Multiblock(small, unlitPattern)).setSymmetrical(true);
		small_ritual_circle_lit = registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "small_ritual_circle_lit"), 
				new Multiblock(small, litPattern)).setSymmetrical(true);
	}

	private static Multiblock registerMultiblock(ResourceLocation location, Multiblock multiblock) {
		MULTIBLOCKS.put(location, multiblock);
		return multiblock;
	}

}
