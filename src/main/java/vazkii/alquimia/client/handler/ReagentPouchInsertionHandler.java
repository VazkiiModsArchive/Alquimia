package vazkii.alquimia.client.handler;

import java.util.Arrays;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import vazkii.alquimia.common.handler.reagent.IReagentHolder;
import vazkii.alquimia.common.network.MessageAddToReagentHolder;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.RenderHelper;

public class ReagentPouchInsertionHandler {

	@SubscribeEvent
	public static void render(RenderTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(event.phase == Phase.END && gui instanceof GuiContainer) {
			GuiContainer container = (GuiContainer) gui;
			ItemStack held = mc.player.inventory.getItemStack();
			if(!held.isEmpty()) {
				Slot under = container.getSlotUnderMouse();
				for(Slot s : container.inventorySlots.inventorySlots) {
					ItemStack stack = s.getStack();
					if(stack.getItem() instanceof IReagentHolder) {
						int x = container.getGuiLeft() + s.xPos;
						int y = container.getGuiTop() + s.yPos;
						GlStateManager.disableDepth();
						mc.fontRenderer.drawStringWithShadow("+", x + 10, y + 8, 0xFFFF00);
						GlStateManager.enableDepth();
						
						if(s == under)
							RenderHelper.renderTooltip(x, y, Arrays.asList(I18n.translateToLocal("alqmisc.right_click_add")));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void click(GuiScreenEvent.MouseInputEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui instanceof GuiContainer && Mouse.getEventButton() == 1) {
			GuiContainer container = (GuiContainer) gui;
			Slot under = container.getSlotUnderMouse();
			ItemStack held = mc.player.inventory.getItemStack();
			
			if(under != null && !held.isEmpty()) {
				ItemStack stack = under.getStack();
				if(stack.getItem() instanceof IReagentHolder) {
					mc.player.inventory.setItemStack(ItemStack.EMPTY);
					NetworkHandler.INSTANCE.sendToServer(new MessageAddToReagentHolder(under.slotNumber));
					event.setCanceled(true);
				}
			}
		}
	}
	
}
