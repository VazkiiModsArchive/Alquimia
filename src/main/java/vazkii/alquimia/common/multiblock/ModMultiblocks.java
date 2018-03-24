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
	public static Multiblock[] small_ritual_circle;
	public static Multiblock[] medium_ritual_circle;
	public static Multiblock[] large_ritual_circle;
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
		StateMatcher pedestal = StateMatcher.fromPredicate(Blocks.AIR, (state) -> state.getBlock() == Blocks.AIR || state.getBlock() == Blocks.DISPENSER); // TODO

		String[][] small = new String[][] {{
			"__PPP__", 
			"_PA0AP_", 
			"PAA AAP", 
			"PA   AP", 
			"PAA AAP", 
			"_PAAAP_", 
			"__PPP__" 
		}};

		String[][] medium = new String[][] {{
			"___PPP___", 
			"__PA0AP__",
			"_PAA AAP_", 
			"PAA   AAP",
			"PA     AP",
			"PAA   AAP",
			"_PAA AAP_",
			"__PAAAP__",
			"___PPP___"
		}};
		
		String[][] large = new String[][] {{
			"___PPPPP___",
			"__PAA0AAP__",
			"_PAA   AAP_",
			"PAA     AAP",
			"PA       AP",
			"PA       AP",
			"PA       AP",
			"PAA     AAP",
			"_PAA   AAP_",
			"__PAAAAAP__",
			"___PPPPP___"
		}};

		Object[] litPattern = new Object[]{
				'0', lit,
				'A', lit,
				'P', pedestal,
				'_', StateMatcher.ANY
		};
		Object[] unlitPattern = new Object[]{
				'0', ModBlocks.ash,
				'A', ModBlocks.ash,
				'P', pedestal,
				'_', StateMatcher.ANY
		};

		small_ritual_circle = new Multiblock[] {
				registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "small_ritual_circle"),
						new Multiblock(small, unlitPattern)).setSymmetrical(true),
				registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "small_ritual_circle_lit"),
						new Multiblock(small, litPattern)).setSymmetrical(true)
		};
		
		medium_ritual_circle = new Multiblock[] {
				registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "medium_ritual_circle"),
						new Multiblock(medium, unlitPattern)).setSymmetrical(true),
				registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "medium_ritual_circle_lit"),
						new Multiblock(medium, litPattern)).setSymmetrical(true)
		};
		
		large_ritual_circle = new Multiblock[] {
				registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "large_ritual_circle"),
						new Multiblock(large, unlitPattern)).setSymmetrical(true),
				registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "large_ritual_circle_lit"),
						new Multiblock(large, litPattern)).setSymmetrical(true)
		};
	}

	private static Multiblock registerMultiblock(ResourceLocation location, Multiblock multiblock) {
		MULTIBLOCKS.put(location, multiblock);
		return multiblock;
	}

}
