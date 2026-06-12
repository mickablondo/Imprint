package dev.mikablondo.imprint.exception;

/**
 * Exception class for handling errors related to the Imprint encoding and decoding processes.
 * This exception can be thrown when serialization, compression, Base64 encoding/decoding,
 * or any other step in the Imprint process fails.
 */
public class ImprintException extends RuntimeException {
    public ImprintException(ImprintError error) {
        super("[imprint] " + error.getMessage());
    }

    public ImprintException(ImprintError error, Throwable cause) {
        super("[imprint] " + error.getMessage(), cause);
    }
}
