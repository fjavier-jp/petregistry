package org.hopto.fjavierjp.petregistry.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;

import org.hopto.fjavierjp.petregistry.exception.StorageException;
import org.hopto.fjavierjp.petregistry.service.FileSystemStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

public class FileSystemStorageServiceTests
{
    private FileSystemStorageService service;

    public FileSystemStorageServiceTests()
    {
        this.service = new FileSystemStorageService("");
    }

    @BeforeEach
    public void setUp()
    {
        this.service.deleteAll();
        this.service.reinit();
    }

    @Test
    public void testStoreEmptyFile()
    {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]);
        assertThrows(StorageException.class, () -> this.service.store(file));
    }

    @Test
    public void testStoreInvalidFilename()
    {
        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", "Test content".getBytes());
        assertThrows(StorageException.class, () -> this.service.store(file));
    }

    @Test
    public void testStoreNoExtension()
    {
        MockMultipartFile file = new MockMultipartFile("file", "test", "text/plain", "Test content".getBytes());
        assertThrows(StorageException.class, () -> this.service.store(file));
    }

    @Test
    public void testStoreInvalidExtension()
    {
        MockMultipartFile file = new MockMultipartFile("file", "test.", "text/plain", "Test content".getBytes());
        assertThrows(StorageException.class, () -> this.service.store(file));
    }

    @Test
    public void testStore() throws IOException
    {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        String filename = this.service.store(file);

        assertTrue(Files.list(this.service.getDirectory()).count() == 1);

        this.service.delete(filename);
    }

    @Test
    public void testDeleteFileNotFound()
    {
        assertThrows(StorageException.class, () -> this.service.delete("test.txt"));
    }

    @Test
    public void testDelete() throws IOException
    {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());
        this.service.delete(this.service.store(file));

        assertEquals(Files.list(this.service.getDirectory()).count(), 0);
    }
}
