package com.nybble.propify.carriershipping.service;

import java.io.ByteArrayOutputStream;

public interface ShippingLabelService {

    ByteArrayOutputStream generateLabel(String id);
}