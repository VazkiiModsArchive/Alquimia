package vazkii.alquimia.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaBlock;
import vazkii.alquimia.common.block.tile.TilePedestal;
import vazkii.arl.block.BlockModContainer;

public class BlockPedestal extends BlockModContainer implements IAlquimiaBlock {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.75, 0.875);
	
	public BlockPedestal() {
		super("pedestal", Material.ROCK);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof TilePedestal && hand == EnumHand.MAIN_HAND) {
			TilePedestal pedestal = (TilePedestal) tileentity;
			ItemStack stack = playerIn.getHeldItem(hand);
			
			boolean did = false;
			SoundEvent sfx = null;
			
			if(playerIn.isSneaking() && stack.isEmpty()) {
				pedestal.rotation = (pedestal.rotation + 1) % 8;
				did = true;
				sfx = SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM;
			} else if(pedestal.isEmpty()) {
				if(!stack.isEmpty()) {
					ItemStack copy = stack.copy();
					copy.setCount(1);
					if(!playerIn.isCreative())
						stack.shrink(1);
					pedestal.setInventorySlotContents(0, copy);
					did = true;
					sfx = SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM;
				}
			} else {
				ItemStack invStack = pedestal.getStackInSlot(0);
				pedestal.setInventorySlotContents(0, ItemStack.EMPTY);
				playerIn.addItemStackToInventory(invStack);
				did = true;
				sfx = SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM;
			}
			
			if(worldIn instanceof WorldServer && did) {
				worldIn.updateComparatorOutputLevel(pos, this);
				((WorldServer) worldIn).playSound(null, pos, sfx, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			return did;
		}
		
		return true;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TilePedestal) {
			int rotation = (int) ((placer.rotationYaw - 22.5 % 360F) / 45);
			((TilePedestal) tile).rotation = rotation;
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if(tileentity instanceof IInventory) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TilePedestal) {
			TilePedestal pedestal = (TilePedestal) tile;
			return pedestal.isEmpty() ? 0 : pedestal.rotation + 1;
		}
		
		return 0;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePedestal();
	}

}
