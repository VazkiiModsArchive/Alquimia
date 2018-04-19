package vazkii.alquimia.common.item.instruction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.arl.util.ItemNBTHelper;

public class ItemInstructionRepeat extends ItemInstruction {

	private static final String TAG_REPEAT = "repeat";
	
	public ItemInstructionRepeat() {
		super("instruction_repeat");
		
		addPropertyOverride(new ResourceLocation("repeat"), new IItemPropertyGetter() {
			
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				return getRepeatStatus(stack) ? 1F : 0F;
			}
			
		});
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(getRepeatStatus(stack))
			setRepeatStatus(stack, false);
	}

	@Override
	public boolean scan(ItemStack stack, IAutomaton automaton) {
		boolean repeat = getRepeatStatus(stack);
		 
		if(!automaton.getWorld().isRemote)
			setRepeatStatus(stack, !repeat);
		
		if(!repeat)
			automaton.rewind();
		
		return false;
	}
	
	@Override
	public void run(ItemStack stack, IAutomaton automaton) {
		// no need for anything here
	}
	
	private boolean getRepeatStatus(ItemStack stack) {
		return stack.hasTagCompound() && ItemNBTHelper.getBoolean(stack, TAG_REPEAT, false);
	}
	
	private void setRepeatStatus(ItemStack stack, boolean repeat) {
		ItemNBTHelper.setBoolean(stack, TAG_REPEAT, repeat);
		NBTTagCompound cmp = stack.getTagCompound();
		if(cmp.getSize() == 1 && !repeat)
			stack.setTagCompound(null);
	}
	
}
