package kekztech;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemDistributionNetworkController implements Comparable<ItemDistributionNetworkController> {
	
	private static final HashSet<ItemDistributionNetworkController> instances = new HashSet<>();
	
	private final UUID uuid = UUID.randomUUID();
	private final LinkedList<IConduit> conduits = new LinkedList<>();
	private final boolean[] slotDirections = new boolean[15];
	private final ItemStack[] networkedItemStacks = new ItemStack[15];
	//private final HashMap<String, IConduit> sources = new HashMap<>(); // k = resource name, v = source conduit
	
	public ItemDistributionNetworkController() {
		
	}
	
	/**
	 * Places a new conduit as it's own network. Merges with adjacent networks automatically.
	 *  
	 * @param conduit
	 * 			The conduit to be placed
	 */
	public static void placeConduit(IConduit conduit) {
		
		conduit.setNetwork(new ItemDistributionNetworkController());
		conduit.getNetwork().addConduit(conduit);
		/*
		final TileEntity te = (TileEntity) conduit;
		final int x = te.xCoord;
		final int y = te.yCoord;
		final int z = te.zCoord;
		
		// Search for adjacent Networks on all six sides
		final HashSet<ItemDistributionNetworkController> networks = new HashSet<>();
		final World world = te.getWorldObj();
		final TileEntity te1x = world.getTileEntity(x + 1, y, z);
		final TileEntity te0x = world.getTileEntity(x - 1, y, z);
		final TileEntity te1y = world.getTileEntity(x, y + 1, z);
		final TileEntity te0y = world.getTileEntity(x, y - 1, z);
		final TileEntity te1z = world.getTileEntity(x, y, z + 1);
		final TileEntity te0z = world.getTileEntity(x, y, z - 1);
		if(te1x != null && te1x instanceof IConduit) {
			final IConduit c = (IConduit) te1x;
			networks.add(c.getNetwork());
		}
		if(te0x != null && te0x instanceof IConduit) {
			final IConduit c = (IConduit) te0x;
			networks.add(c.getNetwork());
		}
		if(te1y != null && te1y instanceof IConduit) {
			final IConduit c = (IConduit) te1y;
			networks.add(c.getNetwork());
		}
		if(te0y != null && te0y instanceof IConduit) {
			final IConduit c = (IConduit) te0y;
			networks.add(c.getNetwork());
		}
		if(te1z != null && te1z instanceof IConduit) {
			final IConduit c = (IConduit) te1z;
			networks.add(c.getNetwork());
		}
		if(te0z != null && te0z instanceof IConduit) {
			final IConduit c = (IConduit) te0z;
			networks.add(c.getNetwork());
		}
		// Return prematurely if no adjacent network was found
		if(networks.size() == 0) {
			return;
		}
		// Sort networks by descending size
		final LinkedList<ItemDistributionNetworkController> networkList = new LinkedList<>();
		networkList.addAll(networks);
		Collections.sort(networkList, Collections.reverseOrder());
		// Larger networks consume smaller networks to reduce copying around data
		while(networkList.size() > 1) {
			final ItemDistributionNetworkController l = networkList.get(networkList.size() - 2);
			final ItemDistributionNetworkController r = networkList.getLast();
			l.appendNetwork(r);
			networkList.removeLast();
		}*/
		
	}
	
	/**
	 * Deletes this conduit and breaks up adjacent networks into separate networks.
	 * 
	 * @param conduit
	 * 			Conduit to be removed.
	 */
	public static void destroyConduit(IConduit conduit) {
		
	}
	
	public void run() {
		
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof ItemDistributionNetworkController) {
			final ItemDistributionNetworkController network = (ItemDistributionNetworkController) o;
			return uuid.equals(network.getUUID());
		} else {
			return false;
		}
	}
	
	@Override
	public int compareTo(ItemDistributionNetworkController o) {
		return (int) Math.signum(o.getSize() - this.getSize());
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public int getSize() {
		return conduits.size();
	}
	
	/**
	 * Find out if this slot is designated as input and can accept an item into the network.
	 * 
	 * Slots are numbered left to right, then top to bottom and start at zero.
	 * For example, the second slot in the second row, would have a slot number of five.
	 * 
	 * @param slot:
	 * 			Slot number.
	 * @return whether this slot can accept input or not.
	 */
	public boolean isInputSlot(int slot) {
		if(slot >= slotDirections.length) {
			return false;
		}
		return slotDirections[slot];
	}
	
	/**
	 * Return the ItemStack that is contained in the given slot number.
	 * 
	 * Slots are numbered left to right, then top to bottom and start at zero.
	 * For example, the second slot in the second row, would have a slot number of five.
	 * 
	 * @param slot:
	 * 			Slot number.
	 * @return ItemStack at given slot number.
	 */
	public ItemStack getStackInSlot(int slot) {
		if(slot >= networkedItemStacks.length) {
			return null;
		}
		return networkedItemStacks[slot];
	}
	
	public void setStackInSlot(int slot, ItemStack itemStack) {
		if(slot < networkedItemStacks.length && isInputSlot(slot)) {
			networkedItemStacks[slot] = itemStack;
		}
	}
	
	/**
	 * Register a new conduit to this network.
	 * 
	 * @param conduit
	 * 			New conduit.
	 */
	private void addConduit(IConduit conduit) {
		conduits.add(conduit);
		conduit.setNetwork(this);
	}
	
	/**
	 * Merge another network with this one if they have been connected by a new conduit.
	 * 
	 * @param network
	 * 			Network to merge with this one.
	 */
	private void appendNetwork(ItemDistributionNetworkController network) {
		for(IConduit conduit : network.conduits) {
			this.addConduit(conduit);
		}
	}
	
}