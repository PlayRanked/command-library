package net.playranked.library.command

import com.sun.jdi.IntegerType
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

    private val helpers = mutableMapOf<BukkitCommand, (CommandExecutor, List<CommandBuilder>) -> Unit>()
    private var defaultHelper: ((CommandExecutor, List<CommandBuilder>) -> Unit) = { executor, list ->
        executor.sender.sendMessage("You entered an invalid command.")
        executor.sender.sendMessage("Did you mean: ")
        list.forEach {
            executor.sender.sendMessage("- ${it.baseCmd}")
        }
    }

    fun handleCommandInput(sender: CommandSender, label: String, args: Array<String>) {
        val executor = CommandExecutor(sender)
        val bukkitCommand = getBukkitCommand(label)
        val highestProbability = getHighestProbability(executor, bukkitCommand, args)

        if (highestProbability.isEmpty()) {
            sender.sendMessage("You entered an invalid command.")
            return
        }

        if (highestProbability[0].canExecute(executor, args)) {
            executeCommand(executor, args, highestProbability[0])
            return
        }

        val helper = helpers[bukkitCommand]
        if (helper != null) {
            helper.invoke(CommandExecutor(sender), highestProbability)
            return
        }

        defaultHelper.invoke(CommandExecutor(sender), highestProbability)
    }

    fun handleTabComplete(sender: CommandSender, label: String, args: Array<String>): List<String> {
        val executor = CommandExecutor(sender)
        val highestProbability = getHighestProbability(executor, getBukkitCommand(label), args)
        return highestProbability.map { it.arguments.entries.toList()[args.size - 1].value.tabComplete(executor, args[args.size - 1]) }.flatten()
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

    fun setDefaultHelper(helper: ((CommandExecutor, List<CommandBuilder>) -> Unit)) {
        defaultHelper = helper
    }

    fun setHelper(name: String, helper: (CommandExecutor, List<CommandBuilder>) -> Unit) {
        helpers[getBukkitCommand(name.lowercase())] = helper
    }

    private fun getHighestProbability(executor: CommandExecutor, bukkitCommand: BukkitCommand, args: Array<String>): List<CommandBuilder> {
        val probabilities: List<Pair<Int, CommandBuilder>> = commandBuilders[bukkitCommand]!!.map {
            Pair(it.probability(executor, args), it)
        }.sortedByDescending { it.first }

        val bestValue = probabilities[0].first
        return probabilities.filter { it.first == bestValue && it.first != -1 }.map { it.second }.toList()
    }

    private fun executeCommand(executor: CommandExecutor, args: Array<String>, builder: CommandBuilder) {
        if (builder.executor == null) {
            Bukkit.getLogger().log(Level.WARNING, "[Command Library] '${builder.aliases[0]}' missing executor.")
            return
        }

        val execution = CommandExecution(builder, executor, args.toList())

        if (!execution.translated.validateArguments()) {
            return
        }

        if (builder.restrictions.any { !it.attemptExecution(execution) }) {
            return
        }

        builder.executor!!.invoke(execution)
    }

    private fun getBukkitCommand(label: String): BukkitCommand {
        val cmd = rootCommands[label.lowercase()]

        if (cmd == null) {
            Bukkit.getLogger().log(Level.WARNING, "[Command Library] Missing root command for '$label'.")
            return null!!
        }

        return cmd
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