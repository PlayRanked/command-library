package net.playranked.library.command.builder

import net.playranked.library.command.argument.ArgumentParser
import net.playranked.library.command.execute.CommandExecution
import net.playranked.library.command.argument.ArgumentHandler
import net.playranked.library.command.argument.StaticArgument
import net.playranked.library.command.argument.translate.ArgumentTranslator
import net.playranked.library.command.executor.CommandExecutor
import net.playranked.library.command.restrict.CommandRestrictor
import org.bukkit.Bukkit
import java.util.logging.Level

class CommandBuilder(private var baseCmd: String) {

    val aliases: MutableList<String> = mutableListOf()
    val arguments = mutableMapOf<String, ArgumentHandler>()

    val restrictions = mutableListOf<CommandRestrictor>()
    var executor: (CommandExecution.() -> Unit)? = null

    init {
        if (baseCmd.startsWith("/")) {
            baseCmd = baseCmd.substring(1)
        }

        val splitBySpaces = baseCmd.split(" ")
        aliases.addAll(splitBySpaces[0].split("|"))

        splitBySpaces.subList(1, splitBySpaces.size).forEach { argument ->
            val requiredArg = argument.startsWith("<") && argument.endsWith(">")
            val optionalArg = argument.startsWith("[") && argument.endsWith("]")
            val staticArg = !requiredArg && !optionalArg

            if (staticArg) {
                val staticSplit = argument.split("|")
                arguments[staticSplit[0].lowercase()] = StaticArgument(staticSplit)
            }

            if (optionalArg || requiredArg) {
                val name = argument.substring(1, argument.length - 1)
                val wildcard = name.endsWith("..")

                if (wildcard) {
                    name.substring(0, name.length - 2)
                }

                val argBuilder = ArgumentBuilder<Any>(name)

                argBuilder.optional()

                if (wildcard) {
                    argBuilder.wildcard()
                }

                arguments[name] = argBuilder
            }
        }
    }

    fun add(vararg aliases: String) {
        this.aliases += aliases
    }

    fun argument(name: String): ArgumentBuilder<Any>? {
        if (!arguments.containsKey(name)) {
            Bukkit.getLogger().log(Level.WARNING, "[Command Library] Argument $name doesn't exist for ${aliases[0]}")
            return null
        }

        return arguments[name] as ArgumentBuilder<Any>
    }

    fun <T> argument(name: String, parser: ArgumentParser<T>, builder: ArgumentBuilder<T>.() -> ArgumentBuilder<T>) {
        val argument = argument(name)!! as ArgumentBuilder<T>
        argument.parser = parser
        builder.invoke(argument)
    }

    fun <T> argument(name: String, builder: ArgumentBuilder<T>.() -> Unit) {
         builder.invoke(argument(name)!! as ArgumentBuilder<T>)
    }

    fun <T> argument(name: String, parser: ArgumentParser<T>) {
        (argument(name)!! as ArgumentBuilder<T>).parser = parser
    }

    fun restrict(restrictor: CommandRestrictor) {
        restrictions.add(restrictor)
    }

    fun restrict(errorMessage: String, executor: CommandExecution.() -> Boolean) {
        restrictions.add(object : CommandRestrictor(errorMessage) {
            override fun canExecute(execution: CommandExecution): Boolean {
                return executor.invoke(execution)
            }
        })
    }

    fun execute(executor: CommandExecution.() -> Unit) {
        this.executor = executor
    }

    private fun getArgumentList(): List<ArgumentHandler> {
        return arguments.values.toList()
    }

    private fun getArgument(index: Int): ArgumentHandler? {
        val argumentList = getArgumentList()

        if (argumentList.size -1 > index) {
            return null
        }

        return argumentList[index]
    }

    fun probability(executor: CommandExecutor, args: Array<String>): Int {
        var probability = 0

        if (getArgumentList().size == args.size) {
            probability += 1
        }

        if (getArgumentList()[getArgumentList().size - 1].isWildcard() && args.size > getArgumentList().size) {
            probability += 1
        }

        val translator = ArgumentTranslator(executor, args.toList(), arguments)
        probability += translator.translatedArguments.size

        return probability
    }

    fun canExecute(executor: CommandExecutor, args: Array<String>): Boolean {
        return ArgumentTranslator(executor, args.toList(), arguments).canExecute()
    }

}