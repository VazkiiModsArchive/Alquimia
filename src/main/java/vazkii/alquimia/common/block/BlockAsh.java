package vazkii.alquimia.common.block;

import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaBlock;
import vazkii.alquimia.common.handler.RitualHandler;
import vazkii.alquimia.common.ritual.RitualType;
import vazkii.arl.block.BlockModDust;

public class BlockAsh extends BlockModDust implements IAlquimiaBlock {

	public static final PropertyBool LIT = PropertyBool.create("lit");
	public static final PropertyBool DYING = PropertyBool.create("dying");

	public static final Predicate<IBlockState> LIT_PREDICATE = (state) -> state.getBlock() instanceof BlockAsh && state.getValue(LIT);

	public BlockAsh() {
		super("ash");
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!state.getValue(LIT)) {
			ItemStack stack = playerIn.getHeldItem(hand);
			boolean allow = false;
			SoundEvent sound = null;
			
			if(stack.getItem() == Items.FLINT_AND_STEEL) {
				stack.damageItem(1, playerIn);
				sound = SoundEvents.ITEM_FLINTANDSTEEL_USE;
				allow = true;
			} else if(stack.getItem() == Items.FIRE_CHARGE) {
				stack.shrink(1);
				sound = SoundEvents.ITEM_FIRECHARGE_USE;
				allow = true;
			}
			
			if(allow) {
				worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.BLOCKS, 1F, worldIn.rand.nextFloat() * 0.4F + 0.8F);
				lightUp(worldIn, pos, true);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(state.getValue(DYING) && !worldIn.isRemote) {
        	if(Math.random() < 0.4)
        		worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3F, 1F);
        	((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, 0.5, 0.5, 0.5, 0.0);

			worldIn.setBlockToAir(pos);
			return;
		}
		
		if(worldIn instanceof WorldServer)
			((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 8, 0.2, 0.0, 0.2, 0);
		worldIn.setBlockState(pos, state.withProperty(DYING, true));
		worldIn.scheduleUpdate(pos, state.getBlock(), 80);
		
		for(EnumFacing face : EnumFacing.HORIZONTALS) {
			EnumAttachPosition attach = getAttachPosition(worldIn, pos, face);
			BlockPos off = pos.offset(face);
			switch(attach) {
			case UP:
				lightUp(worldIn, off.up(), false);
				break;
			case SIDE:
				if(!lightUp(worldIn, off, false))
					lightUp(worldIn, off.down(), false);
				break;
			default: break;
			}
		}
	}
	
	private boolean lightUp(World world, BlockPos pos, boolean delay) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if(block == this && !state.getValue(LIT)) {
			IBlockState belowState = world.getBlockState(pos.down());
			IBlockState newState = state.withProperty(LIT, true);
			world.setBlockState(pos, newState);
			world.scheduleUpdate(pos, newState.getBlock(), delay ? 12 : 2);
			
			RitualType type = RitualHandler.getCircleType(world, pos);
			if(type != null)
				RitualHandler.addCandidate(world, pos, type);
			
			if(world instanceof WorldServer) {
	    		float x = pos.getX();
	        	float y = pos.getY() + 0.2F;
	        	float z = pos.getZ();
	        	
	        	((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + 0.5, y + 0.5, z + 0.5, 6, 0.5, 0.5, 0.5, 0.0);
	        	if(Math.random() < 0.5)
	        		world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.7F, 1F);
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if(state.getValue(LIT))
			entityIn.setFire(2);
	}
	
	@Override
	public int getColor(IBlockAccess world, IBlockState state, BlockPos pos, int tint) {
		return state.getValue(LIT) ? 0xFF9600 : 0xDDDDDD;
	}
	
	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(LIT) ? 15 : 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)  {
		return getDefaultState().withProperty(LIT, (meta & 0x1) != 0).withProperty(DYING, (meta % 0x2) != 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(LIT) ? 1 : 0) + (state.getValue(DYING) ? 2 : 0);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { NORTH, EAST, SOUTH, WEST, LIT, DYING });
	}

}
