package net.playranked.library.command.builder

import net.playranked.library.command.argument.ArgumentParser
import net.playranked.library.command.executor.CommandExecutor
import net.playranked.library.command.argument.ArgumentHandler
import org.bukkit.entity.Player

class ArgumentBuilder<T> (val name: String, var parser: ArgumentParser<T>? = null): ArgumentHandler {

    private var validator: ((CommandExecutor, T) -> Boolean)? = null
    private var errorMessage = parser?.errorMessage

    private var optional = false
    private var wildcard = false

    fun parser(parser: (Unit) -> ArgumentParser<T>): ArgumentBuilder<T> {
        this.parser = parser.invoke(Unit)
        this.errorMessage = this.parser!!.errorMessage
        return this
    }

    fun validate(validation: (CommandExecutor, T) -> Boolean): ArgumentBuilder<T> {
        this.validator = validation
        return this
    }

    fun errorMessage(message: String): ArgumentBuilder<T> {
        this.errorMessage = message
        return this
    }

    fun optional(): ArgumentBuilder<T> {
        optional = true
        return this
    }

    fun wildcard(): ArgumentBuilder<T> {
        wildcard = true
        return this
    }

    override fun matches(executor: CommandExecutor, provided: String): Boolean {
        return parser!!.parse(provided, executor) != null
    }

    override fun isOptional(): Boolean {
        return optional
    }

    override fun isWildcard(): Boolean {
        return wildcard
    }

    fun parse(executor: CommandExecutor, provided: String): T {
        return parser!!.parse(provided, executor) as T
    }
}