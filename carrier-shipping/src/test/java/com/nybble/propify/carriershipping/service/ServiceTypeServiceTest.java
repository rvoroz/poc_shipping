package com.nybble.propify.carriershipping.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nybble.propify.carriershipping.mapper.ServiceTypeMapper;
import com.nybble.propify.carriershipping.model.ServiceType;
import com.nybble.propify.carriershipping.service.impl.ServiceTypeService;

@RunWith(SpringRunner.class)
public class ServiceTypeServiceTest {

    private ServiceTypeService serviceTypeService;

    @MockBean
    private ServiceTypeMapper serviceTypeMapper;

    @Before
    public void setUp() {
        serviceTypeService = new ServiceTypeService(serviceTypeMapper);
    }

    @Test
    public void isServiceTypeAvailable_whenCodeIsOk_returnTrue() {
        ServiceType serviceType = new ServiceType();
        Optional<ServiceType> optionalServiceType = Optional.of(serviceType);
        when(serviceTypeMapper.getServiceType(anyString())).thenReturn(optionalServiceType);
        Boolean result = serviceTypeService.isServiceTypeAvailable("code1");

        verify(serviceTypeMapper, times(1)).getServiceType(anyString());
        Assert.assertEquals(true, result);
    }

    @Test
    public void isServiceTypeAvailable_whenCodeNotFound_returnFalse() {
        Optional<ServiceType> optionalServiceType = Optional.empty();
        when(serviceTypeMapper.getServiceType(anyString())).thenReturn(optionalServiceType);
        Boolean result = serviceTypeService.isServiceTypeAvailable("code1");

        verify(serviceTypeMapper, times(1)).getServiceType(anyString());
        Assert.assertEquals(false, result);
    }

}
