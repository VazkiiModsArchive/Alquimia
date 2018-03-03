package vazkii.alquimia.common.item;

import net.minecraft.item.Item;

public final class ModItems {

	public static Item lexicon;
	public static Item test_rod;

	public static void preInit() {
		lexicon = new ItemLexicon();
		test_rod = new ItemTestRod();
	}
	
}
