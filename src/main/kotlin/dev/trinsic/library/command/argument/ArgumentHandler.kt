package dev.trinsic.library.command.argument

import dev.trinsic.library.command.executor.CommandExecutor

interface ArgumentHandler {
    fun matches(executor: CommandExecutor, provided: String): Boolean
    fun tabComplete(executor: CommandExecutor, provided: String): List<String>

    fun isWildcard(): Boolean {
        return false
    }

    fun isOptional(): Boolean {
        return false
    }
}