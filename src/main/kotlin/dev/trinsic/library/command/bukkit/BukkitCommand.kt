package dev.trinsic.library.command.bukkit

import dev.trinsic.library.command.CommandHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender

class BukkitCommand(root: String) : Command(root) {

    init {
        val commandMap = CommandHandler.plugin.server.javaClass.getDeclaredField("commandMap");
        commandMap.isAccessible = true
        (commandMap.get(CommandHandler.plugin.server) as CommandMap).register(CommandHandler.plugin.name, this)
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        CommandHandler.handleCommandInput(
            sender, commandLabel, args
        )

        return true
    }

    override fun tabComplete(sender: CommandSender, label: String, args: Array<String>): List<String> {
        return CommandHandler.handleTabComplete(sender, label, args)
    }

}