package fr.mff.facmod.handlers;

import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import fr.mff.addons.permission.PermissionApi;
import fr.mff.facmod.core.Homes;
import fr.mff.facmod.core.Lands;
import fr.mff.facmod.network.PacketHelper;
import fr.mff.facmod.perm.Group;
import fr.mff.facmod.perm.PermissionManager;

public class CommonEventHandler {

	@SubscribeEvent
	public void onEntityEnteringChunk(TickEvent.PlayerTickEvent event) {
		Lands.onPlayerTick(event);
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		Homes.onPlayerTick(event);
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		Homes.onLivingHurt(event);
		Lands.onLivingHurt(event);
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		PacketHelper.updateClientFaction(event.player.getUniqueID());
		PacketHelper.updateClientRank(event.player.getUniqueID());
		event.player.refreshDisplayName();
	}

	@SubscribeEvent
	public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		Lands.onPlayerLeave(event);
	}

	@SubscribeEvent
	public void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
		if(!PermissionApi.manager().hasEntityPermission(event.getPlayer(), "world.block.break")) {
			event.setCanceled(true);
		}
		Lands.onPlayerBreakBlock(event);
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!PermissionApi.manager().hasEntityPermission(event.entityPlayer, "world.block.interact")) {
			event.setCanceled(true);
		}
		Lands.onPlayerInteract(event);
	}

	@SubscribeEvent
	public void onPlayerPlaceBlock(PlaceEvent event) {
		if(!PermissionApi.manager().hasEntityPermission(event.player, "world.block.place")) {
			event.setCanceled(true);
		}
		Lands.onPlayerPlaceBlock(event);
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		if(event.current.getItem().equals(Items.bucket)) {
			if(!PermissionApi.manager().hasEntityPermission(event.entityPlayer, "world.block.break")) {
				event.setCanceled(true);
			}
		} else {
			if(!PermissionApi.manager().hasEntityPermission(event.entityPlayer, "world.block.place")) {
				event.setCanceled(true);
			}
		}
		Lands.onBucketFill(event);
	}

	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event) {
		Lands.onAttackEntity(event);
	}

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		Lands.onLivingAttack(event);
	}

	@SubscribeEvent
	public void onExplosion(ExplosionEvent.Detonate event) {
		Lands.onExplosion(event);
	}

	@SubscribeEvent
	public void onCommand(CommandEvent event) {
		if(!event.sender.getEntityWorld().isRemote) {
			if(event.sender.getCommandSenderEntity() != null && !PermissionManager.canEntityExecuteCommand(event.sender.getCommandSenderEntity(), event.command)) {
				event.setCanceled(true);
				event.setResult(Result.DENY);
			}
		}
	}
	
	@SubscribeEvent
	public void onNameFormat(NameFormat event) {
		Group g = PermissionManager.getPlayerGroup(event.entityPlayer.getUniqueID());
		if(g != null) {
			event.displayname = g.getDisplay() + " " + EnumChatFormatting.RESET + event.displayname;
		}
	}
}
