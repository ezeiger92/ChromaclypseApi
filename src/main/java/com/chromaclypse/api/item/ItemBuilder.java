package com.chromaclypse.api.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.chromaclypse.api.annotation.Mutable;
import com.chromaclypse.api.json.JsonBlob;
import com.chromaclypse.api.messages.Text;

/**
 * This class provides a builder for ItemStacks. It is exactly what you expect,
 * with a few special functions listed below and some static utilities.
 * 
 * @see ItemBuilder#selector
 * @see ItemBuilder#wrapLore
 * @see ItemBuilder#wrapText
 * 
 * @author ezeiger92
 */
public class ItemBuilder implements Cloneable {

	/**
	 * Null pointer safe tool for cloning ItemStacks
	 * 
	 * @param source ItemStack to clone
	 * @return Clone of source, or <tt>null</tt> if source is <tt>null</tt>
	 */
	public static ItemStack clone(ItemStack source) {
		return (source != null) ? source.clone() : null;
	}
	
	/**
	 * Item comparison with (optional) wildcard matching for metadata. If either
	 * stack has the lore consisting of a single asterisk ("*"), all metadata
	 * will be discarded before a comparison is made. If either stack is null,
	 * a null pointer safe comparison is made.
	 * 
	 * @param left One of the ItemStacks
	 * @param right The other ItemStack
	 * @return Whether or not the items are considered identical
	 */
	public static boolean compareItems(ItemStack left, ItemStack right) {
		if(left == null || right == null)
			return left == right;
		
		if(left.getType() != right.getType())
			return false;
		
		boolean hasMetaLeft = left.hasItemMeta();
		ItemMeta metaLeft = hasMetaLeft ? left.getItemMeta() : null;
		ItemMeta metaRight = right.hasItemMeta() ? right.getItemMeta() : null;

		if(!isWildcard(metaLeft) && !isWildcard(metaRight))
			return (hasMetaLeft == (metaRight != null)) &&
			(!hasMetaLeft || Bukkit.getItemFactory().equals(metaLeft, metaRight));

		return true;
	}
	
	private static boolean isWildcard(ItemMeta meta) {
		return meta != null && meta.hasLore() && meta.getLore().get(0).equals("*");
	}
	
	/**
	 * Constructs an ItemBuilder by consuming an exiting ItemStack. Any
	 * modifications to the builder directly affect the ItemStack.
	 * 
	 * @param stack The ItemStack to edit
	 * @return A new ItemBuilder that modifies <tt>stack</tt>
	 */
	public static @Mutable ItemBuilder edit(@Mutable("Stored and modified by other functions") ItemStack stack) {
		
		ItemBuilder res = new ItemBuilder();
		res.resultStack = stack;

		if(stack.getType() == Material.AIR)
			res.type(Material.BARRIER);
		
		return res;
	}
	
	private ItemStack resultStack;
	
	private ItemBuilder() {
	}
	
	/**
     * Constructs an ItemBuilder.
     *
     * @param stack Base item to work with
     */
	public ItemBuilder(ItemStack stack) {
		resultStack = stack.clone();
		
		if(stack.getType() == Material.AIR)
			type(Material.BARRIER);
	}
	
	/**
     * Constructs an ItemBuilder.
     *
     * @param type Material of item
     */
	public ItemBuilder(Material type) {
		resultStack = new ItemStack(type);

		if(type == Material.AIR)
			type(Material.BARRIER);
	}
	
	/**
     * Constructs an ItemBuilder.
     *
     * @param type Material of item
     * @param amount Amount of material
     */
	public ItemBuilder(Material type, int amount) {
		this(type);
		amount(amount);
	}
	
	/**
     * Constructs an ItemBuilder.
     *
     * @param type Material of item
     * @param amount Amount of material
     * @param durability Stack durability
     */
	public ItemBuilder(Material type, int amount, short durability) {
		this(type, amount);
		durability(durability);
	}
	
	/**
     * Constructs an ItemBuilder. Takes an int for durability to "play nice"
     * with integral types, but will truncate to a short internally.
     *
     * @param type Material of item
     * @param amount Amount of material
     * @param durability Stack durability as an int
     */
	public ItemBuilder(Material type, int amount, int durability) {
		this(type, amount, (short)durability);
	}
	
