package com.starwars.resistance.api.v1.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class ResourceUriHelper {

    private ResourceUriHelper() { }

    public static URI getUri(Object resourceId) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(resourceId)
            .toUri();
    }

}
