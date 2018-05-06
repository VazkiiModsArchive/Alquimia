package vazkii.alquimia.common.item;

import java.util.function.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.arl.item.ItemMod;

public class ItemAlquimia extends ItemMod implements IAlquimiaItem {

	public ItemAlquimia(String name) {
		super(name);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}
	
	protected void addBooleanPropertyOverride(String name, Predicate<ItemStack> pred) {
		addPropertyOverride(new ResourceLocation(name), new IItemPropertyGetter() {
			
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				return pred.test(stack) ? 1F : 0F;
			}
		});
	}

}
