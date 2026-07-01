package dev.mikablondo.imprint.store;

import dev.mikablondo.imprint.ImprintStore;
import dev.mikablondo.imprint.core.exception.ImprintException;
import dev.mikablondo.imprint.core.exception.ImprintError;
import dev.mikablondo.imprint.core.utils.UUIDUtils;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * {@link ImprintStore} implementation that stores the serialized object in a file,
 * and returns a short UUID as seed.
 */
@RequiredArgsConstructor
public class FileImprintStore implements ImprintStore {
    
    private final String baseDirectory;

    /**
     * {@inheritDoc}
     *
     * @implSpec Stores the binary data in a file and returns a unique key.
     */
    @Override
    public String save(byte[] data) {
        String key = UUIDUtils.generate();
        Path directory = Paths.get(baseDirectory);
        Path filePath = directory.resolve(key);
        
        try {
            Files.createDirectories(directory);
            Files.write(filePath, data);
            return key;
        } catch (IOException e) {
            throw new ImprintException(ImprintError.FILE_SAVE_FAILED, e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Retrieves the binary data from a file using the provided key.
     */
    @Override
    public byte[] load(String key) {
        Path filePath = Paths.get(baseDirectory, key);
        
        try {
            if (!Files.exists(filePath)) {
                throw new ImprintException(ImprintError.FILE_NOT_FOUND);
            }
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new ImprintException(ImprintError.FILE_LOAD_FAILED, e);
        }
    }
}