	/**
     * Returns a reference to our ItemStack so our builder can tweak later
     *
     * @return stack
     */
	public @Mutable("ItemBuilder holds a reference") ItemStack get() {
		return resultStack;
	}
	
	/**
     * Returns a new ItemStack based on our stack
     *
     * @return stack
     */
	public ItemStack getNew() {
		return resultStack.clone();
	}
	
	/**
     * Adds an enchantment
     *
     * @param ench Enchantment type
     * @param level Level of enchantment
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder enchant(Enchantment ench, int level) {
		resultStack.addEnchantment(ench, level);
		return this;
	}
	
	/**
     * Adds an unsafe enchantment
     *
     * @param ench Enchantment type
     * @param level Level of enchantment
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder forceEnchant(Enchantment ench, int level) {
		resultStack.addUnsafeEnchantment(ench, level);
		return this;
	}
	
	/**
     * Removes an enchantment
     *
     * @param ench Enchantment type
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder disenchant(Enchantment ench) {
		resultStack.removeEnchantment(ench);
		return this;
	}
	
	/**
     * Sets stack amount
     *
     * @param amount Target size of stack
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder amount(int amount) {
		resultStack.setAmount(amount);
		return this;
	}
	
	/**
     * Sets stack durability
     *
     * @param durability Target durability for stack
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder durability(short durability) {

		if(resultStack.getItemMeta() instanceof Damageable) {
			ItemMeta meta = resultStack.getItemMeta();
			((Damageable)meta).setDamage(durability);

			resultStack.setItemMeta(meta);
		}
		
		return this;
	}
	
	public @Mutable ItemBuilder durability(int durability) {
		return durability((short)durability);
	}
	
	/**
     * Sets stack material
     *
     * @param type Material
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder type(Material type) {
		resultStack.setType(type);
		return this;
	}
	
	/**
	 * Sets the skull type to a players head, given that the current material
	 * accepts skull types. <tt>playerName</tt> must not be null. If you want a
	 * plain player skull, use
	 * {@link ItemBuilder#skull(SkullType) skull(SkullType.PLAYER)}.
	 * 
	 * @param playerName The player whose face will be displayed on the head
	 * @return this, for chaining
	 */
	public @Mutable ItemBuilder skull(UUID playerUUID) {
		return skull(Bukkit.getOfflinePlayer(playerUUID));
	}
	
	public @Mutable ItemBuilder skull(OfflinePlayer player) {
		type(Material.PLAYER_HEAD);
		
		if(resultStack.getItemMeta() instanceof SkullMeta) {
			SkullMeta smHolder = (SkullMeta)resultStack.getItemMeta();
			smHolder.setOwningPlayer(player);
			resultStack.setItemMeta(smHolder);
		}
		
		return this;
	}
	
	/**
     * Sets leather armor color
     *
     * @param color Color of leather armor
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder leather(Color color) {
		if(resultStack.getItemMeta() instanceof LeatherArmorMeta) {
			LeatherArmorMeta meta = (LeatherArmorMeta)resultStack.getItemMeta();
			meta.setColor(color);
			resultStack.setItemMeta(meta);
		}
		return this;
	}

	/**
	 * Sets a series of flags on the ItemStack, affecting the information it
	 * displays.
	 * 
	 * @param flags A series of ItemFlags
	 * @return this, for chaining
	 */
	public @Mutable ItemBuilder flag(ItemFlag... flags) {
		ItemMeta stackMeta = resultStack.getItemMeta();
		stackMeta.addItemFlags(flags);
		resultStack.setItemMeta(stackMeta);
		return this;
	}
	
	/**
	 * Removes a series of flags from the ItemStack, affecting the information it
	 * displays.
	 * 
	 * @param flags A series of ItemFlags
	 * @return this, for chaining
	 */
	public @Mutable ItemBuilder unflag(ItemFlag... flags) {
		ItemMeta stackMeta = resultStack.getItemMeta();
		stackMeta.removeItemFlags(flags);
		resultStack.setItemMeta(stackMeta);
		return this;
	}
	
	/**
	 * Sets all existing flags on the ItemStack.
	 * 
	 * @see ItemBuilder#flag
	 * 
	 * @return this, for chaining
	 */
	public @Mutable ItemBuilder flagAll() {
		return flag(ItemFlag.values());
	}
	
