package vazkii.alquimia.client.handler;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.util.ItemStackUtil;

public class LexiconRightClickHandler {

	@SubscribeEvent
	public static void onRenderHUD(RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if(event.getType() == ElementType.ALL && /*mc.currentScreen == null &&*/ player.getHeldItemMainhand().getItem() == ModItems.lexicon) {
			Pair<LexiconEntry, Integer> hover = getHoveredEntry();
			if(hover != null) {
				LexiconEntry entry = hover.getLeft();
				if(!entry.isLocked()) {
					ScaledResolution res = event.getResolution();
					int x = res.getScaledWidth() / 2 + 3;
					int y = res.getScaledHeight() / 2 + 3;
					RenderHelper.enableGUIStandardItemLighting();
					mc.getRenderItem().renderItemAndEffectIntoGUI(entry.getIconItem(), x, y);
					GlStateManager.scale(0.5F, 0.5F, 1F);
					mc.getRenderItem().renderItemAndEffectIntoGUI(player.getHeldItemMainhand(),  (x + 8) * 2, (y + 8) * 2);
					GlStateManager.scale(2F, 2F, 1F);
					
					mc.fontRenderer.drawStringWithShadow(entry.getName(), x + 18, y + 3, 0xFFFFFF);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onRightClick(RightClickBlock event) {
		if(event.getWorld().isRemote && event.getItemStack().getItem() == ModItems.lexicon && event.getEntityPlayer().isSneaking()) {
			Pair<LexiconEntry, Integer> hover = getHoveredEntry();
			if(hover != null) {
				LexiconEntry entry = hover.getLeft();
				
				if(!entry.isLocked()) {
					int page = hover.getRight();
					GuiLexicon curr = GuiLexicon.getCurrentGui();
					GuiLexicon.currentGui = new GuiLexiconEntry(entry, page);
					
					if(curr instanceof GuiLexiconEntry) {
						GuiLexiconEntry currEntry = (GuiLexiconEntry) curr;
						if(currEntry.getEntry() == entry && currEntry.getPage() == page)
							return;
					}
					
					GuiLexicon.guiStack.push(curr);
				}
			}
		}
	}
	
	private static Pair<LexiconEntry, Integer> getHoveredEntry() {
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult res = mc.objectMouseOver;
		if(res.typeOfHit == Type.BLOCK) {
			BlockPos pos = res.getBlockPos();
			IBlockState state = mc.world.getBlockState(pos);
			Block block = state.getBlock();
			ItemStack picked = block.getPickBlock(state, res, mc.world, pos, mc.player);
			
			if(!picked.isEmpty()) 
				return LexiconRegistry.INSTANCE.recipeMappings.get(ItemStackUtil.wrapStack(picked));
		}
		
		return null;
	}
	
}
