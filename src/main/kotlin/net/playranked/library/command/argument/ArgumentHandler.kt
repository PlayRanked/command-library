package net.playranked.library.command.argument

import net.playranked.library.command.executor.CommandExecutor

interface ArgumentHandler {
    fun matches(executor: CommandExecutor, provided: String): Boolean

    fun isWildcard(): Boolean {
        return false
    }

    fun isOptional(): Boolean {
        return false
    }
}