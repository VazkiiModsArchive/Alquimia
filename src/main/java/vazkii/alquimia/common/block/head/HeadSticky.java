package vazkii.alquimia.common.block.head;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.block.interf.IAutomatonHead;
import vazkii.alquimia.common.lib.LibObfuscation;
import vazkii.alquimia.common.util.RotationUtil;

public class HeadSticky implements IAutomatonHead {

	private static final List<ResourceLocation> BLACKLIST = new LinkedList();

	// TODO: do TEs render?
	// TODO: what if a block is placed halfway through?
	// TODO: what if the head is detached halfway through?

	private static final String TAG_STATE = "state";
	private static final String TAG_TILE = "tile";

	IBlockState pickedUpState = null;
	TileEntity pickedUpTE = null;

	@Override
	public boolean onRotateStart(IAutomaton automaton) {
		if(automaton.isUp()) {
			World world = automaton.getWorld();
			EnumFacing facing = automaton.getCurrentFacing();
			EnumFacing endFacing = automaton.getCurrentRotation().rotate(facing);

			BlockPos current = automaton.getPos();
			BlockPos target = current.offset(facing);

			BlockPos end = current.offset(endFacing);
			BlockPos diag = end.offset(facing);

			if(!world.isAirBlock(end) || !world.isAirBlock(diag))
				return false;

			if(!world.isAirBlock(target)) {
				pickedUpState = world.getBlockState(target);
				pickedUpTE = world.getTileEntity(target);
				world.removeTileEntity(target);
				world.setBlockToAir(target);
			}
		}

		return true;
	}

	@Override
	public void onRotateEnd(IAutomaton automaton) {
		if(pickedUpState != null) {
			World world = automaton.getWorld();
			BlockPos target = automaton.getPos().offset(automaton.getCurrentFacing());

			if(world.isAirBlock(target)) {
				world.setBlockState(target, pickedUpState.withRotation(automaton.getCurrentRotation()));

				if(pickedUpTE != null) {
					pickedUpTE.validate();
					world.setTileEntity(target, pickedUpTE);
					pickedUpTE.updateContainingBlockInfo();
				}

				pickedUpState = null;
				pickedUpTE = null;
			}
		}
	}

	@Override
	public void writeToNBT(IAutomaton automaton, NBTTagCompound cmp) {
		NBTTagCompound innerCmp = new NBTTagCompound();
		if(pickedUpState != null)
			NBTUtil.writeBlockState(innerCmp, pickedUpState);
		cmp.setTag(TAG_STATE, innerCmp);

		innerCmp = new NBTTagCompound();
		if(pickedUpTE != null)
			pickedUpTE.writeToNBT(innerCmp);
		cmp.setTag(TAG_TILE, innerCmp);
	}

	@Override
	public void readFromNBT(IAutomaton automaton, NBTTagCompound cmp) {
		NBTTagCompound innerCmp = cmp.getCompoundTag(TAG_STATE);
		if(!innerCmp.getKeySet().isEmpty())
			pickedUpState = NBTUtil.readBlockState(innerCmp);
		else pickedUpState = null;

		innerCmp = cmp.getCompoundTag(TAG_TILE);
		if(!innerCmp.getKeySet().isEmpty())
			pickedUpTE = TileEntity.create(automaton.getWorld(), innerCmp);
		else pickedUpTE = null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean render(IAutomaton automaton, float rotation, float translation, float partTicks) {
		if(automaton.isUp() && pickedUpState != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

			GlStateManager.pushMatrix();
			GlStateManager.translate(-1.5F, 0.5F, 0.5F + 1F / 64F);
			GlStateManager.rotate(-90F, 1F, 0F, 0F);

			Rotation rotationObj = RotationUtil.fixHorizontal(RotationUtil.rotationFromFacing(automaton.getPreviousFacing()));
			renderBlockOrTE(rotationObj, pickedUpState, pickedUpTE, automaton.getWorld());
			GlStateManager.popMatrix();
		}

		return true;
	}

	@SideOnly(Side.CLIENT)
	private void renderBlockOrTE(Rotation rotation, IBlockState state, TileEntity tile, World world) {
		Minecraft mc = Minecraft.getMinecraft();
		BlockRendererDispatcher blockRenderer = mc.getBlockRendererDispatcher();
		Block block = state.getBlock();
		ResourceLocation id = Block.REGISTRY.getNameForObject(block);
		state = state.withRotation(rotation);
		
		boolean executedTERender = false;
		EnumBlockRenderType type = block.getRenderType(state);
		renderTE: {
			try {
				if(tile == null || BLACKLIST.contains(id))
					break renderTE;

				GlStateManager.pushMatrix();
				tile.setWorld(world);

				if(tile instanceof TileEntityChest) {
					TileEntityChest chest = (TileEntityChest) tile;
					chest.adjacentChestXPos = null;
					chest.adjacentChestXNeg = null;
					chest.adjacentChestZPos = null;
					chest.adjacentChestZNeg = null;
				}


				EnumFacing facing = null;
				
				if(state.getPropertyKeys().contains(BlockHorizontal.FACING))
					facing = state.getValue(BlockHorizontal.FACING);
				else if(state.getPropertyKeys().contains(BlockDirectional.FACING))
					facing = state.getValue(BlockDirectional.FACING);

				GlStateManager.translate(0F, 0F, -1F);

				// TODO fix fucked up chest rotation
				if(facing != null) {
					float rot = 90F;
					switch(facing) {
					case NORTH: 
						rot = -90F;
						break;
					case EAST:
						rot = 0F;
						break;
					case WEST:
						rot = 180F;
						break;
					default: break;
					}

					GlStateManager.translate(0.5, 0.5, 0.5);
					GlStateManager.rotate(rot, 0, 1F, 0);
					GlStateManager.translate(-0.5, -0.5, -0.5);
				}

				RenderHelper.enableStandardItemLighting();
				TileEntityRendererDispatcher.instance.render(tile, 0, 0, 0, 0);
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popMatrix();

				executedTERender = true;
			} catch(Throwable e) {
				new RuntimeException(id + " can't be rendered for TE moving", e).printStackTrace();
				BLACKLIST.add(id);
			}
		}

		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if(!executedTERender) {
			if((type == EnumBlockRenderType.ENTITYBLOCK_ANIMATED || type == EnumBlockRenderType.INVISIBLE))
				blockRenderer.renderBlockBrightness(Blocks.PLANKS.getDefaultState(), 1F);
			else blockRenderer.renderBlockBrightness(state, 1F);
		}
	}

}
