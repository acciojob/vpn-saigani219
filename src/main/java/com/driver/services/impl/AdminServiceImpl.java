package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        Admin admin = adminRepository1.findById(adminId).get();
        List<ServiceProvider> adminServiceProviders = admin.getServiceProviders();
//        for(ServiceProvider serviceProvider : serviceProviderRepository1.findAll()){
//            if(serviceProvider.getName().equals(providerName)) {
//                adminServiceProviders.add(serviceProvider);
//                admin.setServiceProviders(adminServiceProviders);
//                adminRepository1.save(admin);
//                return admin;
//            }
//        }
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(providerName);
        adminServiceProviders.add(serviceProvider);
        admin.setServiceProviders(adminServiceProviders);
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception {
        //add a country under the serviceProvider and return respective service provider
        //country name would be a 3-character string out of ind, aus, usa, chi, jpn. Each character can be in uppercase or lowercase. You should create a new Country object based on the given country name and add it to the country list of the service provider. Note that the user attribute of the country in this case would be null.
        //In case country name is not amongst the above mentioned strings, throw "Country not found" exception
        Country country = new Country();
        if (countryName.equals("IND") || countryName.equals("ind")){
            country.setCountryName(CountryName.IND);
            country.setCode("001");
        }
        else if(countryName.equals("AUS") || countryName.equals("aus")) {
        country.setCountryName(CountryName.AUS);
            country.setCode("003");
        }
        else if(countryName.equals("USA") || countryName.equals("usa")) {
            country.setCountryName(CountryName.USA);
            country.setCode("002");
        }
        else if(countryName.equals("CHI") || countryName.equals("chi")) {
            country.setCountryName(CountryName.CHI);
            country.setCode("004");
        }
        else if(countryName.equals("JPN") || countryName.equals("jpn")) {
            country.setCountryName(CountryName.JPN);
            country.setCode("005");
        }
        else{
            throw new Exception("Country not found");
        }
        country.setUser(null);
        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
        List<Country> countryServiceProvider = serviceProvider.getCountryList();
        countryServiceProvider.add(country);
        serviceProvider.setCountryList(countryServiceProvider);
        serviceProviderRepository1.save(serviceProvider);
        return serviceProvider;
    }
}
