package vazkii.alquimia.common.block.head;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.block.interf.IAutomatonHead;

public class HeadSticky implements IAutomatonHead {

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
				world.setBlockState(target, pickedUpState);
				if(pickedUpTE != null)
					world.setTileEntity(target, pickedUpTE);
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
	public boolean render(IAutomaton automaton, float translation, float partTicks) {
		if(automaton.isUp() && pickedUpState != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(-1.5F, 0.5F, 0.5F + 1F / 64F);
			GlStateManager.rotate(-90F, 1F, 0F, 0F);
			dispatcher.renderBlockBrightness(pickedUpState, 1F);
			GlStateManager.popMatrix();
		}
		
		return true;
	}
	
}
