package net.playranked.library.command.argument.parser

import net.playranked.library.command.argument.ArgumentParser
import net.playranked.library.command.executor.CommandExecutor

object IntArgument : ArgumentParser<Int>("Invalid integer '%s' provided.") {
    override fun parse(str: String, executor: CommandExecutor): Int? {
        return try {
            str.toInt()
        } catch (ex: Exception) {
            null
        }
    }
}