package net.playranked.library.command.argument

import net.playranked.library.command.executor.CommandExecutor

abstract class ArgumentParser<T>(val errorMessage: String) {

    abstract fun parse(str: String, sender: CommandExecutor): T?

    open fun tabComplete(str: String, sender: CommandExecutor): List<String> {
        return emptyList()
    }

}