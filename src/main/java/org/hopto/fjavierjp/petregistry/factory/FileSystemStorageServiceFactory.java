package org.hopto.fjavierjp.petregistry.factory;

import org.hopto.fjavierjp.petregistry.service.FileSystemStorageService;
import org.hopto.fjavierjp.petregistry.service.StorageService;
import org.springframework.stereotype.Component;

@Component
public class FileSystemStorageServiceFactory implements StorageServiceFactory
{
    @Override
    public StorageService create(String directory)
    {
        return new FileSystemStorageService(directory);
    }
}
