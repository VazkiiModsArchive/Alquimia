package vazkii.alquimia.client.handler;

import java.awt.Color;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.alquimia.client.base.PersistentData.DataHolder.Bookmark;
import vazkii.alquimia.common.lib.LibObfuscation;
import vazkii.alquimia.common.multiblock.Multiblock;
import vazkii.alquimia.common.multiblock.Multiblock.StateMatcher;
import vazkii.alquimia.common.util.RotationUtil;
import vazkii.arl.util.ClientTicker;

public class MultiblockVisualizationHandler {

	public static boolean hasMultiblock;
	public static Bookmark bookmark;
	
	private static Multiblock multiblock;
	private static String name;
	private static BlockPos pos;
	private static boolean isAnchored;
	private static Rotation facingRotation;
	private static Function<BlockPos, BlockPos> offsetApplier;
	private static int blocks, blocksDone;
	private static int timeComplete;
	private static IBlockState lookingState;
	private static BlockPos lookingPos;

	public static void setMultiblock(Multiblock multiblock, String name, Bookmark bookmark, boolean flip) {
		setMultiblock(multiblock, name, bookmark, flip, pos->pos);
	}
	
	public static void setMultiblock(Multiblock multiblock, String name, Bookmark bookmark, boolean flip, Function<BlockPos, BlockPos> offsetApplier) {
		if(flip && hasMultiblock)
			hasMultiblock = false;
		else {
			MultiblockVisualizationHandler.multiblock = multiblock;
			MultiblockVisualizationHandler.name = name;
			MultiblockVisualizationHandler.bookmark = bookmark;
			MultiblockVisualizationHandler.offsetApplier = offsetApplier;
			pos = null;
			hasMultiblock = true;
			isAnchored = false;
		}
	}

