package vazkii.alquimia.common.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.client.gui.GuiAdvancementsExt;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.AlquimiaSounds;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.alquimia.common.lib.LibGuiIDs;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.item.ItemMod;

public class ItemLexicon extends ItemMod implements IAlquimiaItem {

	private static final String[] ORDINAL_SUFFIXES = new String[]{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	
	public ItemLexicon() {
		super("lexicon");
		setMaxStackSize(1);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
		
//		addPropertyOverride(new ResourceLocation("open"), new IItemPropertyGetter() {
//			
//            @SideOnly(Side.CLIENT)
//            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
//            	Minecraft mc = Minecraft.getMinecraft();
//            	boolean valid = entityIn == mc.player 
//            			&& (stack == mc.player.getHeldItemMainhand() || stack == mc.player.getHeldItemOffhand()) 
//            			&& (mc.currentScreen instanceof GuiLexicon || mc.currentScreen instanceof GuiAdvancementsExt);
//            	return valid ? 1 : 0;
//            }
//            
//        });
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote)
			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, AlquimiaSounds.book_open, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
		playerIn.openGui(Alquimia.instance, LibGuiIDs.LEXICON, worldIn, 0, 0, 0);
		
		return succeed(playerIn, handIn);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(getEdition());
	}
	
	@SideOnly(Side.CLIENT)
	public static String getEdition() {
		String version = LibMisc.BUILD;
		int build = version.contains("GRADLE") ? 0 : Integer.parseInt(version);
		String editionStr = build == 0 ? I18n.translateToLocal("alqmisc.dev_edition") : numberToOrdinal(build); 
		return I18n.translateToLocalFormatted("alqmisc.edition", editionStr);
	}
	
	private static String numberToOrdinal(int i) {
		return i % 100 == 11 || i % 100 == 12 || i % 100 == 13 ? i + "th" : i + ORDINAL_SUFFIXES[i % 10];
	}

}
