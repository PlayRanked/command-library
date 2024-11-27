package dev.trinsic.library.command.restrict.impl

import dev.trinsic.library.command.execute.CommandExecution
import dev.trinsic.library.command.restrict.CommandRestrictor

class PermissionRestriction(val permission: String, message: String = "&cNo permission."): CommandRestrictor(message) {
    override fun canExecute(execution: CommandExecution): Boolean {
        val executor = execution.executor

        if (!executor.isPlayer()) {
            return true
        }

        return executor.player().hasPermission(permission)
    }
}