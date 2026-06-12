package dev.mikablondo.imprint;

/**
 * Exception class for handling errors related to the Imprint encoding and decoding processes.
 * This exception can be thrown when serialization, compression, Base64 encoding/decoding,
 * or any other step in the Imprint process fails.
 */
public class ImprintException extends RuntimeException {
    public ImprintException(String message) {
        super("** Imprint Lib ** - " + message);
    }
}
