package ldr.server.storage;

import java.nio.file.Path;

public record Config(Path location, int VECTOR_LEN) {
}
