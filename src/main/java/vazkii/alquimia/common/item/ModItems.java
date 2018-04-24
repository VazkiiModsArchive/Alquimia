package vazkii.alquimia.common.item;

import net.minecraft.item.Item;
import vazkii.alquimia.common.block.head.HeadBlade;
import vazkii.alquimia.common.block.head.HeadDrill;
import vazkii.alquimia.common.block.head.HeadSticky;
import vazkii.alquimia.common.item.instruction.ItemInstructionClockwise;
import vazkii.alquimia.common.item.instruction.ItemInstructionCounterclockwise;
import vazkii.alquimia.common.item.instruction.ItemInstructionDown;
import vazkii.alquimia.common.item.instruction.ItemInstructionNop;
import vazkii.alquimia.common.item.instruction.ItemInstructionRandomTurn;
import vazkii.alquimia.common.item.instruction.ItemInstructionRepeat;
import vazkii.alquimia.common.item.instruction.ItemInstructionUp;
import vazkii.alquimia.common.lib.LibMisc;

public final class ModItems {

	public static Item lexicon;
	public static Item alchemical_ash;
	public static Item orichalcum_ingot;
	public static Item cinnabar;
	public static Item divining_rod;
	public static Item orichalcum_gear;
	public static Item sticky_head;
	public static Item instruction_clockwise;
	public static Item instruction_counterclockwise;
	public static Item instruction_up;
	public static Item instruction_down;
	public static Item instruction_nop;
	public static Item instruction_random_turn;
	public static Item instruction_repeat;
	public static Item blade_head;
	public static Item drill_head;

	public static Item test_rod;

	public static void preInit() {
		lexicon = new ItemLexicon();
		alchemical_ash = new ItemAlquimia("alchemical_ash");
		orichalcum_ingot = new ItemAlquimia("orichalcum_ingot");
		cinnabar = new ItemCinnabar();
		divining_rod = new ItemDiviningRod();
		orichalcum_gear = new ItemAlquimia("orichalcum_gear");
		sticky_head = new ItemAutomatonHead("sticky_head", HeadSticky::new);
		instruction_clockwise = new ItemInstructionClockwise();
		instruction_counterclockwise = new ItemInstructionCounterclockwise();
		instruction_up = new ItemInstructionUp();
		instruction_down = new ItemInstructionDown();
		instruction_nop = new ItemInstructionNop();
		instruction_random_turn = new ItemInstructionRandomTurn();
		instruction_repeat = new ItemInstructionRepeat();
		blade_head = new ItemAutomatonHead("blade_head", HeadBlade::new);
		drill_head = new ItemAutomatonHead("drill_head", HeadDrill::new);

		if(LibMisc.IS_DEBUG)
			test_rod = new ItemTestRod();
	}
	
}