	@SubscribeEvent
	public static void onRenderHUD(RenderGameOverlayEvent.Post event) {
		if(event.getType() == ElementType.ALL && hasMultiblock) {
			int waitTime = 40;
			int fadeOutSpeed = 4;
			int fullAnimTime = waitTime + 10;
			float animTime = timeComplete + (timeComplete == 0 ? 0 : event.getPartialTicks());

			if(animTime > fullAnimTime) {
				hasMultiblock = false;
				return;
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, -Math.max(0, animTime - waitTime) * fadeOutSpeed, 0);

			int x = event.getResolution().getScaledWidth() / 2;
			int y = 12;

			Minecraft mc = Minecraft.getMinecraft();
			mc.fontRenderer.drawStringWithShadow(name, x - mc.fontRenderer.getStringWidth(name) / 2, y , 0xFFFFFF);

			int width = 180;
			int height = 9;
			int left = x - width / 2;
			int top = y + 10;

			if(timeComplete > 0) {
				String s = I18n.translateToLocal("alquimia.gui.lexicon.structure_complete");
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, Math.min(height + 5, animTime), 0);
				mc.fontRenderer.drawStringWithShadow(s, x - mc.fontRenderer.getStringWidth(s) / 2, top + height - 10, 0x00FF00);
				GlStateManager.popMatrix();
			}

			Gui.drawRect(left - 1, top - 1, left + width + 1, top + height + 1, 0xFF000000);
			vazkii.arl.util.RenderHelper.drawGradientRect(left, top, 0F, left + width, top + height, 0xFF666666, 0xFF555555);

			float fract = (float) blocksDone / Math.max(1, blocks);
			int progressWidth = (int) ((float) width * fract);
			int color = MathHelper.hsvToRGB(fract / 3.0F, 1.0F, 1.0F) | 0xFF000000;
			int color2 = new Color(color).darker().getRGB();
			vazkii.arl.util.RenderHelper.drawGradientRect(left, top, 0F, left + progressWidth, top + height, color, color2);

			if(!isAnchored) {
				String s = I18n.translateToLocal("alquimia.gui.lexicon.not_anchored");
				mc.fontRenderer.drawStringWithShadow(s, x - mc.fontRenderer.getStringWidth(s) / 2, top + height + 8, 0xFFFFFF);
			} else {
				if(lookingState != null) {
					Block block = lookingState.getBlock();
					ItemStack stack = block.getPickBlock(lookingState, mc.objectMouseOver, mc.world, lookingPos, mc.player);

					if(!stack.isEmpty()) {
						mc.fontRenderer.drawStringWithShadow(stack.getDisplayName(), left + 20, top + height + 8, 0xFFFFFF);
						RenderHelper.enableGUIStandardItemLighting();
						mc.getRenderItem().renderItemIntoGUI(stack, left, top + height + 2);
					}
				}

				if(timeComplete == 0) {
					String progress = blocksDone + "/" + blocks;
					mc.fontRenderer.drawStringWithShadow(progress, left + width - mc.fontRenderer.getStringWidth(progress), top + height + 2, 0xFFFFFF);
				}
			}

			GlStateManager.popMatrix();
		}
	}

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		if(hasMultiblock && multiblock != null)
			renderMultiblock(Minecraft.getMinecraft().world);
	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		if(hasMultiblock && !isAnchored) {
			pos = event.getPos();
			facingRotation = RotationUtil.rotationFromFacing(event.getEntityPlayer().getHorizontalFacing());
			isAnchored = true;
		}
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
		if(Minecraft.getMinecraft().world == null)
			hasMultiblock = false;
		else if(isAnchored && blocks == blocksDone) {
			timeComplete++;
			if(timeComplete == 14)
				Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F));
		} else timeComplete = 0;
	}

	public static void renderMultiblock(World world) {
		Minecraft mc = Minecraft.getMinecraft();
		if(!isAnchored) {
			facingRotation = RotationUtil.rotationFromFacing(mc.player.getHorizontalFacing());
			pos = mc.objectMouseOver.getBlockPos();
		}

		if(pos == null)
			return;

		RenderManager manager = Minecraft.getMinecraft().getRenderManager();
		BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		double posX = ReflectionHelper.getPrivateValue(RenderManager.class, manager, LibObfuscation.RenderManager.RENDER_POS_X);
		double posY = ReflectionHelper.getPrivateValue(RenderManager.class, manager, LibObfuscation.RenderManager.RENDER_POS_Y);
		double posZ = ReflectionHelper.getPrivateValue(RenderManager.class, manager, LibObfuscation.RenderManager.RENDER_POS_Z);

		GlStateManager.pushMatrix();
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.translate(-posX, -posY, -posZ);

		BlockPos checkPos = mc.objectMouseOver.typeOfHit == Type.BLOCK ? mc.objectMouseOver.getBlockPos().offset(mc.objectMouseOver.sideHit) : null;
		BlockPos startPos = offsetApplier.apply(pos);
		if(!multiblock.isSymmetrical())
			startPos = startPos.add(RotationUtil.x(facingRotation, -multiblock.offX, -multiblock.offZ), -multiblock.offY, RotationUtil.z(facingRotation, -multiblock.offX, -multiblock.offZ));
		else startPos = startPos.add(multiblock.offX, multiblock.offY, multiblock.offZ);
		
		blocks = blocksDone = 0;
		lookingState = null;
		lookingPos = checkPos;

		for(int x = 0; x < multiblock.sizeX; x++)
			for(int y = 0; y < multiblock.sizeY; y++)
				for(int z = 0; z < multiblock.sizeZ; z++) {
					float alpha = 0.3F;
					BlockPos renderPos = startPos.add(RotationUtil.x(facingRotation, x, z) , y, RotationUtil.z(facingRotation, x , z));
					StateMatcher matcher = multiblock.stateTargets[x][y][z];
					if(renderPos.equals(checkPos)) {
						lookingState = matcher.displayState;
						alpha = 0.6F + (float) (Math.sin(ClientTicker.total * 0.3F) + 1F) * 0.1F;
					}

					if(matcher != StateMatcher.ANY) {
						blocks++;
						if(!multiblock.test(world, startPos, x, y, z, facingRotation)) {
							IBlockState renderState = matcher.displayState.withRotation(facingRotation);
							renderBlock(world, renderState, renderPos, alpha, dispatcher);
						} else blocksDone++;
					}
				}

		if(!isAnchored)
			blocks = blocksDone = 0;

		GL11.glPopAttrib();
		GL14.glBlendColor(1F, 1F, 1F, 1F);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}

	public static void renderBlock(World world, IBlockState state, BlockPos pos, float alpha, BlockRendererDispatcher brd) {
		if(pos != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.rotate(-90F, 0F, 1F, 0F);
			GL14.glBlendColor(1F, 1F, 1F, alpha);

			if(state.getBlock() == Blocks.AIR) {
				float scale = 0.3F;
				float off = (1F - scale) / 2;
				GlStateManager.translate(off, off, -off);
				GlStateManager.scale(scale, scale, scale);

				brd.renderBlockBrightness(Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED), 1.0F);
			} else brd.renderBlockBrightness(state, 1.0F);

			GlStateManager.popMatrix();
		}
	}


}
