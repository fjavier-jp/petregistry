package org.hopto.fjavierjp.petregistry.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UrlGenerator
{
    public static String generatePublicUrl(String relativePath)
    {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(relativePath)
            .toUriString();
    }
}