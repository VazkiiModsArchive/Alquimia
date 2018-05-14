package vazkii.alquimia.client.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.soap.Text;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.alquimia.common.handler.reagent.IReagentConsumer;
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
	public static void onHUDRender(RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(event.getType() == ElementType.ALL && mc.currentScreen == null) {
			EntityPlayer player = mc.player;
			ItemStack stack = player.getHeldItemMainhand();
			if(!(stack.getItem() instanceof IReagentConsumer))
				stack = player.getHeldItemOffhand();
			
			if(stack.getItem() instanceof IReagentConsumer) {
				ReagentList reagents = ((IReagentConsumer) stack.getItem()).getReagentsToConsume(stack, player);
				int reagentCount = reagents.stacks.size();
				
				int w = 18;
				ScaledResolution res = event.getResolution();
				int x = res.getScaledWidth() / 2 - (reagentCount * w) / 2;
				int y = res.getScaledHeight() - 90;
				int pad = 4;
				
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				Gui.drawRect(x - pad, y - pad, x + reagentCount * 18 + pad, y + 20 + pad, 0x22000000);
				Gui.drawRect(x - pad - 1, y - pad - 1, x + reagentCount * 18 + pad + 1, y + 20 + pad + 1, 0x22000000);

				for(int i = 0; i < reagentCount; i++) {
					ReagentStack rstack = reagents.stacks.get(i);
					int count = ReagentHandler.getCount(player, rstack.stack);
					renderReagentStack(rstack, x + 1, y, count, rstack.trueCount);
					x += w;
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onMakeTooltip(ItemTooltipEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = event.getItemStack();
		List<String> tooltip = event.getToolTip();
		if(stack.getItem() instanceof IReagentHolder) {
			ReagentList list = ReagentHandler.getReagents(stack);
			int count = list.stacks.size();
			boolean creative = ((IReagentHolder) stack.getItem()).isCreativeReagentHolder(stack);
			
			if(creative)
				tooltip.add(TextFormatting.AQUA + I18n.translateToLocal("alqmisc.creative_pouch"));
			
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
			
			for(int i = 0; i < stacks.size(); i++) {
				ReagentStack rstack = stacks.get(i);
				int x = bx + (i % STACKS_PER_LINE) * 18;
				int y = by + (i / STACKS_PER_LINE) * 20;
				renderReagentStack(rstack, x, y);
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
					if(s.inventory != mc.player.inventory)
						continue;
					
					ItemStack stack = s.getStack();
					if(stack.getItem() instanceof IReagentHolder && ReagentHandler.isValidReagent(held, stack)) {
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

			if(under != null && !held.isEmpty() && under.inventory == mc.player.inventory) {
				ItemStack stack = under.getStack();
				if(stack.getItem() instanceof IReagentHolder && ReagentHandler.isValidReagent(held, stack)) {
					mc.player.inventory.setItemStack(ItemStack.EMPTY);
					NetworkHandler.INSTANCE.sendToServer(new MessageAddToReagentHolder(under.getSlotIndex(), held));
					event.setCanceled(true);
				}
			}
		}
	}
	
	private static void renderReagentStack(ReagentStack rstack, int x, int y) {
		renderReagentStack(rstack, x, y, -1, 0);
	}
	
	private static void renderReagentStack(ReagentStack rstack, int x, int y, int count, int req) {
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.disableDepth();
		RenderItem render = mc.getRenderItem();
		
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		render.renderItemIntoGUI(rstack.stack, x, y);
		
		if(count == -1)
			count = rstack.trueCount;
		
		String s1 =  count == Integer.MAX_VALUE ? "\u221E" : TextFormatting.BOLD + Integer.toString((int) ((float) count / ReagentList.DEFAULT_MULTIPLICATION_FACTOR));
		int w1 = mc.fontRenderer.getStringWidth(s1);
		int color = 0xFFFFFF;
		if(count < req)
			color = 0xFF0000;
		
		boolean hasReq = req > 0;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 8 - w1 / 4, y + (hasReq ? 12 : 14), 0);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		mc.fontRenderer.drawStringWithShadow(s1, 0, 0, color);
		GlStateManager.popMatrix();
		
		if(hasReq) {
			if(count < req) {
				GlStateManager.enableDepth();
				Gui.drawRect(x - 1, y - 1, x + 17, y + 17, 0x44FF0000);
				GlStateManager.disableDepth();
			}
			
			float f = (float) req / ReagentList.DEFAULT_MULTIPLICATION_FACTOR;
			String fs = (f - (int) f) == 0 ? Integer.toString((int) f) : Float.toString(f);
			String s2 = TextFormatting.BOLD + "(" + fs + ")";
			int w2 = mc.fontRenderer.getStringWidth(s2);

			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 8 - w2 / 4, y + 17, 0);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			mc.fontRenderer.drawStringWithShadow(s2, 0, 0, 0x999999);
			GlStateManager.popMatrix();
		}
		GlStateManager.enableDepth();
	}

}
