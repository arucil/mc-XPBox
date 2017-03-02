package xpbox;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class XPCapability {
   public int value;

   public static class Storage implements Capability.IStorage<XPCapability> {
      @Override
      public void readNBT(Capability<XPCapability> capability, XPCapability instance, EnumFacing side, NBTBase nbt) {
         instance.value = ((NBTTagInt) nbt).getInt();
      }

      @Override
      public NBTBase writeNBT(Capability<XPCapability> capability, XPCapability instance, EnumFacing side) {
         return new NBTTagInt(instance.value);
      }
   }

   public static class Provider implements ICapabilitySerializable<NBTTagInt> {

      private XPCapability cap = new XPCapability();

      @Override
      public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
         return capability == XPBox.XP_CAPABILITY;
      }

      @Override
      public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
         if (capability == XPBox.XP_CAPABILITY)
            return (T) cap;
         return null;
      }

      @Override
      public NBTTagInt serializeNBT() {
         return new NBTTagInt(cap.value);
      }

      @Override
      public void deserializeNBT(NBTTagInt nbt) {
         cap.value = nbt.getInt();
      }
   }
}
