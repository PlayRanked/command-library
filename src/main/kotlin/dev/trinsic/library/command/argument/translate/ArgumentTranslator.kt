package dev.trinsic.library.command.argument.translate

import dev.trinsic.library.command.argument.ArgumentHandler
import dev.trinsic.library.command.argument.StaticArgument
import dev.trinsic.library.command.builder.ArgumentBuilder
import dev.trinsic.library.command.executor.CommandExecutor

class ArgumentTranslator(val executor: CommandExecutor, playerArgs: List<String>, private val argumentHandlers: MutableMap<String, ArgumentHandler>) {

    val translatedArguments = HashMap<String, Any?>()
    private val requiredArguments = argumentHandlers.filter { !it.value.isOptional() }.size

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

                if (index >= argumentList.size) {
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

    fun validateArguments(): Boolean {
        return !translatedArguments.any {
            val handler = argumentHandlers[it.key]

            if (handler !is ArgumentBuilder<*>) {
                return@any false
            }

            return@any !handler.runValidation(executor, it.value)
        }
    }

    fun canExecute(): Boolean {
        return (requiredArguments == translatedArguments.size) || (argumentHandlers.size == translatedArguments.size)
    }
}