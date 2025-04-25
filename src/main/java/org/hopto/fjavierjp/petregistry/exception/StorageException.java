package org.hopto.fjavierjp.petregistry.exception;

public class StorageException extends RuntimeException
{
    public StorageException(String message)
    {
        super(message);
    }
    public StorageException(String message, Exception exception)
    {
        super(message, exception);
    }
}
