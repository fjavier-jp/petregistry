package org.hopto.fjavierjp.petregistry.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.hopto.fjavierjp.petregistry.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService
{
    private final Path directory;

    public FileSystemStorageService(@Value("${storage.location:uploads/}") String directory)
    {
        try
        {
            // This a dynamic way to get the resource folder.
            Path resourcePath = Paths.get(getClass().getClassLoader().getResource("").toURI());
            this.directory = resourcePath.resolve("static").resolve(directory);
            log.info("Uploads path set");

            Files.createDirectories(this.directory);
            log.info("Directory created");
        }
        catch (URISyntaxException exception)
        {
            throw new StorageException("Unable to find uploads directory", exception);
        }
        catch (IOException exception)
        {
            throw new StorageException("Could not create uploads directory", exception);
        }
    }

    public Path getDirectory()
    {
        return this.directory;
    }

    @Override
    public String store(MultipartFile file) throws StorageException
    {
        if (file.isEmpty())
        {
            throw new StorageException("Cannot store empty file");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank())
        {
            throw new StorageException("File must have a valid name");
        }

        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == originalFilename.length() - 1)
        {
            throw new StorageException("File must have a valid extension");
        }

        String extension = originalFilename.substring(dotIndex);
        String uuidFilename = UUID.randomUUID().toString();
        String hashedFilename = uuidFilename + extension;
        Path destinationFile = this.directory.resolve(hashedFilename).normalize().toAbsolutePath();

        try
        {
            Files.copy(file.getInputStream(), destinationFile);
        }
        catch (IOException exception)
        {
            throw new StorageException("Failed to store file", exception);
        }

        return hashedFilename;
    }

    @Override
    public void delete(String filename) throws StorageException
    {
        try 
        {
            Files.delete(directory.resolve(filename));
        }
        catch (IOException exception)
        {
            throw new StorageException("Failed to delete file", exception);
        }
    }

    @Override
    public void deleteAll() throws StorageException
    {
        try
        {
            FileSystemUtils.deleteRecursively(directory);
            log.info("Directory recursively deleted");
        }
        catch (IOException exception)
        {
            throw new StorageException("Failed to delete directory recursively", exception);
        }
    }

    @Override
    public void reinit() throws StorageException
    {
        try
        {
            Files.createDirectories(this.directory);
        }
        catch (IOException exception)
        {
            throw new StorageException("Could not create uploads directory");
        }
    }
}
