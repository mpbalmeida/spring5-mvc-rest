package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VendorServiceImplTest {

    private static final String NAME = "Name 1";
    private static final String NAME_2 = "Name 2";
    private static final Long ID = 1L;
    private static final Long ID_2 = 2L;
    private static final String URL = VendorController.BASE_URL + "/" + ID;

    @Mock
    VendorRepository vendorRepository;

    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    VendorService vendorService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vendorService = new VendorServiceImpl(vendorRepository, vendorMapper);
    }

    @Test
    public void getAllVendors() {
        // given
        Vendor vendor = new Vendor();
        vendor.setName(NAME);
        vendor.setId(ID);

        Vendor vendor2 = new Vendor();
        vendor2.setName(NAME_2);
        vendor2.setId(ID_2);

        when(vendorRepository.findAll()).thenReturn(Arrays.asList(vendor, vendor2));

        List<VendorDTO> allVendors = vendorService.getAllVendors();

        assertNotNull(allVendors);
        assertEquals(2, allVendors.size());
    }

    @Test
    public void getVendorById() {
        // given
        Vendor vendor = new Vendor();
        vendor.setName(NAME);
        vendor.setId(ID);

        when(vendorRepository.findById(ID)).thenReturn(Optional.of(vendor));

        VendorDTO vendorById = vendorService.getVendorById(ID);

        assertNotNull(vendorById);
        assertEquals(ID, vendorById.getId());
        assertEquals(NAME, vendorById.getName());
        assertEquals(URL, vendorById.getVendorUrl());
    }

    @Test
    public void createNewVendor() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setId(ID);
        savedVendor.setName(NAME);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        VendorDTO newVendor = vendorService.createNewVendor(vendorDTO);

        assertNotNull(newVendor);
        assertEquals(ID, newVendor.getId());
        assertEquals(URL, newVendor.getVendorUrl());
    }

    @Test
    public void saveVendorByDTO() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setId(ID);
        savedVendor.setName(NAME);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        VendorDTO newVendor = vendorService.saveVendorByDTO(ID, vendorDTO);

        assertNotNull(newVendor);
        assertEquals(ID, newVendor.getId());
        assertEquals(URL, newVendor.getVendorUrl());
    }

    @Test
    public void deleteVendorById() {

        vendorService.deleteVendorById(ID);

        verify(vendorRepository, times(1)).deleteById(ID);
    }
}