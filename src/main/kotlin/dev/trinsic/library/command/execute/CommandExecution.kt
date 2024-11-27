package dev.trinsic.library.command.execute

import dev.trinsic.library.command.argument.translate.ArgumentTranslator
import dev.trinsic.library.command.builder.CommandBuilder
import dev.trinsic.library.command.executor.CommandExecutor

class CommandExecution(val command: CommandBuilder, val executor: CommandExecutor, val args: List<String>) {

    val translated = ArgumentTranslator(executor, args, command.arguments)

    fun <T> getArg(argument: String): T {
        return translated.translatedArguments[argument] as T
    }

    fun <T> getOptionalArg(argument: String): T? {
        return translated.translatedArguments[argument] as T
    }

    fun <T> getOptionalArg(argument: String, default: T): T {
        return getOptionalArg<T>(argument) ?: default
    }
}