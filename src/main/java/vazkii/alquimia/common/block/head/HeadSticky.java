package vazkii.alquimia.common.block.head;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.common.block.BlockAutomaton;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.block.interf.IAutomatonHead;
import vazkii.alquimia.common.util.AutomatonUtil;
import vazkii.alquimia.common.util.RotationUtil;

public class HeadSticky implements IAutomatonHead {

	private static final List<ResourceLocation> BLACKLIST = new LinkedList();

	private static final String TAG_STATE = "state";
	private static final String TAG_TILE = "tile";

	IBlockState pickedUpState = null;
	TileEntity pickedUpTE = null;

	@Override
	public void onRemoved(IAutomaton automaton) {
		World world = automaton.getWorld();
		EnumFacing facing = automaton.getCurrentFacing();
		EnumFacing endFacing = automaton.getCurrentRotation().rotate(facing);
		BlockPos end = automaton.getPos().offset(endFacing);
		IBlockState state = pickedUpState;

		if(state != null) {
			Block block = state.getBlock();

			placePickedUpBlock(automaton, world, end);
			block.dropBlockAsItem(world, end, state, 0);
			world.setBlockToAir(end);
		}

		if(world.getBlockState(end).getBlock() == ModBlocks.placeholder)
			world.setBlockToAir(end);
	}

	@Override
	public boolean onRotateStart(IAutomaton automaton) {
		if(automaton.isUp()) {
			World world = automaton.getWorld();
			EnumFacing facing = automaton.getCurrentFacing();
			EnumFacing endFacing = automaton.getCurrentRotation().rotate(facing);
			BlockPos current = automaton.getPos();
			BlockPos target = current.offset(facing);
			BlockPos end = current.offset(endFacing);

			if(AutomatonUtil.hasObstruction(automaton, false))
				return false;

			if(!world.isAirBlock(target)) {
				pickedUpState = world.getBlockState(target);
				pickedUpTE = world.getTileEntity(target);
				world.removeTileEntity(target);
				world.setBlockToAir(target);
				world.setBlockState(end, ModBlocks.placeholder.getDefaultState());
			}
		}

		return true;
	}

	@Override
	public void onRotateEnd(IAutomaton automaton) {
		if(pickedUpState != null) {
			World world = automaton.getWorld();
			BlockPos target = automaton.getPos().offset(automaton.getCurrentFacing());

			if(world.isAirBlock(target) || world.getBlockState(target).getBlock() == ModBlocks.placeholder)
				placePickedUpBlock(automaton, world, target);
		}
	}

	@Override
	public void onTicked(IAutomaton automaton) {
		if(automaton.isUp() && pickedUpState != null) {
			int time = automaton.getInstructionTime();
			World world = automaton.getWorld();
			EnumFacing endFacing = automaton.getCurrentFacing();
			EnumFacing facing = automaton.getCurrentRotation().rotate(endFacing.getOpposite());

			BlockPos current = automaton.getPos();
			BlockPos target = current.offset(facing);

			BlockPos end = current.offset(endFacing);
			BlockPos diag = end.offset(facing);
			
			boolean halfway = (time >= automaton.getSpeed() / 2);
			AutomatonUtil.moveEntitiesAt(world, halfway ? end : diag, halfway ? facing.getOpposite() : endFacing, 0.25F);
		}
	}
	
	private void placePickedUpBlock(IAutomaton automaton, World world, BlockPos target) {
		world.setBlockState(target, pickedUpState.withRotation(automaton.getCurrentRotation()));

		if(pickedUpTE != null) {
			pickedUpTE.validate();
			world.setTileEntity(target, pickedUpTE);
			pickedUpTE.updateContainingBlockInfo();
		}

		pickedUpState = null;
		pickedUpTE = null;
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
	public void render(IAutomaton automaton, ItemStack stack, float translation, float partTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		RenderItem render = mc.getRenderItem(); 

		translation *= -0.3F;
		GlStateManager.translate(translation , 0F, 0F);
		render.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
		GlStateManager.translate(-translation , 0F, 0F);

		if(automaton.isUp() && pickedUpState != null) {
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

			GlStateManager.pushMatrix();
			GlStateManager.translate(-1.5F, 0.5F, BlockAutomaton.AABB.maxY + 1F / 32F);
			GlStateManager.rotate(-90F, 1F, 0F, 0F);

			Rotation rotationObj = RotationUtil.fixHorizontal(RotationUtil.rotationFromFacing(automaton.getPreviousFacing()));
			renderBlockOrTE(rotationObj, pickedUpState, pickedUpTE, automaton.getWorld());
			GlStateManager.popMatrix();
		}
	}

	@SideOnly(Side.CLIENT)
	private void renderBlockOrTE(Rotation rotation, IBlockState state, TileEntity tile, World world) {
		Minecraft mc = Minecraft.getMinecraft();
		BlockRendererDispatcher blockRenderer = mc.getBlockRendererDispatcher();
		Block block = state.getBlock();
		ResourceLocation id = Block.REGISTRY.getNameForObject(block);

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

				GlStateManager.translate(0F, 0F, -1F);
				GlStateManager.translate(0.5, 0.5, 0.5);
				GlStateManager.rotate(-90F, 0, 1F, 0);
				GlStateManager.translate(-0.5, -0.5, -0.5);

				RenderHelper.enableStandardItemLighting();
				TileEntityRendererDispatcher.instance.render(tile, 0, 0, 0, 0);
				GlStateManager.disableRescaleNormal();
				GlStateManager.popMatrix();

				executedTERender = true;
			} catch(Throwable e) {
				new RuntimeException(id + " can't be rendered for TE moving", e).printStackTrace();
				BLACKLIST.add(id);
			}
		}

		state = state.withRotation(rotation);
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if(!executedTERender && (type == EnumBlockRenderType.ENTITYBLOCK_ANIMATED || type == EnumBlockRenderType.INVISIBLE))
			blockRenderer.renderBlockBrightness(Blocks.PLANKS.getDefaultState(), 1F);
		else if(type == EnumBlockRenderType.MODEL)
			blockRenderer.renderBlockBrightness(state, 1F);
	}

}