	/**
	 * Removes all flags from the ItemStack.
	 * 
	 * @see ItemBuilder#unflag
	 * 
	 * @return this, for chaining
	 */
	public @Mutable ItemBuilder unflagAll() {
		return unflag(ItemFlag.values());
	}
	
	/**
	 * Sets the display name for the ItemStack.
	 * 
	 * @param displayName The new name
	 * @return this, for chaining
	 */
	public @Mutable ItemBuilder display(String displayName) {
		ItemMeta stackMeta = resultStack.getItemMeta();
		stackMeta.setDisplayName(Text.format().colorize(displayName));
		resultStack.setItemMeta(stackMeta);
		return this;
	}
	
	/**
     * Sets item lore, applying colors in the process
     *
     * @param lore The text to set
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder lore(String... lore) {
		return directLore(Arrays.asList(Text.format().colorizeList(lore)));
	}
	
	/**
     * Sets item lore without colors, in case they were processed before
     *
     * @param lore The text to set
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder directLore(List<String> lore) {
		ItemMeta stackMeta = resultStack.getItemMeta();
		stackMeta.setLore(lore);
		resultStack.setItemMeta(stackMeta);
		return this;
	}
	
	/**
     * Creates a wrapping text field across the display name and lore
     * Null elements will be ignored
     *
     * @param text The text to set
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder wrapText(int length, String... text) {
		text[0] = "&f&o" + text[0];
		List<String> lines = Text.format().wrap(length, Text.format().colorizeList(text));
		
		ItemMeta stackMeta = resultStack.getItemMeta();
		if(lines.size() > 0) {
			stackMeta.setDisplayName(lines.get(0));
			lines.remove(0);
		}
		stackMeta.setLore(lines);
		resultStack.setItemMeta(stackMeta);
		
		return this;
	}
	
	public @Mutable ItemBuilder wrapText(String... text) {
		return wrapText(32, text);
	}
	
	/**
     * Creates a wrapping text field across lore only
     * Null elements will be ignored
     *
     * @param lore The text to set
     * 
     * @return this, for chaining
     */
	public @Mutable ItemBuilder wrapLore(int length, String... lore) {
		lore[0] = "&f&o" + lore[0];
		return directLore(Text.format().wrap(length, Text.format().colorizeList(lore)));
	}
	
	public @Mutable ItemBuilder wrapLore(String... text) {
		return wrapLore(32, text);
	}
	
	/**
	 * Creates a list selector within item lore. All options will be printed in order with default
	 * formatting, and the selected index will be highlighted with special formatting.
	 * 
	 * @param index The index to highlight, defaults to 0 if outside a valid range
	 * @param options Array of options to select, must have at least 1 element to function
	 * @return this, for chaining
	 */
	public @Mutable ItemBuilder selector(int index, String... options) {
		if(options.length == 0)
			return this;
		
		if(index < 0 || index >= options.length)
			index = 0;
		
		ArrayList<String> result = new ArrayList<>(options.length + 1);
		result.add("");
		
		for(int i = 0; i < options.length; ++i)
			result.add(Text.format().colorize(" &7" + options[i]));
		
		result.set(index + 1, Text.format().colorize("&2>" + options[index]));
		directLore(result);
		return this;
	}
	
	private static final String stringOf(JsonBlob blob) {
		if(blob != null) {
			return blob.toString().replace("\\", "\\\\").replace("\"", "\\\"");
		}
		
		return "";
	}
	
	@SuppressWarnings("deprecation")
	public @Mutable ItemBuilder pages(JsonBlob page, JsonBlob... extra) {
		
		if(!(resultStack.getItemMeta() instanceof BookMeta)) {
			type(Material.WRITTEN_BOOK);
		}
		
		int length = extra.length;
		StringBuilder pages = new StringBuilder("{pages:[\"").append(stringOf(page)).append('"');

		for (int i = 0; i < length; ++i) {
			pages.append(",\"").append(stringOf(extra[i])).append('"');
		}

		pages.append("]}");
		
		Bukkit.getUnsafe().modifyItemStack(resultStack, pages.toString());
		
		return this;
	}

	/**
	 * Clones the current ItemBuilder, copying the current state of its
	 * ItemStack. Neither builder can access the others ItemStack.
	 * 
	 * @return A new ItemBuilder
	 */
	@Override
	public ItemBuilder clone() {
		return new ItemBuilder(resultStack);
	}
}
