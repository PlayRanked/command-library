package net.playranked.library.command.argument.parser

import net.playranked.library.command.argument.ArgumentParser
import net.playranked.library.command.executor.CommandExecutor

object IntArgument : ArgumentParser<Int>("Invalid integer '%s' provided.") {
    override fun parse(str: String, sender: CommandExecutor): Int? {
        return str.toInt()
    }
}