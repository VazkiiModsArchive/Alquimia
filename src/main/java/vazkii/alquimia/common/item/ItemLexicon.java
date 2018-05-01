package vazkii.alquimia.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.base.AlquimiaConfig;
import vazkii.alquimia.common.base.AlquimiaSounds;
import vazkii.alquimia.common.lib.LibGuiIDs;
import vazkii.alquimia.common.lib.LibMisc;

public class ItemLexicon extends ItemAlquimia {

	private static final String[] ORDINAL_SUFFIXES = new String[]{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };

	private static final String TAG_ACQUIRED_LEXICON_ONCE = LibMisc.PREFIX_MOD + "acquired_lexica";
	
	public ItemLexicon() {
		super("lexicon");
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
		setContainerItem(this);
	}
	
	@SubscribeEvent
	public void onEntityDrops(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if(entity instanceof EntityZombie && AlquimiaConfig.enableZombieDrop) {
			Entity attacker = event.getSource().getTrueSource();
			if(attacker instanceof EntityPlayer && !attacker.getEntityData().getBoolean(TAG_ACQUIRED_LEXICON_ONCE) && Math.random() < 0.25) {
				EntityItem newEntity = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, new ItemStack(this));
				event.getDrops().add(newEntity);
				attacker.getEntityData().setBoolean(TAG_ACQUIRED_LEXICON_ONCE, true);
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote)
			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, AlquimiaSounds.book_open, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
		playerIn.openGui(Alquimia.instance, LibGuiIDs.LEXICON, worldIn, 0, 0, 0);
		playerIn.getEntityData().setBoolean(TAG_ACQUIRED_LEXICON_ONCE, true);
		
		return succeed(playerIn, handIn);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
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
