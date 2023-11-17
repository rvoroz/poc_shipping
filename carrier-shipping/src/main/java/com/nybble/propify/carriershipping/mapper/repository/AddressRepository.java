package com.nybble.propify.carriershipping.mapper.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.nybble.propify.carriershipping.entities.AddressRequest;
import com.nybble.propify.carriershipping.entities.AddressValidationResponse;
import com.nybble.propify.carriershipping.entities.Candidate;
import com.nybble.propify.carriershipping.entities.CandidateAddress;
import com.nybble.propify.carriershipping.entities.XAVResponseAddressValidation;
import com.nybble.propify.carriershipping.exception.AddressValidationException;
import com.nybble.propify.carriershipping.provider.UPSProviderApi;

@Repository
public class AddressRepository {

    private UPSProviderApi upsProviderApi;

    
    public AddressRepository(UPSProviderApi upsProviderApi) {
        this.upsProviderApi = upsProviderApi;
    }

    public AddressValidationResponse adddressValidate(AddressRequest addressRequest)
            throws AddressValidationException {

        XAVResponseAddressValidation xavResponseAddressValidation = upsProviderApi.validateAddress(addressRequest);

        AddressValidationResponse addressValidationResponse = new AddressValidationResponse();

        Optional<List<Candidate>> candidateOptional = Optional
                .ofNullable(xavResponseAddressValidation.getXAVResponse().getCandidate());

        if (candidateOptional.isPresent()) {
            xavResponseAddressValidation.getXAVResponse().getCandidate().forEach(candidate -> {
                addressValidationResponse.getCandidates().add(CandidateAddress.builder()
                        .streetAddress(candidate.getAddressKeyFormat().getAddressLine().isEmpty() ? "" : candidate.getAddressKeyFormat().getAddressLine().get(0))
                        .additionalInfoAddress((candidate.getAddressKeyFormat().getAddressLine().size() > 1) ?
                                candidate.getAddressKeyFormat().getAddressLine().get(1) : "" )
                        .countryCode(candidate.getAddressKeyFormat().getCountryCode())
                        .stateCode(candidate.getAddressKeyFormat().getPoliticalDivision1())
                        .city(candidate.getAddressKeyFormat().getPoliticalDivision2())
                        .postalCode(candidate.getAddressKeyFormat().getPostcodePrimaryLow())
                        .postalCodeExt(candidate.getAddressKeyFormat().getPostcodeExtendedLow())
                        .build());
            });
        }

        return addressValidationResponse;
    }

}
