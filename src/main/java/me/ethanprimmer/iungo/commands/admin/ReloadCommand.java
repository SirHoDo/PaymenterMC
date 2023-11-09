/* 
 * Paymenter Minecraft Rewrite | Paymenter Development Group
 * Author: Ethan 
 * Website: https://iungo.info
 */

package me.ethanprimmer.iungo.commands.admin;

import me.ethanprimmer.iungo.commands.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Ethan Primmer
 */
public class ReloadCommand extends SubCommand {

	public ReloadCommand() {
		super ("reload");

		setPermission("paymenter.admin.reload");
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
		plugin.reloadConfig();
		chatManager.reloadConfig();

		sender.sendMessage(chatManager.prefixedMessage("commands.admin_commands.reload"));
	}

	@Override
	public String getTutorial() {
		return "Reloads all of the system configuration";
	}

	@Override
	public int getType() {
		return HIDDEN;
	}

	@Override
	public int getSenderType() {
		return BOTH;
	}

	@Override
	public int getCooldown() {
		return 10;
	}
}