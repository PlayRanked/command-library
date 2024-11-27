package dev.trinsic.library.command.restrict

import dev.trinsic.library.command.execute.CommandExecution

abstract class CommandRestrictor(private val message: String = "") {
    abstract fun canExecute(execution: CommandExecution): Boolean

    fun attemptExecution(execution: CommandExecution): Boolean {
        val canExecute = canExecute(execution)

        if (!canExecute) {
            execution.executor.sender.sendMessage(message)
        }

        return canExecute
    }
}