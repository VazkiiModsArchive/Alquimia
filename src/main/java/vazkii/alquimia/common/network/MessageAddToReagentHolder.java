package vazkii.alquimia.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.alquimia.common.handler.reagent.IReagentHolder;
import vazkii.alquimia.common.handler.reagent.ReagentHandler;
import vazkii.alquimia.common.handler.reagent.ReagentList;
import vazkii.arl.network.NetworkMessage;

public class MessageAddToReagentHolder extends NetworkMessage<MessageAddToReagentHolder> {

	public int slot;
	public ItemStack stack = ItemStack.EMPTY;
	
	public MessageAddToReagentHolder() { }
	
	public MessageAddToReagentHolder(int slot) { 
		this.slot = slot;
	}
	
	public MessageAddToReagentHolder(int slot, ItemStack stack) { 
		this(slot);
		this.stack = stack;
	}
	
	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayer player = context.getServerHandler().player;
		ItemStack holder = player.inventory.getStackInSlot(slot);
		if(holder.getItem() instanceof IReagentHolder) {
			ItemStack held = player.inventory.getItemStack();
			
			if(player.isCreative() && !stack.isEmpty())
				held = stack;
			
			if(ReagentHandler.isValidReagent(held, holder)) {
				ReagentList reagents = ReagentHandler.getReagents(holder).copy();
				reagents.addStack(held);
				reagents.commit(holder);
				player.inventory.setItemStack(ItemStack.EMPTY);
			}
		}
		
		return null;
	}
	
}
