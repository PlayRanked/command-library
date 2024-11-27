package dev.trinsic.library.command.argument.parser

import dev.trinsic.library.command.argument.ArgumentParser
import dev.trinsic.library.command.executor.CommandExecutor

object StringArgument : ArgumentParser<String>("N/A") {
    override fun parse(str: String, sender: CommandExecutor): String {
        return str
    }
}