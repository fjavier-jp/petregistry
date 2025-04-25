package org.hopto.fjavierjp.petregistry.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UrlGenerator
{
    public static String generatePublicUrl(String basePath, String filename)
    {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(basePath)
            .path(filename)
            .toUriString();
    }
}