package vazkii.alquimia.client.lexicon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LexiconUtils {

	public static ItemStack loadStackFromString(String res) {
		int meta = 0;
		String[] tokens = res.split(":");
		if(tokens.length < 2)
			return ItemStack.EMPTY;
		
		if(tokens.length == 3)
			meta = Integer.parseInt(tokens[2]);
		
		Item item = Item.REGISTRY.getObject(new ResourceLocation(tokens[0], tokens[1]));
		return new ItemStack(item, 1, meta);
	}
	
}
