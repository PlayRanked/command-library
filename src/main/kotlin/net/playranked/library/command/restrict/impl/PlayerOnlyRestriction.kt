package net.playranked.library.command.restrict.impl

import net.playranked.library.command.execute.CommandExecution
import net.playranked.library.command.restrict.CommandRestrictor

object PlayerOnlyRestriction : CommandRestrictor("&cYou must be a player to use this command.") {
    override fun canExecute(execution: CommandExecution): Boolean {
        return execution.executor.isPlayer()
    }
}