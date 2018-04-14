package vazkii.alquimia.common.ritual.medium;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.common.base.AlquimiaSounds;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.ritual.Ritual;
import vazkii.alquimia.common.ritual.RitualType;
import vazkii.arl.util.RenderHelper;

public class RitualClearSkies extends Ritual {

	private static Collection<Object> INGREDIENTS = Arrays.asList(new Object[] {ModItems.cinnabar, ModItems.cinnabar, Items.BLAZE_POWDER, Items.BLAZE_POWDER, Blocks.DOUBLE_PLANT, Blocks.DOUBLE_PLANT});
	
	public RitualClearSkies() {
		super("clear_skies", RitualType.MEDIUM, INGREDIENTS);
	}
	
	@Override
	public boolean canRun(World world, BlockPos center) {
		return world.isRaining();
	}
	
	@Override
	public boolean run(World world, BlockPos pos, NBTTagCompound cmp) {
		world.playSound(null, pos, AlquimiaSounds.ritual_clear_skies, SoundCategory.BLOCKS, 1F, 1F);
		return false;
	}
	
	@Override
	public boolean tick(World world, BlockPos pos, int time, NBTTagCompound cmp) {
		if(time == 90)
			world.getWorldInfo().setRaining(false);
		
		float height = Math.min(2F, time * 0.01F);
		double x = pos.getX() + 0.5;
		double y = pos.getY() + height - 0.2;
		double z = pos.getZ() + 0.5;
		
		if(time > 0 && time <= 150 && time % 30 == 0 && world instanceof WorldServer) {
			((WorldServer) world).spawnParticle(EnumParticleTypes.FLAME, x, y, z, 20, 0.2, 0.2, 0.2, 0.1F * height);
			world.playSound(null, x, y, z, AlquimiaSounds.heartbeat, SoundCategory.BLOCKS, 1F, world.rand.nextFloat() * 0.3F + 0.8F);
		}
		
		if(time == 190 && !world.isRemote)
			world.playSound(null, x, y, z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.2F, 1F);
		
		return time > 200;
	}
	
	@Override
	public void consumeItem(TileEntity tile, ItemStack stack) {
		super.consumeItem(tile, stack);
		
		if(stack.getItem() == Items.BLAZE_POWDER) {
			WorldServer world = (WorldServer) tile.getWorld(); 
			BlockPos pos = tile.getPos();
			world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 80, 0.3, 0.8, 0.3, 0.02F);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void render(World world, BlockPos pos, int time, float pticks) {
		super.render(world, pos, time, pticks);
		
		float size = Math.min(0.25F, ((float) time + pticks) * 0.0025F);
		if(time > 190)
			size = Math.max(0, size - (time - 190) * 0.05F);
		float height = Math.min(2F, ((float) time + pticks) * 0.01F);
		GlStateManager.translate(0, height, 0);
		RenderHelper.renderStar(0xff7E00, size, 89483L);
	}

}
