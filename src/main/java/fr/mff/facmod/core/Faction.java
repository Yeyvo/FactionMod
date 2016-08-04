package fr.mff.facmod.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import com.mojang.authlib.GameProfile;

import fr.mff.facmod.config.ConfigFaction;
import fr.mff.facmod.network.PacketHelper;

public class Faction {

	protected final String name;
	protected String description;
	protected boolean opened;

	protected final List<UUID> bannedPlayers;
	protected final List<Member> members;
	protected final List<UUID> invitations;

	public Faction(String name, String description) {
		this(name, description, new ArrayList<UUID>(), new ArrayList<Member>(), new ArrayList<UUID>(), false);
	}

	public Faction(String name, String description, List<UUID> bannedPlayers, List<Member> members, List<UUID> invitations, boolean opened) {
		this.name = name;
		this.description = description;
		this.bannedPlayers = bannedPlayers;
		this.members = members;
		this.invitations = invitations;
		Faction.Registry.factions.add(this);
		FactionSaver.save();
	}

	public String getName() {
		return this.name;
	}

	public int getMaxPowerLevel() {
		return members.size() * ConfigFaction.POWER_PER_PLAYER;
	}

	public int getPowerLevel() {
		int power = 0;
		for(Member member : members) {
			power += Powers.getPowerOf(member.getUUID());
		}
		return power;
	}

	public String getDescription() {
		return this.description;
	}

	public List<Member> getMembers() {
		return this.members;
	}

	public List<UUID> getBannedPlayers() {
		return this.bannedPlayers;
	}

