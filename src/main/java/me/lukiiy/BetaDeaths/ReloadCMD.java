package me.lukiiy.BetaDeaths;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        BetaDeaths.inst.config();
        commandSender.sendMessage("§aBetaDeaths Reload complete.");
        return true;
    }
}
