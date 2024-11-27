package dev.trinsic.library.command.restrict.impl

import dev.trinsic.library.command.execute.CommandExecution
import dev.trinsic.library.command.restrict.CommandRestrictor

object PlayerOnlyRestriction : CommandRestrictor("&cYou must be a player to use this command.") {
    override fun canExecute(execution: CommandExecution): Boolean {
        return execution.executor.isPlayer()
    }
}