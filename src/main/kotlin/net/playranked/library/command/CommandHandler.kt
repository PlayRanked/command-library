package net.playranked.library.command

import net.playranked.library.command.builder.ArgumentBuilder
import net.playranked.library.command.builder.CommandBuilder
import net.playranked.library.command.bukkit.BukkitCommand
import net.playranked.library.command.execute.CommandExecution
import net.playranked.library.command.executor.CommandExecutor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.util.logging.Level

object CommandHandler {

    var plugin = Bukkit.getPluginManager().plugins[0]

    private val commandBuilders = mutableMapOf<BukkitCommand, MutableList<CommandBuilder>>()
    private val rootCommands = mutableMapOf<String, BukkitCommand>()

    fun handleCommandInput(sender: CommandSender, label: String, args: Array<String>) {
        val executor = CommandExecutor(sender)
        val highestProbability = getHighestProbability(executor, label, args)

        if (highestProbability.isEmpty()) {
            Bukkit.getLogger().log(Level.WARNING, "[Command Library] Missing command builder for '$label'.")
            return
        }

        if (highestProbability[0].canExecute(executor, args)) {
            executeCommand(executor, args, highestProbability[0])
            return
        }

        sender.sendMessage("you had " + highestProbability.size + " matches : " + highestProbability[0].aliases[0] + " was your main one" )
    }

    private fun getHighestProbability(executor: CommandExecutor, label: String, args: Array<String>): List<CommandBuilder> {
        val bukkitCommand = rootCommands[label.lowercase()]

        if (bukkitCommand == null) {
            Bukkit.getLogger().log(Level.WARNING, "[Command Library] Missing root command for '$label'.")
            return emptyList()
        }

        return commandBuilders[bukkitCommand]!!.sortedByDescending { it.probability(executor, args) }
    }

    private fun executeCommand(executor: CommandExecutor, args: Array<String>, builder: CommandBuilder) {
        if (builder.executor == null) {
            Bukkit.getLogger().log(Level.WARNING, "[Command Library] '${builder.aliases[0]}' missing executor.")
            return
        }

        val execution = CommandExecution(builder, executor, args.toList())
        if (builder.restrictions.any { !it.attemptExecution(execution) }) {
            return
        }

        builder.executor!!.invoke(execution)
    }

    fun registerBuilder(builder: CommandBuilder) {
        if (builder.arguments.filter { it.value is ArgumentBuilder<*> }.any {
                if ((it.value as ArgumentBuilder<*>).parser == null) {
                    Bukkit.getLogger().log(Level.WARNING, "[Command Library] Argument '$it'.")
                    return@any true
                }

                return@any false
            }) {
            return
        }

        builder.aliases.forEach {
            registerRoot(builder, it.lowercase())
        }
    }

    private fun registerRoot(builder: CommandBuilder, root: String) {
        if (!rootCommands.containsKey(root)) {
            rootCommands[root] = BukkitCommand(root)
        }

        val bukkitCommand = rootCommands[root]!!
        val builders = commandBuilders.getOrDefault(bukkitCommand, mutableListOf())
        builders += builder
        commandBuilders[bukkitCommand] = builders
    }

}