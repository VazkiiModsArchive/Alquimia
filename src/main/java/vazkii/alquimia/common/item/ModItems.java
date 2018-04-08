package vazkii.alquimia.common.item;

import net.minecraft.item.Item;

public final class ModItems {

	public static Item lexicon;
	public static Item alchemical_ash;
	public static Item orichalcum_ingot;
	public static Item cinnabar;
	public static Item divining_rod;
	public static Item orichalcum_gear;
	public static Item test_rod;

	public static void preInit() {
		lexicon = new ItemLexicon();
		alchemical_ash = new ItemBasic("alchemical_ash");
		orichalcum_ingot = new ItemBasic("orichalcum_ingot");
		cinnabar = new ItemBasic("cinnabar");
		divining_rod = new ItemDiviningRod();
		orichalcum_gear = new ItemBasic("orichalcum_gear");
		
		test_rod = new ItemTestRod();
	}
	
}
