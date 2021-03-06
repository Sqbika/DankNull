package p455w0rd.danknull.inventory.cap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import p455w0rd.danknull.api.IDankNullHandler;
import p455w0rd.danknull.init.ModGlobals;
import p455w0rd.danknull.inventory.DankNullHandler;
import p455w0rdslib.util.CapabilityUtils;

import java.util.Objects;

/**
 * @author BrockWS
 */
public class DankNullCapabilityProvider implements ICapabilityProvider {

	private final ItemStack stack;
	private final IDankNullHandler dankNullHandler;
	private boolean needsInitialNBT = false; // When a stack is copied the NBT isn't applied until after capabilities are initialized

	public DankNullCapabilityProvider(final ModGlobals.DankNullTier tier, final ItemStack stack) {
		this.stack = stack;
		dankNullHandler = new DankNullHandler(tier) {
			@Override
			protected void onContentsChanged(final int slot) {
				super.onContentsChanged(slot);
				NBTTagCompound oldNBT = stack.getTagCompound();
				NBTTagCompound newNBT = (NBTTagCompound) CapabilityDankNull.DANK_NULL_CAPABILITY.writeNBT(this, null);
				if (Objects.nonNull(oldNBT)) {
					if (Objects.nonNull(newNBT)) {
						oldNBT.merge(newNBT);
					}
				} else {
					oldNBT = newNBT;
				}
				stack.setTagCompound(oldNBT);
			}

			@Override
			protected void onSettingsChanged() {
				super.onSettingsChanged();
				NBTTagCompound oldNBT = stack.getTagCompound();
				NBTTagCompound newNBT = (NBTTagCompound) CapabilityDankNull.DANK_NULL_CAPABILITY.writeNBT(this, null);
				if (Objects.nonNull(oldNBT)) {
					if (Objects.nonNull(newNBT)) {
						oldNBT.merge(newNBT);
					}
				} else {
					oldNBT = newNBT;
				}
				stack.setTagCompound(oldNBT);
			}
		};
		if (stack.hasTagCompound()) {
			CapabilityDankNull.DANK_NULL_CAPABILITY.readNBT(dankNullHandler, null, stack.getTagCompound());
		}
		else {
			needsInitialNBT = true;
		}
	}

	@Override
	public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
		if (needsInitialNBT && stack.hasTagCompound()) {
			needsInitialNBT = false;
			CapabilityDankNull.DANK_NULL_CAPABILITY.readNBT(dankNullHandler, null, stack.getTagCompound());
		}
		return capability == CapabilityDankNull.DANK_NULL_CAPABILITY || CapabilityUtils.isItemHandler(capability);
	}

	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (hasCapability(capability, facing)) {
			if (CapabilityUtils.isItemHandler(capability)) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(dankNullHandler);
			}
			else if (capability == CapabilityDankNull.DANK_NULL_CAPABILITY) {
				return CapabilityDankNull.DANK_NULL_CAPABILITY.cast(dankNullHandler);
			}
		}
		return null;
	}

}
