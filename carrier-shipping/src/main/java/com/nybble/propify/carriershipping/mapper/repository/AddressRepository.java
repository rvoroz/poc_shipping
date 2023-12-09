package com.nybble.propify.carriershipping.mapper.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.nybble.propify.carriershipping.entities.AddressRequest;
import com.nybble.propify.carriershipping.entities.AddressValidationResponse;
import com.nybble.propify.carriershipping.entities.Candidate;
import com.nybble.propify.carriershipping.entities.CandidateAddress;
import com.nybble.propify.carriershipping.entities.XAVResponseAddressValidation;
import com.nybble.propify.carriershipping.exception.AddressValidationException;
import com.nybble.propify.carriershipping.exception.AddressValidationRequestException;
import com.nybble.propify.carriershipping.provider.UPSProviderApi;

@Repository
public class AddressRepository {

    private final UPSProviderApi upsProviderApi;

    public AddressRepository(UPSProviderApi upsProviderApi) {
        this.upsProviderApi = upsProviderApi;
    }

    public AddressValidationResponse adddressValidate(AddressRequest addressRequest)
            throws AddressValidationException {

        if (validateRequest(addressRequest)) {
            throw new AddressValidationRequestException();
        }

        XAVResponseAddressValidation xavResponseAddressValidation = upsProviderApi.validateAddress(addressRequest);

        AddressValidationResponse addressValidationResponse = new AddressValidationResponse();

        Optional<List<Candidate>> candidateOptional = Optional
                .ofNullable(xavResponseAddressValidation.getXAVResponse().getCandidate());

        if (candidateOptional.isPresent()) {
            for (Candidate candidate : xavResponseAddressValidation.getXAVResponse().getCandidate()) {
                addressValidationResponse.getCandidates().add(CandidateAddress.builder()
                        .streetAddress(Objects.isNull(candidate.getAddressKeyFormat().getAddressLine())
                                || candidate.getAddressKeyFormat().getAddressLine().isEmpty() ? ""
                                        : candidate.getAddressKeyFormat().getAddressLine().get(0))
                        .countryCode(candidate.getAddressKeyFormat().getCountryCode())
                        .stateCode(candidate.getAddressKeyFormat().getPoliticalDivision1())
                        .city(candidate.getAddressKeyFormat().getPoliticalDivision2())
                        .postalCode(candidate.getAddressKeyFormat().getPostcodePrimaryLow())
                        .postalCodeExt(candidate.getAddressKeyFormat().getPostcodeExtendedLow())
                        .build());
            }
        }

        return addressValidationResponse;
    }

    private Boolean validateRequest(AddressRequest addressRequest) {
        Boolean cityIsBlank = Strings.isBlank(addressRequest.getCity());
        Boolean stateIsBlank = Strings.isBlank(addressRequest.getStateCode());
        Boolean postCodeIsBlank = Strings.isBlank(addressRequest.getPostalCode());

        return cityIsBlank && stateIsBlank && postCodeIsBlank;

    }

}
