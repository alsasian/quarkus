package io.quarkus.deployment.dev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class IsolatedRemoteDevModeMainTest {

    @Test
    void createHashes() throws IOException {
        Path rootPath = Files.createTempDirectory(Paths.get("target"), "rootPath");
        rootPath.toFile().deleteOnExit();

        Path subDir = Files.createTempDirectory(rootPath, "subDir");
        subDir.toFile().deleteOnExit();

        Path subFile = Files.createTempFile(subDir, "subFile", null);
        subFile.toFile().deleteOnExit();

        Path subFileSoftLink = Files.createSymbolicLink(subDir.resolve("subFileSoftLink"), subFile.getFileName());
        subFileSoftLink.toFile().deleteOnExit();

        Path subFileHardLink = Files.createLink(subDir.resolve("subFileHardLink"), subFile.toAbsolutePath());
        subFileHardLink.toFile().deleteOnExit();

        Path subDirSoftLink = Files.createSymbolicLink(rootPath.resolve("subDirSoftLink"), subDir.getFileName());
        subDirSoftLink.toFile().deleteOnExit();

        Path subDirSoftLinkBroken = Files.createSymbolicLink(rootPath.resolve("subDirSoftLinkBroken"), Paths.get("missing"));
        subDirSoftLinkBroken.toFile().deleteOnExit();

        try {
            Map<String, String> hashes = IsolatedRemoteDevModeMain.createHashes(rootPath);
            assertEquals(3, hashes.size());
        } catch (IOException e) {
            fail("Should not fail", e);
        }
    }

}
