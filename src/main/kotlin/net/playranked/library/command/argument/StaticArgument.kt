package net.playranked.library.command.argument

import net.playranked.library.command.executor.CommandExecutor

class StaticArgument(private val aliases: List<String>) : ArgumentHandler {
    override fun matches(executor: CommandExecutor, provided: String): Boolean {
        return aliases.contains(provided.lowercase())
    }

    override fun tabComplete(executor: CommandExecutor, provided: String): List<String> {
        return aliases.filter { it.startsWith(provided.lowercase()) }
    }
}