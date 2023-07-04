package wtf.nucker.buildstom

import dev.emortal.tnt.TNTLoader
import dev.emortal.tnt.source.FileTNTSource
import net.hollowcube.polar.PolarLoader
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.IChunkLoader
import java.nio.file.Path

enum class FormatType {

	POLAR {
		override val fileNameExtension: String
			get() = ".polar"
		override fun setupWorldLoader(path: Path): IChunkLoader {
			return PolarLoader(path)
		}
	},
	TNT {
		override val fileNameExtension: String
			get() = ".tnt"
		override fun setupWorldLoader(path: Path): IChunkLoader {
			return TNTLoader(FileTNTSource(path))
		}
	},
	ANVIL {
		override val fileNameExtension: String
			get() = "/"
		override fun setupWorldLoader(path: Path): IChunkLoader {
			return AnvilLoader(path)
		}
	};

	abstract fun setupWorldLoader(path: Path): IChunkLoader
	abstract val fileNameExtension: String
}