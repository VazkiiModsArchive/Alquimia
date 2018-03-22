package vazkii.alquimia.common.item;

import net.minecraft.item.Item;

public final class ModItems {

	public static Item lexicon;
	public static Item alchemical_ash;
	public static Item cinnabar;
	public static Item divining_rod;
	public static Item test_rod;

	public static void preInit() {
		lexicon = new ItemLexicon();
		alchemical_ash = new ItemBasic("alchemical_ash");
		cinnabar = new ItemBasic("cinnabar");
		divining_rod = new ItemDiviningRod();
		
		test_rod = new ItemTestRod();
	}
	
}
