package me.ethanprimmer.iungo.commands;

import me.ethanprimmer.iungo.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;


public class TabCompletion implements TabCompleter {

	private final Main plugin;

	public TabCompletion(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		List<String> completions = new ArrayList<>(), commands = plugin.getCommandHandler().getSubCommands().stream().map(SubCommand::getName).collect(Collectors.toList());
		String arg = args[0];

		if (args.length == 1) {
			StringUtil.copyPartialMatches(arg, commands, completions);
		}

		completions.sort(null);
		return completions;
	}
}