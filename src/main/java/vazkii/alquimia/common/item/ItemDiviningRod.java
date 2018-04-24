package vazkii.alquimia.common.item;

import java.util.Random;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.common.base.AlquimiaSounds;
import vazkii.arl.util.ItemNBTHelper;

public class ItemDiviningRod extends ItemAlquimia {

	private static final String TAG_HAS_TARGET = "hasTarget";
	private static final String TAG_TARGET_X = "targetX";
	private static final String TAG_TARGET_Y = "targetY";
	private static final String TAG_TARGET_Z = "targetZ";
	private static final String TAG_SEED = "seed";
	private static final String TAG_RNG_SEED = "rngSeed";

	public ItemDiviningRod() {
		super("divining_rod");
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
		
		addPropertyOverride(new ResourceLocation("located"), new IItemPropertyGetter() {
			
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
            	return ItemNBTHelper.getBoolean(stack, TAG_HAS_TARGET, false) ? 1 : 0;
            }
            
        });
	}

	@SubscribeEvent
	public void onBlockDrops(BlockEvent.HarvestDropsEvent event) {
		IBlockState state = event.getState();
		EntityPlayer player = event.getHarvester();

		if(isStone(state) && !event.getDrops().isEmpty() && player != null) {
			World world = event.getWorld();
			ItemStack pick = player.getHeldItemMainhand();
			ItemStack rod = player.getHeldItemOffhand();
			
			if(rod.getItem() == this && ItemNBTHelper.getBoolean(rod, TAG_HAS_TARGET, false) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, pick) == 0) {
				int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, pick);
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(ModItems.cinnabar, 2 + world.rand.nextInt(3 + fortune)));
				
				setHasTarget(player, rod, false, true);
				setNewSeed(rod, world);
			}	
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if(ItemNBTHelper.getBoolean(stack, TAG_HAS_TARGET, false) && playerIn.isSneaking()) {
			setHasTarget(playerIn, stack, false, true);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(worldIn.isRemote || !(entityIn instanceof EntityPlayer) || entityIn.posY > 26)
			return;

		if(ItemNBTHelper.getLong(stack, TAG_RNG_SEED, 0) == 0)
			setNewSeed(stack, worldIn);
		EntityPlayer player = (EntityPlayer) entityIn;
		
		if(player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack) {
			if(ItemNBTHelper.getBoolean(stack, TAG_HAS_TARGET, false)) {
				BlockPos pos = getPosition(stack);
				if(!isStone(worldIn, pos))
					setHasTarget(player, stack, false, true);
				else
					((WorldServer) worldIn).spawnParticle(EnumParticleTypes.REDSTONE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2, 0.4, 0.4, 0.4, 0);
			} else if(worldIn.getTotalWorldTime() % 10 == 0)
				locateNextTarget(stack, worldIn, entityIn);
		}
	}

	protected BlockPos getPosition(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_TARGET_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_TARGET_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_TARGET_Z, 0);
		return new BlockPos(x, y, z);
	}

	protected void setPosition(ItemStack stack, Entity player, BlockPos pos) {
		ItemNBTHelper.setInt(stack, TAG_TARGET_X, pos.getX());
		ItemNBTHelper.setInt(stack, TAG_TARGET_Y, pos.getY());
		ItemNBTHelper.setInt(stack, TAG_TARGET_Z, pos.getZ());
		setHasTarget(player, stack, true, true);
	}

	protected void locateNextTarget(ItemStack stack, World world, Entity viewer) {
		BlockPos center = viewer.getPosition();
		long seed = ItemNBTHelper.getLong(stack, TAG_RNG_SEED, 0);
		Random rand = new Random(seed ^ center.hashCode());
		int range = 6;
		int tries = 4;

		for(int i = 0; i < tries; i++) {
			BlockPos pos = center.add(rand.nextInt(range * 2 + 1) - range, rand.nextInt(range * 2 + 1) - range, rand.nextInt(range * 2 + 1) - range);
			if(isStone(world, pos) && canSee(viewer, pos))
				setPosition(stack, viewer, pos);
		}
	}
	
	protected void setHasTarget(Entity player, ItemStack stack, boolean target, boolean sfx) {
		ItemNBTHelper.setBoolean(stack, TAG_HAS_TARGET, target);
		
		if(player != null && sfx)
			player.world.playSound(null, player.getPosition(), (target ? AlquimiaSounds.divining_rod_enable : AlquimiaSounds.divining_rod_disable), SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	protected void setNewSeed(ItemStack stack, World world) {
		long currSeed = ItemNBTHelper.getLong(stack, TAG_RNG_SEED, 0);
		long newSeed = world.getSeed() ^ world.getTotalWorldTime() ^ currSeed;
		ItemNBTHelper.setLong(stack, TAG_RNG_SEED, newSeed);
	}

	protected boolean isStone(World world, BlockPos pos) {
		return isStone(world.getBlockState(pos));
	}

	protected boolean isStone(IBlockState state) {
		if(state.getBlock() == Blocks.STONE) {
			BlockStone.EnumType type = state.getValue(BlockStone.VARIANT);
			return type.isNatural();
		}

		return false;
	}


	protected boolean canSee(Entity entity, BlockPos pos) {
		RayTraceResult res = entity.world.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ), new Vec3d(pos), false, true, true); 
		return res != null && res.getBlockPos().equals(pos);
	}

}
