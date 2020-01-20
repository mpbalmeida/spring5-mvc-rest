package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controllers.RestResponseEntityExceptionHandler;
import guru.springfamework.services.ResourceNotFoundException;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest extends AbstractRestControllerTest {

    private static final String NAME_1 = "Name1";
    private static final Long ID_1 = 1L;
    private static final String NAME_2 = "Name2";
    private static final Long ID_2 = 2L;

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void testListVendors() throws Exception {
        // given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setId(ID_1);
        vendor1.setName(NAME_1);
        vendor1.setVendorUrl(VendorController.BASE_URL + "/" + ID_1);

        VendorDTO vendor2 = new VendorDTO();
        vendor2.setId(ID_2);
        vendor2.setName(NAME_2);
        vendor2.setVendorUrl(VendorController.BASE_URL + "/" + ID_2);
        // when
        when(vendorService.getAllVendors()).thenReturn(Arrays.asList(vendor1, vendor2));

        mockMvc.perform(get(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void testGetVendorById() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setId(ID_1);
        vendorDTO.setName(NAME_1);
        vendorDTO.setVendorUrl(VendorController.BASE_URL + "/" + ID_1);

        when(vendorService.getVendorById(anyLong())).thenReturn(vendorDTO);

        mockMvc.perform(
                get(VendorController.BASE_URL + "/" + ID_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME_1)));
    }

    @Test
    public void testCreateNewVendor() throws Exception {
        VendorDTO vendor = new VendorDTO();
        vendor.setName(NAME_1);

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setId(ID_1);
        returnDTO.setName(NAME_1);
        returnDTO.setVendorUrl(VendorController.BASE_URL + "/" + ID_1);

        when(vendorService.createNewVendor(vendor)).thenReturn(returnDTO);

        mockMvc.perform(
                post(VendorController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(NAME_1)))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "/" + ID_1)))
                .andExpect(jsonPath("$.id", equalTo(ID_1.intValue())));
    }

    @Test
    public void testUpdateVendor() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME_1);

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendorDTO.getName());
        returnDTO.setId(ID_1);
        returnDTO.setVendorUrl(VendorController.BASE_URL + "/" + ID_1);

        when(vendorService.saveVendorByDTO(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(
                put(VendorController.BASE_URL + "/" + ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME_1)))
                .andExpect(jsonPath("$.id", equalTo(ID_1.intValue())))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "/" + ID_1)));
    }

    @Test
    public void testPatchVendor() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setId(ID_1);
        returnDTO.setName(NAME_1);
        returnDTO.setVendorUrl(VendorController.BASE_URL + "/" + ID_1);

        when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(
                patch(VendorController.BASE_URL + "/" + ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME_1)))
                .andExpect(jsonPath("$.id", equalTo(ID_1.intValue())))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "/" + ID_1)));
    }

    @Test
    public void testDeleteVendor() throws Exception {
        mockMvc.perform(
                delete(VendorController.BASE_URL + "/" + ID_1))
                .andExpect(status().isOk());

        verify(vendorService, times(1)).deleteVendorById(ID_1);
    }

    @Test
    public void testNotFoundException() throws Exception {

        when(vendorService.getVendorById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(VendorController.BASE_URL + "/222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
