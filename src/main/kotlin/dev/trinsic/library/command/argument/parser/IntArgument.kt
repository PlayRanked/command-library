package dev.trinsic.library.command.argument.parser

import dev.trinsic.library.command.argument.ArgumentParser
import dev.trinsic.library.command.executor.CommandExecutor

object IntArgument : ArgumentParser<Int>("Invalid integer '%s' provided.") {
    override fun parse(str: String, executor: CommandExecutor): Int? {
        return try {
            str.toInt()
        } catch (ex: Exception) {
            null
        }
    }
}