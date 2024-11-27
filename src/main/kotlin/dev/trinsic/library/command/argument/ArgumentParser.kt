package dev.trinsic.library.command.argument

import dev.trinsic.library.command.executor.CommandExecutor

abstract class ArgumentParser<T>(val errorMessage: String) {

    abstract fun parse(str: String, executor: CommandExecutor): T?

    fun parseSafe(str: String, executor: CommandExecutor): T? =
        try {
            parse(str, executor)
        } catch (ex: Exception) {
            null
        }


    open fun tabComplete(str: String, sender: CommandExecutor): List<String> {
        return emptyList()
    }

}