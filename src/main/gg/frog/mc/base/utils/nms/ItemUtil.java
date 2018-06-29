package gg.frog.mc.base.utils.nms;

import java.lang.reflect.Method;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import gg.frog.mc.base.PluginMain;
import gg.frog.mc.base.config.PluginCfg;
import gg.frog.mc.base.utils.StrUtil;

public class ItemUtil {

	private static Class<?> nbtBaseClass;
	private static Class<?> nbtTagCompoundClass;
	private static Class<?> nbtTagStringClass;
	private static Class<?> nbtTagIntClass;
	// private static Class<?> nbtTagShortClass;
	// private static Class<?> nbtTagListClass;
	private static Class<?> itemstackClass;
	private static Method asNmsCopyMethod;
	private static Method asCraftMirrorMethod;
	private static Method hasTagMethod;
	private static Method getTagMethod;
	private static Method setTagMethod;
	private static Method nbtSetMethod;
	// private static Method nbtListAddSetMethod;
	private static boolean setupOk;

	static {
		try {
			try {
				itemstackClass = NMSUtil.getNmsClass("ItemStack");
				nbtBaseClass = NMSUtil.getNmsClass("NBTBase");
				nbtTagStringClass = NMSUtil.getNmsClass("NBTTagString");
				nbtTagIntClass = NMSUtil.getNmsClass("NBTTagInt");
				nbtTagCompoundClass = NMSUtil.getNmsClass("NBTTagCompound");

				hasTagMethod = itemstackClass.getMethod("hasTag", new Class[0]);
				getTagMethod = itemstackClass.getMethod("getTag", new Class[0]);
				setTagMethod = itemstackClass.getMethod("setTag", new Class[] { nbtTagCompoundClass });
				nbtSetMethod = nbtTagCompoundClass.getMethod("set", new Class[] { String.class, nbtBaseClass });
				// nbtTagShortClass = NMSUtil.getNmsClass("NBTTagShort");
				// nbtTagListClass = NMSUtil.getNmsClass("NBTTagList");
				// nbtListAddSetMethod = nbtTagListClass.getMethod("add", new Class[]
				// {nbtBaseClass});
			} catch (Exception e) {
				if (NMSUtil.getServerVersion().startsWith("v1_7_R4")) {
					itemstackClass = NMSUtil.getNmClass("item.ItemStack");// add
					nbtBaseClass = NMSUtil.getNmClass("nbt.NBTBase");// dy
					nbtTagStringClass = NMSUtil.getNmClass("nbt.NBTTagString");// dx
					nbtTagIntClass = NMSUtil.getNmClass("nbt.NBTTagInt");// dp
					nbtTagCompoundClass = NMSUtil.getNmClass("nbt.NBTTagCompound");// dh

					hasTagMethod = itemstackClass.getMethod("func_77942_o", new Class[0]);// hasTagCompound
					getTagMethod = itemstackClass.getMethod("func_77978_p", new Class[0]);// getTagCompound
					setTagMethod = itemstackClass.getMethod("func_77982_d", new Class[] { nbtTagCompoundClass });// setTagCompound
					nbtSetMethod = nbtTagCompoundClass.getMethod("func_74782_a", new Class[] { String.class, nbtBaseClass });// setTag
				} else {
					throw new Exception("Nbt edit is not support.");
				}
			}
			asNmsCopyMethod = NMSUtil.getObcClass("inventory.CraftItemStack").getMethod("asNMSCopy", new Class[] { ItemStack.class });
			asCraftMirrorMethod = NMSUtil.getObcClass("inventory.CraftItemStack").getMethod("asCraftMirror", new Class[] { itemstackClass });
			setupOk = true;
		} catch (Exception e) {
			e.printStackTrace();
			PluginMain.LOG.warning(StrUtil.messageFormat(PluginCfg.PLUGIN_PREFIX + "ItemUtil setup fail. Some functions are unavailable."));
		}
	}

	public static ItemStack addEnchantLight(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 0);
		if (!NMSUtil.getServerVersion().startsWith("v1_7_") && setupOk) {
			try {
				Object nmsItemstack = asNmsCopyMethod.invoke(null, new Object[] { item });
				if (nmsItemstack == null) {
					return item;
				}
				Object nbtCompound = null;
				if (((Boolean) hasTagMethod.invoke(nmsItemstack, new Object[0])).booleanValue()) {
					nbtCompound = getTagMethod.invoke(nmsItemstack, new Object[0]);
				} else {
					nbtCompound = nbtTagCompoundClass.newInstance();
					setTagMethod.invoke(nmsItemstack, new Object[] { nbtCompound });
				}
				if (nbtCompound == null) {
					return item;
				}
				Object nbtHideFlags = nbtTagIntClass.getConstructor(int.class).newInstance(1);
				nbtSetMethod.invoke(nbtCompound, new Object[] { "HideFlags", nbtHideFlags });
				return (ItemStack) asCraftMirrorMethod.invoke(null, new Object[] { nmsItemstack });
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return item;
	}

	public static ItemStack addSkullOwner(ItemStack item, String name) {
		if (setupOk) {
			try {
				Object nmsItemstack = asNmsCopyMethod.invoke(null, new Object[] { item });
				if (nmsItemstack == null) {
					return item;
				}
				Object nbtCompound = null;
				if (((Boolean) hasTagMethod.invoke(nmsItemstack, new Object[0])).booleanValue()) {
					nbtCompound = getTagMethod.invoke(nmsItemstack, new Object[0]);
				} else {
					nbtCompound = nbtTagCompoundClass.newInstance();
					setTagMethod.invoke(nmsItemstack, new Object[] { nbtCompound });
				}
				if (nbtCompound == null) {
					return item;
				}
				Object nbtString = nbtTagStringClass.getConstructor(String.class).newInstance(name);
				nbtSetMethod.invoke(nbtCompound, new Object[] { "SkullOwner", nbtString });
				return (ItemStack) asCraftMirrorMethod.invoke(null, new Object[] { nmsItemstack });
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return item;
	}
}
