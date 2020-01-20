package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.bootstrap.Bootstrap;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VendorServiceImplIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    VendorRepository vendorRepository;

    VendorService vendorService;

    @Before
    public void setUp() throws Exception {
        Bootstrap bootstrap = new Bootstrap(categoryRepository, customerRepository, vendorRepository);
        bootstrap.run(); //load data

        vendorService = new VendorServiceImpl(vendorRepository, VendorMapper.INSTANCE);
    }

    @Test
    public void patchEmptyUpdate() throws Exception {
        long id = getVendorIdValue();

        Vendor originalCustomer = vendorRepository.getOne(id);
        assertNotNull(originalCustomer);
        //save original first name
        String originalName = originalCustomer.getName();

        VendorDTO customerDTO = new VendorDTO();

        vendorService.patchVendor(id, customerDTO);

        Vendor updatedCustomer = vendorRepository.findById(id).get();

        assertNotNull(updatedCustomer);
        assertEquals(originalName, updatedCustomer.getName());
    }

    @Test
    public void patchVendorUpdateName() throws Exception {
        String updatedName = "UpdatedName";
        long id = getVendorIdValue();

        Vendor originalCustomer = vendorRepository.getOne(id);
        assertNotNull(originalCustomer);
        //save original first name
        String originalName = originalCustomer.getName();

        VendorDTO customerDTO = new VendorDTO();
        customerDTO.setName(updatedName);

        vendorService.patchVendor(id, customerDTO);

        Vendor updatedCustomer = vendorRepository.findById(id).get();

        assertNotNull(updatedCustomer);
        assertEquals(updatedName, updatedCustomer.getName());
        assertThat(originalName, not(equalTo(updatedCustomer.getName())));
    }

    private Long getVendorIdValue() {
        List<Vendor> vendors = vendorRepository.findAll();

        System.out.println("Customers Found: " + vendors.size());

        //return first id
        return vendors.get(0).getId();
    }
}