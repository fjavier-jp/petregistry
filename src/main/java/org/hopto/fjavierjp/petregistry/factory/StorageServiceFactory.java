package org.hopto.fjavierjp.petregistry.factory;

import org.hopto.fjavierjp.petregistry.service.StorageService;

public interface StorageServiceFactory
{
    public StorageService create(String directory);
}
