package net.playranked.library.command

import net.playranked.library.command.builder.CommandBuilder
import net.playranked.library.command.executor.CommandExecutor

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