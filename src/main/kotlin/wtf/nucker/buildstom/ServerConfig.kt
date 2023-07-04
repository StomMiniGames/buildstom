package wtf.nucker.buildstom

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
data class ServerConfig(
	val server: ServerConfig = ServerConfig(),
	val saving: SavingConfig = SavingConfig(),
	// TODO: Implement whitelist
//	val whitelist: List<String> = listOf("Notch")
) {
	@ConfigSerializable
	data class ServerConfig(
		val port: Int = 25565,
		val ip: String = "0.0.0.0",
	)

	@ConfigSerializable
	data class SavingConfig(
		@Comment("Formats = polar, tnt, anvil")
		val format: FormatType = FormatType.POLAR
	)
}
