package wtf.nucker.buildstom.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.instance.Instance

class SaveCommand(private val instance: Instance): Command("save", "saveworld") {

	init {
		addSyntax({ sender, _ ->
			instance.saveChunksToStorage()
			sender.sendMessage(Component.text("World has been saved", NamedTextColor.GREEN))
		})
	}
}