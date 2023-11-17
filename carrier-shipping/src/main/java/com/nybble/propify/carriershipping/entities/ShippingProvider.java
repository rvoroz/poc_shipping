package com.nybble.propify.carriershipping.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingProvider {
    private Long id;
    private String name;
	private String liveUrl;
	private String sandboxUrl;
	private String clientId;
	private String clientSecret;
	private String authLiveUrl;
	private String authSandboxUrl;
    private String truststore;
    private String truststorePassword;
}
