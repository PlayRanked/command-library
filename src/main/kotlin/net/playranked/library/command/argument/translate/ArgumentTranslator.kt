package net.playranked.library.command.argument.translate

import net.playranked.library.command.argument.ArgumentHandler
import net.playranked.library.command.argument.StaticArgument
import net.playranked.library.command.builder.ArgumentBuilder
import net.playranked.library.command.executor.CommandExecutor

class ArgumentTranslator(val executor: CommandExecutor, playerArgs: List<String>, private val argumentHandlers: MutableMap<String, ArgumentHandler>) {

    val translatedArguments = HashMap<String, Any?>()

    init {
        val argumentList = argumentHandlers.entries.toList()
        val playerArgIterator = playerArgs.iterator()

        var index = 0
        var wildcardValue = ""

        while (index < argumentHandlers.size && playerArgIterator.hasNext()) {
            var handler = argumentList[index]
            val playerArg = playerArgIterator.next()

            index++

            val matches = handler.value.matches(executor, playerArg)
            if (!matches) {
                if (!handler.value.isOptional()) {
                    break
                }

                handler = argumentList[index]
                if (!handler.value.matches(executor, playerArg)) {
                    break
                }
            }

            if (handler.value is ArgumentBuilder<*>) {
                val builder = (handler.value as ArgumentBuilder<*>)

                if (builder.isWildcard()) {
                    wildcardValue = playerArg
                    continue
                }

                val parsed = builder.parse(executor, playerArg)
                translatedArguments[handler.key] = parsed
            }

            if (handler.value is StaticArgument) {
                translatedArguments[handler.key] = playerArg
            }
        }

        if (wildcardValue.isNotEmpty()) {
            while(playerArgIterator.hasNext()) {
                wildcardValue += " " + playerArgIterator.next()
            }

            val wildcardHandler = argumentList[argumentList.size - 1]
            val wildcardBuilder = wildcardHandler.value as ArgumentBuilder<*>
            val parsed = wildcardBuilder.parse(executor, wildcardValue)

            if (parsed != null) {
                translatedArguments[wildcardHandler.key] = parsed
            }
        }
    }

    fun canExecute(): Boolean {
        return argumentHandlers.size == translatedArguments.size
    }
}