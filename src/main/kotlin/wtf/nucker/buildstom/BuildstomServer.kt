package wtf.nucker.buildstom

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.timer.ExecutionType
import net.minestom.server.timer.TaskSchedule
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import wtf.nucker.buildstom.command.SaveCommand
import java.nio.file.Path

class BuildstomServer {

	private val  minecraftServer = MinecraftServer.init()
	private val instanceManager = MinecraftServer.getInstanceManager()
	private val globalEventHandler = MinecraftServer.getGlobalEventHandler()

	private val buildInstance = instanceManager.createInstanceContainer()

	private val config: ServerConfig

	init {
		val loader = YamlConfigurationLoader.builder()
			.path(Path.of(System.getProperty("user.dir"), "server.yml"))
			.defaultOptions {
				it.shouldCopyDefaults(true)
			}
			.indent(4)
			.build()
		config = loader.load().get(ServerConfig::class.java)!!
		saveConfig()
		if(config.saving.format != FormatType.POLAR) {
			println("[WARNING] Everything except polar is broken at the moment")
		}

		setupWorld()
		setupCommands()

		globalEventHandler.addListener(PlayerLoginEvent::class.java) {
			it.setSpawningInstance(buildInstance)
			it.player.respawnPoint = Pos(1.0, 43.0, 0.0)

			it.player.gameMode = GameMode.CREATIVE
			it.player.isFlying = true
			it.player.itemInMainHand = ItemStack.of(Material.STONE)
		}

		minecraftServer.start("0.0.0.0", 25565)
	}

	private fun setupWorld() {
		buildInstance.chunkLoader = config.saving.format.setupWorldLoader(Path.of(System.getProperty("user.dir"), "build${config.saving.format.fileNameExtension}"))
		buildInstance.saveChunksToStorage()

		buildInstance.setBlock(0, 42, 0, Block.STONE)

		MinecraftServer.getSchedulerManager().submitTask({
			println("Saving")
			buildInstance.saveChunksToStorage()

			return@submitTask TaskSchedule.minutes(5)
		}, ExecutionType.ASYNC)
		MinecraftServer.getSchedulerManager().buildShutdownTask {
			buildInstance.saveChunksToStorage()
		}
	}

	private fun setupCommands() {
		MinecraftServer.getCommandManager().register(SaveCommand(buildInstance))
	}

	private fun saveConfig() {
		val loader = YamlConfigurationLoader.builder()
			.path(Path.of(System.getProperty("user.dir"), "server.yml"))
			.defaultOptions {
				it.shouldCopyDefaults(true)
			}
			.indent(4)
			.nodeStyle(NodeStyle.BLOCK)
			.build()
		val node = loader.load()
		node.set(ServerConfig::class.java, config)
		loader.save(node)
	}
}