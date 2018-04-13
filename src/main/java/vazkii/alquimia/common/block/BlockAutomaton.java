package vazkii.alquimia.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaBlock;
import vazkii.alquimia.common.block.interf.IAutomatonHead;
import vazkii.alquimia.common.block.tile.TileAutomaton;
import vazkii.alquimia.common.lib.LibGuiIDs;
import vazkii.arl.block.BlockModContainer;

public class BlockAutomaton extends BlockModContainer implements IAlquimiaBlock {

	public static final AxisAlignedBB AABB = new AxisAlignedBB(0F, 0F, 0F, 1F, 12F / 16F, 1F);
	public static final IProperty<Boolean> REDSTONE = PropertyBool.create("redstone");
	
	public BlockAutomaton() {
		super("automaton", Material.IRON);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
		
		setDefaultState(getDefaultState().withProperty(REDSTONE, false));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		playerIn.openGui(Alquimia.instance, LibGuiIDs.AUTOMATON, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean isPowered = worldIn.isBlockPowered(pos);
        boolean wasPowered = state.getValue(REDSTONE);

        if(isPowered != wasPowered)
            worldIn.setBlockState(pos, state.withProperty(REDSTONE, isPowered), 2 | 4);
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileAutomaton();
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if(tileentity instanceof TileAutomaton) {
			TileAutomaton automaton = (TileAutomaton) tileentity;
			automaton.runInHead(IAutomatonHead::onRemoved);
			InventoryHelper.dropInventoryItems(worldIn, pos, automaton);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(REDSTONE, (meta & 1) == 0 ? false : true);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(REDSTONE) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { REDSTONE });
	}
	
	@Override
	public IProperty[] getIgnoredProperties() {
		return new IProperty[] { REDSTONE };
	}
	
}
