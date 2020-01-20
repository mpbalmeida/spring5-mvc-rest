package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Vendor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VendorMapperTest {

    private static final String NAME = "Name";
    private static final Long ID = 1L;
    VendorMapper customerMapper = VendorMapper.INSTANCE;

    @Test
    public void vendorToVendorDTO() {
        // given
        Vendor vendor = new Vendor();
        vendor.setName(NAME);
        vendor.setId(ID);

        // when
        VendorDTO vendorDTO = customerMapper.vendorToVendorDTO(vendor);

        // then
        assertNotNull(vendorDTO);
        assertEquals(NAME, vendorDTO.getName());
        assertEquals(ID, vendorDTO.getId());
    }

    @Test
    public void vendorDtoToVendor() {
        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);
        vendorDTO.setId(ID);

        // when
        Vendor vendor = customerMapper.vendorDtoToVendor(vendorDTO);

        // then
        assertNotNull(vendor);
        assertEquals(NAME, vendor.getName());
        assertEquals(ID, vendor.getId());
    }
}