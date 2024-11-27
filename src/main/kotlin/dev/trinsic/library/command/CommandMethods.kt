package dev.trinsic.library.command

import dev.trinsic.library.command.builder.CommandBuilder
import dev.trinsic.library.command.executor.CommandExecutor

fun useCommand(command: String, builder: CommandBuilder.() -> Unit) {
    val commandBuilder = CommandBuilder(command)
    builder.invoke(commandBuilder)
    CommandHandler.registerBuilder(commandBuilder)
}

fun useCommandHelp(commandRoot: String, helper: (CommandExecutor, List<CommandBuilder>) -> Unit) {
    CommandHandler.setHelper(commandRoot, helper)
}

fun useCommandHelp(helper: (CommandExecutor, List<CommandBuilder>) -> Unit) {
    CommandHandler.setDefaultHelper(helper)
}