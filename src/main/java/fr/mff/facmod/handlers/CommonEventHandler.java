package fr.mff.facmod.handlers;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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
import fr.mff.facmod.core.EnumResult;
import fr.mff.facmod.core.Faction;
import fr.mff.facmod.core.Homes;
import fr.mff.facmod.core.Lands;
import fr.mff.facmod.core.Powers;
import fr.mff.facmod.network.PacketHelper;
import fr.mff.facmod.perm.Group;
import fr.mff.facmod.perm.PermissionManager;

public class CommonEventHandler {

	private int tickCountForClear = 0;

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		Lands.onPlayerTick(event);
		Powers.onPlayerTick(event);
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		Homes.onWorldTick(event);
		if(!event.world.isRemote && event.world.equals(MinecraftServer.getServer().getEntityWorld())) {
			tickCountForClear++;
				if(tickCountForClear >= 75000) {
				tickCountForClear = 0;
				MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "clearitems");
				MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "say Items cleared (every 2 minutes)");
			}
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		Homes.onLivingHurt(event);
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		PermissionManager.onPlayerJoin(event);
		Powers.onPlayerJoined(event);
		PacketHelper.updateClientFaction(event.player.getUniqueID());
		PacketHelper.updateClientRank(event.player.getUniqueID());
		PacketHelper.updatePowerLevel(event.player.getUniqueID());
	}

	@SubscribeEvent
	public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		Lands.onPlayerLeave(event);
	}

	@SubscribeEvent
	public void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
		if(!event.world.isRemote) {
			if(!PermissionManager.hasEntityPermission(event.getPlayer(), "world.block.break")) {
				event.setCanceled(true);
			}
		}
		Lands.onPlayerBreakBlock(event);
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!event.world.isRemote) {
			if(!PermissionManager.hasEntityPermission(event.entityPlayer, "world.block.interact")) {
				event.setCanceled(true);
			}
		}
		Lands.onPlayerInteract(event);
	}

	@SubscribeEvent
	public void onPlayerPlaceBlock(PlaceEvent event) {
		if(!event.world.isRemote) {
			if(!PermissionManager.hasEntityPermission(event.player, "world.block.place")) {
				event.setCanceled(true);
			}
		}
		Lands.onPlayerPlaceBlock(event);
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		if(!event.world.isRemote && event.current.getItem().equals(Items.bucket)) {
			if(!PermissionManager.hasEntityPermission(event.entityPlayer, "world.block.break")) {
				event.setCanceled(true);
			}
		} else {
			if(!PermissionManager.hasEntityPermission(event.entityPlayer, "world.block.place")) {
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
	public void onLivingDeath(LivingDeathEvent event) {
		Powers.onLivingDeath(event);
		Faction.Registry.onLivingDeath(event);
	}

	@SubscribeEvent
	public void onEntityJoining(EntityJoinWorldEvent event) {
		if(!event.world.isRemote && event.world.equals(MinecraftServer.getServer().getEntityWorld())) {
			if(Lands.isSafeZone(event.world.getChunkFromBlockCoords(event.entity.getPosition()).getChunkCoordIntPair())) {
				if(event.entity instanceof EntityMob || event.entity instanceof EntityAnimal || event.entity instanceof EntitySlime) {
					event.setCanceled(true);
				}
			}
		}
	}
}
