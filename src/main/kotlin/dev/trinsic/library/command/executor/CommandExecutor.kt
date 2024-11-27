package dev.trinsic.library.command.executor

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandExecutor(val sender: CommandSender) {

    fun player(): Player {
        return sender as Player
    }

    fun isConsole(): Boolean {
        return !isPlayer()
    }

    fun isPlayer(): Boolean {
        return sender is Player
    }
}