	public List<UUID> getInvitations() {
		return this.invitations;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOpened() {
		return this.opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	/**
	 * Add player to the faction
	 * @param uuid
	 * @param rank
	 */
	public void addPlayer(UUID uuid, EnumRank rank) {
		this.members.add(new Member(uuid, rank));
		Faction.Registry.playersFactions.put(uuid, this.name);
		PacketHelper.updateClientFaction(uuid);
		PacketHelper.updateClientRank(uuid);
		FactionSaver.save();
	}

	/**
	 * Remove the player from the faction
	 * @param uuid
	 * @return The player removed, null if the player wasn't a member
	 */
	public Member removePlayer(UUID uuid) {
		Member toRemove = null;
		for(Member member : this.members) {
			if(member.getUUID().equals(uuid)) {
				toRemove = member;
			}
		}
		this.members.remove(toRemove);
		Faction.Registry.playersFactions.remove(uuid);
		PacketHelper.updateClientFaction(uuid);
		PacketHelper.updateClientRank(uuid);
		FactionSaver.save();
		return toRemove;
	}

	/**
	 * Choose a new owner
	 */
	public void chooseNewOwner() {
		int highestRank = EnumRank.values().length;
		List<Member> promotableMembers = new ArrayList<Member>();
		for(Member member : this.members) {
			int degree = member.getRank().getAutority();
			if(degree < highestRank) {
				highestRank = degree;
				promotableMembers.clear();
			}
			if(degree == highestRank) {
				promotableMembers.add(member);
			}
		}
		promotableMembers.get(0).setRank(EnumRank.OWNER);
	}

	/**
	 * Remove the faction, 
	 */
	public void remove() {
		Faction.Registry.factions.remove(this);
		for(Member member : this.members) {
			Faction.Registry.factions.remove(member.getUUID());
			Faction.Registry.playersFactions.remove(member.getUUID());
			PacketHelper.updateClientFaction(member.getUUID());
			PacketHelper.updateClientRank(member.getUUID());
		}
		Lands.clearChunksFaction(this.name);
		FactionSaver.save();
	}

	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("name", this.name);
		compound.setString("description", this.description);
		compound.setBoolean("opened", this.opened);

		NBTTagList bannedList = new NBTTagList();
		for(UUID banned : this.bannedPlayers) {
			bannedList.appendTag(new NBTTagString(banned.toString()));
		}
		compound.setTag("bannedPlayers", bannedList);

		NBTTagList membersList = new NBTTagList();
		for(Member member : this.members) {
			NBTTagCompound memberTag = new NBTTagCompound();
			member.writeToNBT(memberTag);
			membersList.appendTag(memberTag);
		}
		compound.setTag("members", membersList);
	}

	public static Faction readFromNBT(NBTTagCompound compound) {
		String name = compound.getString("name");
		String description = compound.getString("description");
		boolean opened = compound.getBoolean("opened");

		List<UUID> bannedPlayers = new ArrayList<UUID>();
		NBTTagList bannedList = (NBTTagList)compound.getTag("bannedPlayers");
		for(int i = 0; i < bannedList.tagCount(); i++) {
			bannedPlayers.add(UUID.fromString(bannedList.getStringTagAt(i)));
		}

		List<Member> members = new ArrayList<Member>();
		NBTTagList membersList = (NBTTagList)compound.getTag("members");
		for(int i = 0; i < membersList.tagCount(); i++) {
			members.add(Member.readFromNBT(membersList.getCompoundTagAt(i)));
		}

		return new Faction(name, description, bannedPlayers, members, new ArrayList<UUID>(), opened);
	}

	public Member getMember(UUID uuid) {
		for(Member member : this.members) {
			if(member.getUUID().equals(uuid)) {
				return member;
			}
		}
		return null;
	}

	public static class Registry {

		/* ----------------- Faction Registry -------------------------- */

		public static final List<Faction> factions = new ArrayList<Faction>();
		public static final HashMap<UUID, String> playersFactions = new HashMap<UUID, String>();

		public static void clear() {
			factions.clear();
			playersFactions.clear();
		}

		public static void writeToNBT(NBTTagCompound compound) {
			NBTTagList factionsList = new NBTTagList();
			for(Faction faction : Faction.Registry.factions) {
				NBTTagCompound factionTag = new NBTTagCompound();
				faction.writeToNBT(factionTag);
				factionsList.appendTag(factionTag);
			}
			compound.setTag("factions", factionsList);

			NBTTagList playersFactionsList = new NBTTagList();
			Iterator<Entry<UUID, String>> iterator = playersFactions.entrySet().iterator();
			while(iterator.hasNext()) {
				NBTTagCompound playerFactionTag = new NBTTagCompound();
				Entry<UUID, String> entry = iterator.next();
				playerFactionTag.setString("uuid", entry.getKey().toString());
				playerFactionTag.setString("faction", entry.getValue());
				playersFactionsList.appendTag(playerFactionTag);
			}
			compound.setTag("playersFactions", playersFactionsList);
		}

		public static void readFromNBT(NBTTagCompound compound) {
			Faction.Registry.clear();
			NBTTagList factionsList = (NBTTagList)compound.getTag("factions");
			for(int i = 0; i < factionsList.tagCount(); i++) {
				Faction.readFromNBT(factionsList.getCompoundTagAt(i));
			}

			NBTTagList playersFactionsList = (NBTTagList)compound.getTag("playersFactions");
			for(int i = 0; i < playersFactionsList.tagCount(); i++) {
				NBTTagCompound playerFactionTag = playersFactionsList.getCompoundTagAt(i);
				playersFactions.put(UUID.fromString(playerFactionTag.getString("uuid")), playerFactionTag.getString("faction"));
			}
		}

		/**
		 * Return the faction with the given name
		 * @param name
		 * @return {@code null} if any faction has been found
		 */
		public static Faction getFactionFromName(String name) {
			for(Faction faction : Faction.Registry.factions) {
				if(faction.getName().equalsIgnoreCase(name)) {
					return faction;
				}
			}
			return null;
		}

		/**
		 * Return player's faction
		 * @param uuid
		 * @return {@code null} if the player isn't in a faction
		 */
		public static Faction getPlayerFaction(UUID uuid) {
			return Faction.Registry.getFactionFromName(Faction.Registry.playersFactions.get(uuid));
		}

		private static final int MINIMUM_NAME_LENGTH = 3;
		private static final int MAXIMUM_NAME_LENGTH = 15;
		private static final int MAXIMUM_DESCRIPTION_LENGTH = 100;

		public static EnumResult createFaction(UUID uuid, String name, String description) {
			Faction faction = Faction.Registry.getPlayerFaction(uuid);
			if(faction == null) {
				faction = Faction.Registry.getFactionFromName(name);
				if(faction == null) {
					if(!name.equalsIgnoreCase("safezone") && !name.equalsIgnoreCase("warzone") && !name.equalsIgnoreCase("wilderness")) {
						if(name.length() >= MINIMUM_NAME_LENGTH && name.length() <= MAXIMUM_NAME_LENGTH) {
							if(description.length() <= MAXIMUM_DESCRIPTION_LENGTH) {
								Faction newFaction = new Faction(name, description);
								newFaction.addPlayer(uuid, EnumRank.OWNER);
								return EnumResult.FACTION_CREATED.clear().addInformation(EnumChatFormatting.GOLD + newFaction.getName());
							}
							return EnumResult.INVALID_DESCRIPTION_LENGTH.clear().addInformation(EnumChatFormatting.WHITE + String.valueOf(MAXIMUM_DESCRIPTION_LENGTH));
						}
						return EnumResult.INVALID_NAME_LENGTH.clear().addInformation(EnumChatFormatting.WHITE + String.valueOf(MINIMUM_NAME_LENGTH)).addInformation(EnumChatFormatting.WHITE + String.valueOf(MAXIMUM_NAME_LENGTH));
					}
					return EnumResult.TAKEN_FACTION_NAME.clear().addInformation(name);
				}
				return EnumResult.TAKEN_FACTION_NAME.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
			}
			return EnumResult.IN_A_FACTION.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
		}

		public static EnumResult joinFaction(UUID uuid, String name) {
			Faction faction = Faction.Registry.getPlayerFaction(uuid);
			if(faction == null) {
				faction = Faction.Registry.getFactionFromName(name);
				if(faction != null) {
					if(faction.isOpened() || faction.getInvitations().contains(uuid)) {
						faction.getInvitations().remove(uuid);
						faction.addPlayer(uuid, EnumRank.BASIC_MEMBER);
						return EnumResult.FACTION_JOINED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
					}
					return EnumResult.INVITATION_NEEDED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
				}
				return EnumResult.NOT_EXISTING_FACTION.clear().addInformation(EnumChatFormatting.GOLD + name);
			}
			return EnumResult.IN_A_FACTION.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
		}

		public static EnumResult leaveFaction(UUID uuid) {
			Faction faction = Faction.Registry.getPlayerFaction(uuid);
			if(faction != null) {
				Member member = faction.removePlayer(uuid);
				if(faction.getMembers().size() > 0) {
					if(member.getRank().equals(EnumRank.OWNER)) {
						faction.chooseNewOwner();
					}
				} else {
					faction.remove();
				}
				return EnumResult.FACTION_LEFT.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
			}
			return EnumResult.NOT_IN_A_FACTION;
		}

		public static EnumResult kickPlayer(UUID executor, String slaveName) {
			Faction faction = Faction.Registry.getPlayerFaction(executor);
			if(faction != null) {
				if(faction.getMember(executor).getRank().hasPermission(Permission.COMMUNITY_HANDLING)) {
					GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(slaveName);
					if(profile != null) {
						UUID slave = profile.getId();
						if(Faction.Registry.getPlayerFaction(slave).getName().equalsIgnoreCase(faction.getName())) {
							if(EnumRank.canAffect(executor, slave)) {
								faction.removePlayer(slave);
								return EnumResult.PLAYER_KICKED.clear().addInformation(EnumChatFormatting.WHITE + slaveName).addInformation(EnumChatFormatting.GOLD + faction.getName());
							}
							return EnumResult.NO_PERMISSION;
						}
						return EnumResult.PLAYER_NOT_IN_THE_FACTION.clear().addInformation(EnumChatFormatting.WHITE + profile.getName()).addInformation(EnumChatFormatting.GOLD + faction.getName());
					}
					return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(EnumChatFormatting.WHITE + slaveName);
				}
				return EnumResult.NO_PERMISSION;
			}
			return EnumResult.NOT_IN_A_FACTION;
		}

		public static EnumResult setFactionOpen(UUID executor, boolean opened) {
			Faction faction = Faction.Registry.getPlayerFaction(executor);
			if(faction != null) {
				if(faction.getMember(executor).getRank().hasPermission(Permission.COMMUNITY_HANDLING)) {
					faction.setOpened(opened);
					return opened ? EnumResult.FACTION_OPENED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName()) : EnumResult.FACTION_CLOSED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
				}
				return EnumResult.NO_PERMISSION;
			}
			return EnumResult.NOT_IN_A_FACTION;
		}

		public static EnumResult banPlayer(UUID executor, String slaveName) {
			Faction faction = Faction.Registry.getPlayerFaction(executor);
			if(faction != null) {
				if(faction.getMember(executor).getRank().hasPermission(Permission.COMMUNITY_HANDLING)) {
					GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(slaveName);
					if(profile != null) {
						UUID slave = profile.getId();
						if(Faction.Registry.getPlayerFaction(slave).getName().equalsIgnoreCase(faction.getName())) {
							if(EnumRank.canAffect(executor, slave)) {
								for(UUID uuid : faction.getBannedPlayers()) {
									if(uuid.equals(slave)) {
										return EnumResult.ALREADY_BANNED_PLAYER.clear().addInformation(EnumChatFormatting.WHITE + slaveName);
									}
								}
								faction.removePlayer(slave);
								faction.getBannedPlayers().add(slave);
								return EnumResult.PLAYER_BANNED.clear().addInformation(EnumChatFormatting.WHITE + slaveName).addInformation(EnumChatFormatting.GOLD + faction.getName());
							}
							return EnumResult.NO_PERMISSION;
						}
						return EnumResult.PLAYER_NOT_IN_THE_FACTION.clear().addInformation(EnumChatFormatting.WHITE + profile.getName()).addInformation(EnumChatFormatting.GOLD + faction.getName());
					}
					return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(EnumChatFormatting.WHITE + slaveName);
				}
				return EnumResult.NO_PERMISSION;
			}
			return EnumResult.NOT_IN_A_FACTION;
		}

		public static EnumResult destroyFaction(UUID uuid) {
			Faction faction = Faction.Registry.getPlayerFaction(uuid);
			if(faction != null) {
				Member member = faction.getMember(uuid);
				if(member != null) {
					if(member.getRank().hasPermission(Permission.FACTION_HANDLING)) {
						faction.remove();
						return EnumResult.FACTION_REMOVED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
					}
					return EnumResult.NO_PERMISSION;
				}
				return EnumResult.ERROR;
			}
			return EnumResult.NOT_IN_A_FACTION;
		}

		public static EnumResult changeDescription(UUID uuid, String desc) {
			Faction faction = Faction.Registry.getPlayerFaction(uuid);
			if(faction != null) {
				Member member = faction.getMember(uuid);
				if(member != null) {
					if(member.getRank().hasPermission(Permission.FACTION_HANDLING)) {
						if(desc.length() >= 0 && desc.length() <= MAXIMUM_DESCRIPTION_LENGTH) {
							faction.setDescription(desc);
							return EnumResult.DESCRIPTION_CHANGED.clear().addInformation(EnumChatFormatting.GOLD + faction.getName());
						}
						return EnumResult.INVALID_DESCRIPTION_LENGTH.clear().addInformation(String.valueOf(MAXIMUM_DESCRIPTION_LENGTH));
					}
					return EnumResult.NO_PERMISSION;
				}
				return EnumResult.ERROR;
			}
			return EnumResult.NOT_IN_A_FACTION;
		}

		public static EnumResult invite(UUID executor, String slaveName) {
			Faction faction = Faction.Registry.getPlayerFaction(executor);
			if(faction != null) {
				Member member = faction.getMember(executor);
				if(member != null) {
					if(member.getRank().hasPermission(Permission.COMMUNITY_HANDLING)) {
						GameProfile slave = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(slaveName);
						if(slave != null) {
							if(faction.getMember(slave.getId()) == null) {
								if(!faction.getInvitations().contains(slave.getId())) {
									Entity entity = MinecraftServer.getServer().getEntityFromUuid(slave.getId());
									if(entity instanceof EntityPlayer) {
										faction.getInvitations().add(slave.getId());
										((EntityPlayer)entity).addChatComponentMessage(new ChatComponentTranslation("msg.invitation.received", faction.getName()));
										return EnumResult.PLAYER_INVITED.clear().addInformation(slave.getName());
									}
									return EnumResult.NOT_CONNECTED_PLAYER.clear().addInformation(EnumChatFormatting.WHITE + slave.getName());
								}
								return EnumResult.ALREADY_INVITED_PLAYER.clear().addInformation(slave.getName());
							}
							return EnumResult.IN_THE_FACTION.clear().addInformation(EnumChatFormatting.WHITE + slave.getName()).addInformation(EnumChatFormatting.GOLD + faction.getName());
						}
						return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(EnumChatFormatting.WHITE + slaveName);
					}
					return EnumResult.NO_PERMISSION;
				}
				return EnumResult.ERROR;
			}
			return EnumResult.NOT_IN_A_FACTION;
		}

		public static EnumResult info(EntityPlayer player, String[] args) {
			Faction faction;
			if(args.length >= 2) {
				faction = Faction.Registry.getFactionFromName(args[1]);
			} else {
				faction = Faction.Registry.getPlayerFaction(player.getUniqueID());
			}
			if(faction != null) {
				player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + "[ " + faction.getName() + " ]"));
				if(!faction.getDescription().isEmpty()) {
					player.addChatComponentMessage(new ChatComponentTranslation("msg.description", EnumChatFormatting.BLUE + faction.getDescription()));
				}
				player.addChatComponentMessage(new ChatComponentTranslation("msg.members", EnumChatFormatting.GREEN.toString() + faction.getMembers().size()));
				player.addChatComponentMessage(new ChatComponentTranslation("msg.lands", EnumChatFormatting.YELLOW.toString() + Lands.getLandsForFaction(faction.getName()).size()));
				player.addChatComponentMessage(new ChatComponentTranslation("msg.power", EnumChatFormatting.BLUE.toString() + faction.getPowerLevel() + "/" + faction.getMaxPowerLevel()));
				return null;
			}
			if(args.length >= 2) {
				return EnumResult.NOT_EXISTING_FACTION.clear().addInformation(EnumChatFormatting.GOLD + args[1]);
			} else {
				return  EnumResult.NOT_IN_A_FACTION;
			}
		}

		public static EnumResult members(EntityPlayer player, String[] args) {
			Faction faction;
			if(args.length >= 2) {
				faction = Faction.Registry.getFactionFromName(args[1]);
			} else {
				faction = Faction.Registry.getPlayerFaction(player.getUniqueID());
			}
			if(faction != null) {
				player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + "[ " + faction.getName() + " ]"));
				faction.getMembers().sort(new Member.MemberComparator());
				for(Member member : faction.getMembers()) {
					String memberName = MinecraftServer.getServer().getPlayerProfileCache().getProfileByUUID(member.getUUID()).getName();
					player.addChatComponentMessage(new ChatComponentText(member.getRank().getColor() + memberName + EnumChatFormatting.RESET + " : " + member.getRank().getDisplay()));
				}
				return null;
			}
			if(args.length >= 2) {
				return EnumResult.NOT_EXISTING_FACTION.clear().addInformation(EnumChatFormatting.GOLD + args[1]);
			} else {
				return  EnumResult.NOT_IN_A_FACTION;
			}
		}

		public static EnumResult promote(UUID uuid, String[] args) {
			Faction faction = Faction.Registry.getPlayerFaction(uuid);
			if(faction != null) {
				GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(args[1]);
				if(profile != null) {
					Faction pFaction = Faction.Registry.getPlayerFaction(profile.getId());
					if(pFaction != null && pFaction == faction) {
						Member pMember = faction.getMember(profile.getId());
						Member member = faction.getMember(uuid);
						if(pMember != null && member != null) {
							if(EnumRank.canAffect(uuid, profile.getId()) && member.getRank().hasPermission(Permission.COMMUNITY_HANDLING)) {
								EnumRank rank = EnumRank.valueOf(args[2]);
								if(rank != null) {
									if(rank.getAutority() < member.getRank().getAutority()) {
										pMember.setRank(rank);
										Entity entity = MinecraftServer.getServer().getEntityFromUuid(profile.getId());
										if(entity instanceof EntityPlayer) {
											((EntityPlayer)entity).addChatComponentMessage(new ChatComponentTranslation("msg.promoted", new Object[]{rank.getColor() + rank.getDisplay()}));
										}
										FactionSaver.save();
										return EnumResult.PLAYER_PROMOTED.clear().addInformation(profile.getName()).addInformation(rank.getColor() + rank.getDisplay());
									} else if(member.getRank().equals(EnumRank.OWNER) && rank.equals(EnumRank.OWNER)) {
										member.setRank(EnumRank.MANAGER);
										pMember.setRank(EnumRank.OWNER);
										return EnumResult.PLAYER_PROMOTED.clear().addInformation(profile.getName()).addInformation(rank.getColor() + rank.getDisplay());
									}
									return EnumResult.NO_PERMISSION;
								}
								return EnumResult.WRONG_RANK.clear().addInformation(args[2]);
							}
							return EnumResult.NO_PERMISSION;
						}
						return EnumResult.ERROR;
					}
					return EnumResult.PLAYER_NOT_IN_THE_FACTION.clear().addInformation(EnumChatFormatting.WHITE + args[1]).addInformation(EnumChatFormatting.GOLD + faction.getName());
				}
				return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(EnumChatFormatting.WHITE + args[1]);
			}
			return EnumResult.NOT_IN_A_FACTION;
		}

		public static EnumResult unbanPlayer(UUID executor, String slaveName) {
			Faction faction = Faction.Registry.getPlayerFaction(executor);
			if(faction != null) {
				if(faction.getMember(executor).getRank().hasPermission(Permission.COMMUNITY_HANDLING)) {
					GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(slaveName);
					if(profile != null) {
						UUID slave = profile.getId();
						for(UUID uuid : faction.getBannedPlayers()) {
							if(uuid.equals(slave)) {
								faction.getBannedPlayers().remove(slave);
								return EnumResult.PLAYER_UNBANNED.clear().addInformation(EnumChatFormatting.WHITE + slaveName);
							}
						}
						return EnumResult.PLAYER_NOT_BANNED.clear().addInformation(EnumChatFormatting.WHITE + slaveName).addInformation(EnumChatFormatting.GOLD + faction.getName());
					}
					return EnumResult.NOT_EXISTING_PLAYER.clear().addInformation(EnumChatFormatting.WHITE + slaveName);
				}
				return EnumResult.NO_PERMISSION;
			}
			return EnumResult.NOT_IN_A_FACTION;
		}

	}
}