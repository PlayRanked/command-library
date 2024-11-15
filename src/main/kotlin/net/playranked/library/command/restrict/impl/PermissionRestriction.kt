package net.playranked.library.command.restrict.impl

import net.playranked.library.command.execute.CommandExecution
import net.playranked.library.command.restrict.CommandRestrictor

class PermissionRestriction(val permission: String, message: String = "&cNo permission."): CommandRestrictor(message) {
    override fun canExecute(execution: CommandExecution): Boolean {
        val executor = execution.executor

        if (!executor.isPlayer()) {
            return true
        }

        return executor.player().hasPermission(permission)
    }
}