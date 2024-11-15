package net.playranked.library.command.argument.parser

import net.playranked.library.command.argument.ArgumentParser
import net.playranked.library.command.executor.CommandExecutor
import org.bukkit.GameMode

object GameModeArgument : ArgumentParser<GameMode>("Invalid gamemode '%s' provided.") {

    val modes = mapOf(
        "survival" to GameMode.SURVIVAL,
        "s" to GameMode.SURVIVAL,
        "0" to GameMode.SURVIVAL,
        "creative" to GameMode.CREATIVE,
        "c" to GameMode.CREATIVE,
        "1" to GameMode.CREATIVE,
        "adventure" to GameMode.ADVENTURE,
        "a" to GameMode.ADVENTURE,
        "2" to GameMode.ADVENTURE,
        "spectator" to GameMode.SPECTATOR,
        "sp" to GameMode.SPECTATOR,
        "3" to GameMode.SPECTATOR
    )

    override fun parse(str: String, executor: CommandExecutor): GameMode? {
        return if (modes.containsKey(str.lowercase())) modes[str.lowercase()] else null
    }

    override fun tabComplete(str: String, sender: CommandExecutor): List<String> {
        return modes.keys.filter { it.startsWith(str.lowercase()) }
    }
}