package com.shanzhu.em.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shanzhu.em.common.R;
import com.shanzhu.em.entity.Address;
import com.shanzhu.em.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
    }

    private String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAllUserAddress_WhenRecordsExist_ReturnsList() throws Exception {
        // Prepare test data
        Address addr1 = new Address();
        addr1.setLinkUser("John Doe");
        addr1.setLinkAddress("123 Main St, New York");
        addr1.setLinkPhone("555-0100");

        Address addr2 = new Address();
        addr2.setLinkUser("Jane Smith");
        addr2.setLinkAddress("456 Oak Ave, London");
        addr2.setLinkPhone("555-0200");

        when(addressService.list()).thenReturn(Arrays.asList(addr1, addr2));

        // Execute & Verify
        mockMvc.perform(get("/api/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].linkPhone").value("555-0100"))
                .andExpect(jsonPath("$.data[1].linkUser").value("Jane Smith"));
    }

    @Test
    void findAllUserAddress_WhenNoRecords_ReturnsEmptyList() throws Exception {
        when(addressService.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void saveAddress_WithValidData_ReturnsSuccess() throws Exception {
        Address address = new Address();
        address.setLinkUser("Test User");
        address.setLinkAddress("Test Address");
        address.setLinkPhone("555-0000");
        address.setUserId(100L);

        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(address)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(addressService).save(argThat(addr ->
                addr.getLinkUser().equals("Test User") &&
                        addr.getLinkPhone().equals("555-0000")
        ));
    }

    @Test
    void updateAddress_WithValidData_ReturnsSuccess() throws Exception {
        Address address = new Address();
        address.setId(2L);
        address.setLinkUser("Updated User");
        address.setLinkAddress("Updated Address");
        address.setLinkPhone("555-1111");

        mockMvc.perform(put("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(address)))
                .andExpect(status().isOk());

        verify(addressService).updateById(argThat(addr ->
                addr.getId().equals(2L) &&
                        addr.getLinkPhone().equals("555-1111")
        ));
    }

    @Test
    void deleteAddress_WithValidId_ReturnsSuccess() throws Exception {
        mockMvc.perform(delete("/api/address/3"))
                .andExpect(status().isOk());

        verify(addressService).removeById(eq(3L));
    }

}