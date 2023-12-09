package com.nybble.propify.carriershipping.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.nybble.propify.carriershipping.model.CarrierShipment;
import com.nybble.propify.carriershipping.exception.CarrierLabelNotFoundException;
import com.nybble.propify.carriershipping.exception.PDFGenerationException;
import com.nybble.propify.carriershipping.mapper.CarrierShipmentMapper;
import com.nybble.propify.carriershipping.service.ShippingLabelService;

@Service
public class ShippingLabelServiceImpl implements ShippingLabelService {
    
    private final CarrierShipmentMapper carrierShipmentMapper;


    public ShippingLabelServiceImpl(CarrierShipmentMapper carrierShipmentMapper) {
        this.carrierShipmentMapper = carrierShipmentMapper;
    }

    @Override
    public ByteArrayOutputStream generateLabel(String id) {

        Optional<CarrierShipment> shipment = carrierShipmentMapper.getByLabelId(id);
        if(shipment.isPresent()){
            return generatePDF(shipment.get().getShippingLabelImage());
        }
        throw new CarrierLabelNotFoundException(id);
    }

    public ByteArrayOutputStream generatePDF(String imageString) {
        try {
            Document document = new Document(PageSize.LETTER);
            byte[] imageBytes = Base64.getDecoder().decode(imageString);
            Image image = Image.getInstance(imageBytes);
            image.scalePercent(25);
            image.getIndentationRight();
            image.setRotationDegrees(270);
            ByteArrayOutputStream pdfData = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, pdfData);
            document.open();
            document.add(image);
            document.close();

            return pdfData;
        } catch (Exception e) {
            throw new PDFGenerationException(e);
        }
    }
}
