package net.playranked.library.command.argument.parser

import net.playranked.library.command.argument.ArgumentParser
import net.playranked.library.command.executor.CommandExecutor

object StringArgument : ArgumentParser<String>("N/A") {
    override fun parse(str: String, sender: CommandExecutor): String {
        return str;
    }
}