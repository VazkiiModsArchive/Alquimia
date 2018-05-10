package vazkii.alquimia.client.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.soap.Text;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLSync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.RenderTooltipEvent.PostText;
import net.minecraftforge.client.event.RenderTooltipEvent.Pre;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.alquimia.common.handler.reagent.IReagentHolder;
import vazkii.alquimia.common.handler.reagent.ReagentHandler;
import vazkii.alquimia.common.handler.reagent.ReagentList;
import vazkii.alquimia.common.handler.reagent.ReagentStack;
import vazkii.alquimia.common.network.MessageAddToReagentHolder;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.RenderHelper;
import vazkii.arl.util.TooltipHandler;

public class ReagentPouchEventHandler {

	private static final int STACKS_PER_LINE = 8;

	@SubscribeEvent
	public static void onMakeTooltip(ItemTooltipEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = event.getItemStack();
		List<String> tooltip = event.getToolTip();
		if(stack.getItem() instanceof IReagentHolder) {
			ReagentList list = ReagentHandler.getReagents(stack);
			int count = list.stacks.size();
			
			if(count > 0)
				TooltipHandler.tooltipIfShift(tooltip, () -> {
					int lines = (((count - 1) / STACKS_PER_LINE) + 1) * 2;
					int width = Math.min(STACKS_PER_LINE, count) * 18;
					String spaces = "\u00a7r\u00a7r\u00a7r\u00a7r\u00a7r";
					while(mc.fontRenderer.getStringWidth(spaces) < width)
						spaces += " ";
					
					for(int i = 0; i < lines; i++)
						tooltip.add(spaces);
				});
		}
	}
	
	@SubscribeEvent
	public static void onDrawTooltip(RenderTooltipEvent.PostText event) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = event.getStack();
		if(stack.getItem() instanceof IReagentHolder && GuiScreen.isShiftKeyDown()) {
			ReagentList list = ReagentHandler.getReagents(stack);
			int count = list.stacks.size();
			
			List<ReagentStack> stacks = new ArrayList(list.stacks);
			Collections.sort(stacks);
			
			int bx = event.getX();
			int by = event.getY();
			
			List<String> tooltip = event.getLines();
			int lines = (((count - 1) / STACKS_PER_LINE) + 1);
			int width = Math.min(STACKS_PER_LINE, count) * 18;
			int height = lines * 20 + 1;
			
			for(String s : tooltip) {
				if(s.trim().equals("\u00a77\u00a7r\u00a7r\u00a7r\u00a7r\u00a7r"))
					break;
				else by += 10;
			}
			
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Gui.drawRect(bx, by, bx + width, by + height, 0x55000000);
			
			RenderItem render = mc.getRenderItem();
			for(int i = 0; i < stacks.size(); i++) {
				ReagentStack rstack = stacks.get(i);
				int x = bx + (i % STACKS_PER_LINE) * 18;
				int y = by + (i / STACKS_PER_LINE) * 20;
				render.renderItemIntoGUI(rstack.stack, x, y);	
				
				String s = TextFormatting.BOLD + Integer.toString((int) Math.ceil((float) rstack.trueCount / ReagentList.DEFAULT_MULTIPLICATION_FACTOR));
				int w = mc.fontRenderer.getStringWidth(s);
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 8 - w / 4, y + 16, 0);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				mc.fontRenderer.drawStringWithShadow(s, 0, 0, 0xFFFFFF);
				GlStateManager.popMatrix();
			}
		}
	}
	
	@SubscribeEvent
	public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui instanceof GuiContainer) {
			GuiContainer container = (GuiContainer) gui;
			ItemStack held = mc.player.inventory.getItemStack();
			if(!held.isEmpty()) {
				Slot under = container.getSlotUnderMouse();
				for(Slot s : container.inventorySlots.inventorySlots) {
					ItemStack stack = s.getStack();
					if(stack.getItem() instanceof IReagentHolder) {
						if(s == under) {
							int x = event.getMouseX();
							int y = event.getMouseY();
							RenderHelper.renderTooltip(x, y, Arrays.asList(I18n.translateToLocal("alqmisc.right_click_add")));
						} else {
							int x = container.getGuiLeft() + s.xPos;
							int y = container.getGuiTop() + s.yPos;
							
							GlStateManager.disableDepth();
							mc.fontRenderer.drawStringWithShadow("+", x + 10, y + 8, 0xFFFF00);
							GlStateManager.enableDepth();
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRightClick(GuiScreenEvent.MouseInputEvent.Pre event) {
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
					NetworkHandler.INSTANCE.sendToServer(new MessageAddToReagentHolder(under.getSlotIndex()));
					event.setCanceled(true);
				}
			}
		}
	}

}
