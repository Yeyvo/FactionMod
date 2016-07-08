package fr.mff.facmod.key;

import fr.mff.facmod.FactionMod;
import fr.mff.facmod.network.PacketWandActivate;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class KeyEvents
{
  public KeyBinding keyBinding = new KeyBinding("bbw.key.mode", 50, "bbw.key.category");
  public KeyBinding keyBindingFluid = new KeyBinding("bbw.key.fluidmode", 33, "bbw.key.category");
  private boolean isPressed;
  private boolean isPressedFluid;
  
  public KeyEvents()
  {
    ClientRegistry.registerKeyBinding(this.keyBinding);
    ClientRegistry.registerKeyBinding(this.keyBindingFluid);
    MinecraftForge.EVENT_BUS.register(this);
    this.isPressed = false;
    this.isPressedFluid = false;
  }
  
  @SubscribeEvent
  public void KeyEvent(InputEvent event)
  {
    boolean currentIsPressed = this.keyBinding.isPressed();
    boolean currentFluidIsPressed = this.keyBindingFluid.isPressed();
    if ((currentIsPressed != this.isPressed) || (currentFluidIsPressed != this.isPressedFluid))
    {
      this.isPressed = currentIsPressed;
      PacketWandActivate packet = new PacketWandActivate(currentIsPressed, currentFluidIsPressed);
      FactionMod.INSTANCE.networkWrapper.sendToServer(packet);
    }
  }
}
