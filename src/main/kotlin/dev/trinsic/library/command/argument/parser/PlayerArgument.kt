package dev.trinsic.library.command.argument.parser

import dev.trinsic.library.command.argument.ArgumentParser
import dev.trinsic.library.command.executor.CommandExecutor
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PlayerArgument : ArgumentParser<Player>("Could not find a player by the name of '%s'.") {
    override fun parse(str: String, sender: CommandExecutor): Player? {
        return Bukkit.getPlayer(str)
    }

    override fun tabComplete(str: String, sender: CommandExecutor): List<String> {
        return Bukkit.getOnlinePlayers().map { it.name }.filter { it.lowercase().startsWith(str.lowercase()) }
    }
}