package org.hopto.fjavierjp.petregistry.service;

import java.nio.file.Path;

import org.hopto.fjavierjp.petregistry.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService
{
    /**
     * Stores the given file.
     * @param file
     * @return Name of the stored file.
     */
    public String store(MultipartFile file) throws StorageException;

    /**
     * Deletes a specific file.
     * @param file
     * @return void
     */
    public void delete(String filename) throws StorageException;

    /**
     * Deletes all data under a certain location.
     * @return void
     */
    public void deleteAll() throws StorageException;

    /**
     * Reinitializes storage
     * @return void
     */
    public void reinit() throws StorageException;

    /**
     * Returns storage service absolute path to its images location.
     * @return
     */
    public Path getAbsolutePath();
}
