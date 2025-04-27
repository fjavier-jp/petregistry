package org.hopto.fjavierjp.petregistry.unit.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hopto.fjavierjp.petregistry.util.UrlGenerator;

@ExtendWith(SpringExtension.class)
public class UrlGeneratorTests
{
    @Test
    public void testGeneratePublicUrl()
    {
        String pictureUrl = "https://example.com/uploads/images/pets/file.png";
        try (MockedStatic<ServletUriComponentsBuilder> mockedServlet = Mockito.mockStatic(ServletUriComponentsBuilder.class))
        {
            ServletUriComponentsBuilder builderMock = Mockito.mock(ServletUriComponentsBuilder.class);
            mockedServlet.when(() -> ServletUriComponentsBuilder.fromCurrentContextPath()).thenReturn(builderMock);
            Mockito.when(builderMock.path(Mockito.anyString())).thenReturn(builderMock);
            Mockito.when(builderMock.toUriString()).thenReturn(pictureUrl);
            String result = UrlGenerator.generatePublicUrl("");
            
            assertEquals(pictureUrl, result);
        }
    }
}
