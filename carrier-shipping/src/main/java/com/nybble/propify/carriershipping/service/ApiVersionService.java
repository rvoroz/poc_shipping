package com.nybble.propify.carriershipping.service;

import com.nybble.propify.carriershipping.entities.ApiVersion;

public interface ApiVersionService {
    public ApiVersion getApiVersionById(Long id);

    public ApiVersion addApiVersion(ApiVersion apiVersion);
}
