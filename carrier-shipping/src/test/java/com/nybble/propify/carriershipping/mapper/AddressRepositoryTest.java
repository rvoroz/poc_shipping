package com.nybble.propify.carriershipping.mapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nybble.propify.carriershipping.entities.AddressKeyFormat;
import com.nybble.propify.carriershipping.entities.AddressRequest;
import com.nybble.propify.carriershipping.entities.AddressValidationResponse;
import com.nybble.propify.carriershipping.entities.Candidate;
import com.nybble.propify.carriershipping.entities.CandidateAddress;
import com.nybble.propify.carriershipping.entities.XAVResponse;
import com.nybble.propify.carriershipping.entities.XAVResponseAddressValidation;
import com.nybble.propify.carriershipping.mapper.repository.AddressRepository;
import com.nybble.propify.carriershipping.provider.UPSProviderApi;

@RunWith(SpringRunner.class)
public class AddressRepositoryTest {

    private AddressRepository addressRepository;

    @Mock
    private UPSProviderApi upsProviderApi;

    @Before
    public void setUp() {
        addressRepository = new AddressRepository(upsProviderApi);
    }

    @Test
    public void adddressValidate_whenAddressIsValid() throws JsonMappingException, JsonProcessingException {
        AddressKeyFormat addressKeyFormat = AddressKeyFormat.builder()
                .addressLine(new ArrayList<>(Arrays.asList("26601 ALISO CREEK ROAD", "STE D",
                        "ALISO VIEJO TOWN CENTER")))
                .countryCode("US")
                .politicalDivision1("CA")
                .politicalDivision2("ALISO VIEJO").build();
        Candidate candidate = Candidate.builder().addressKeyFormat(addressKeyFormat).build();
        List<Candidate> candidates = new ArrayList<>();
        candidates.add(candidate);
        XAVResponse xavResponse = XAVResponse.builder().candidate(candidates).build();
        XAVResponseAddressValidation xavResponseAddressValidation = XAVResponseAddressValidation.builder()
                .xAVResponse(xavResponse).build();
        when(upsProviderApi.validateAddress(any(AddressRequest.class))).thenReturn(xavResponseAddressValidation);

        List<String> addressLine = new ArrayList<>();
        addressLine.add("26601 ALISO CREEK ROAD");
        addressLine.add("STE D");
        addressLine.add("ALISO VIEJO TOWN CENTER");
        addressLine.add("CA");
        AddressRequest addressRequest = AddressRequest.builder()
                .addressLine(addressLine)
                .city("ALISO VIEJO")
                .state("CA").build();

        AddressValidationResponse addressValidationResponse = addressRepository.adddressValidate(addressRequest);

        Assert.assertNotNull(addressValidationResponse);
        Assert.assertEquals(1, addressValidationResponse.getCandidates().size());
        CandidateAddress candidateResult = addressValidationResponse.getCandidates().get(0);
        Assert.assertEquals("US", candidateResult.getCountryCode());
        Assert.assertEquals("CA", candidateResult.getStateCode());
        Assert.assertEquals("ALISO VIEJO", candidateResult.getCity());
      //  Assert.assertEquals(3, candidateResult.getAddressLine().size()); //TODO fix this
    }

}
