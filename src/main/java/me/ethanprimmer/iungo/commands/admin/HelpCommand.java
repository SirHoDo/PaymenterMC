package me.ethanprimmer.iungo.commands.admin;

import me.ethanprimmer.iungo.commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class HelpCommand extends SubCommand {

	public HelpCommand() {
		super ("help");
	}

	@Override
	public String getPossibleArguments() {
		return null;
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		sender.sendMessage("");
		sender.sendMessage(chatManager.coloredRawMessage("&e---- &6&lPaymenter Commands &e----"));
		sender.sendMessage("");

		List<SubCommand> subCommands = new ArrayList<>(plugin.getCommandHandler().getSubCommands());
		int pageSize = 5;
		int totalPages = (int) Math.ceil((double) subCommands.size() / pageSize);

		int pageNumber = 1;
		if (args.length > 0) {
			try {
				pageNumber = Integer.parseInt(args[0]);
				if (pageNumber < 1 || pageNumber > totalPages) {
					pageNumber = 1;
				}
			} catch (NumberFormatException ignored) {
			}
		}

		int startIndex = (pageNumber - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, subCommands.size());

		for (int i = startIndex; i < endIndex; i++) {
			SubCommand subCommand = subCommands.get(i);

			String usage;
			if (subCommand.getType() == GENERIC) {
				usage = "   /" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments() != null ? " " + subCommand.getPossibleArguments() : "");
			} else if (subCommand.getType() == HIDDEN && sender.hasPermission("paymenter.admin")) {
				usage = ChatColor.YELLOW + "★ " + ChatColor.RED + "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments() != null ? " " + subCommand.getPossibleArguments() : "");
			} else {
				continue;
			}

			String usageHover;
			if (subCommand.getType() == GENERIC) {
				usageHover = "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments() != null ? " " + subCommand.getPossibleArguments() : "");
			} else if (subCommand.getType() == HIDDEN && sender.hasPermission("paymenter.admin")) {
				usageHover = ChatColor.RED + "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments() != null ? " " + subCommand.getPossibleArguments() : "");
			} else {
				continue;
			}

			if (subCommand.getType() == GENERIC) {
				if (sender instanceof Player) {
					List<String> help = new ArrayList<>();
					help.add(ChatColor.DARK_AQUA + usageHover);

					if (subCommand.getTutorial() != null) {
						help.add(ChatColor.AQUA + subCommand.getTutorial());
					}

					((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
						.color(ChatColor.AQUA)
						.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.join("\n", help))))
						.create());
				} else {
					sender.sendMessage(ChatColor.AQUA + usage);
				}
			} else if (subCommand.getType() == HIDDEN && sender.hasPermission("paymenter.admin")) {

				if (sender instanceof Player) {
					List<String> help = new ArrayList<>();
					help.add(ChatColor.YELLOW + "ADMIN " + ChatColor.DARK_AQUA + usageHover);

					if (subCommand.getTutorial() != null) {
						help.add(ChatColor.RED + subCommand.getTutorial());
					}

					((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
						.color(ChatColor.RED)
						.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.join("\n", help))))
						.create());
				} else {
					sender.sendMessage(ChatColor.AQUA + usage);
				}
			}
		}

		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage("");
			player.spigot().sendMessage(new ComponentBuilder("TIP:")
				.color(ChatColor.YELLOW)
				.bold(true)
				.append("hover")
				.color(ChatColor.WHITE)
				.underlined(true)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Hover on the commands to get info about them.")))
				.append(" or ", ComponentBuilder.FormatRetention.NONE)
				.color(ChatColor.GRAY)
				.append("click")
				.color(ChatColor.WHITE)
				.underlined(true)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Click on the commands to insert them in the chat.")))
				.append(" on the commands!", ComponentBuilder.FormatRetention.NONE)
				.color(ChatColor.GRAY)
				.create());
		}
		sender.sendMessage(chatManager.coloredRawMessage("&e&lPage: &6" + pageNumber + "&e/&6" + totalPages + " &b/" + label + " help [page]"));
	}


	@Override
	public String getTutorial() {
		return null;
	}

	@Override
	public int getType() {
		return GENERIC;
	}

	@Override
	public int getSenderType() {
		return BOTH;
	}

	@Override
	public int getCooldown() {
		return 0;
	}
}