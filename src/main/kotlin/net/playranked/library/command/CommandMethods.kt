package net.playranked.library.command

import net.playranked.library.command.builder.CommandBuilder

fun useCommand(command: String, builder: CommandBuilder.() -> Unit) {
    val commandBuilder = CommandBuilder(command)
    builder.invoke(commandBuilder)
    CommandHandler.registerBuilder(commandBuilder)
}