package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        //create a user of given country. The originalIp of the user should be "countryCode.userId" and return the user. Note that right now user is not connected and thus connected would be false and maskedIp would be null
        //Note that the userId is created automatically by the repository layer
        User user = new User();
        user.setConnected(false);
        user.setUsername(username);
        user.setPassword(password);
        Country country = new Country();
        if (countryName.equals("IND") || countryName.equals("ind")){
            country.setCountryName(CountryName.IND);
            country.setCode("001");
            String currentIp = "001." + user.getId();
            user.setOriginalIp(currentIp);
        }
        else if(countryName.equals("AUS") || countryName.equals("aus")) {
            country.setCountryName(CountryName.AUS);
            country.setCode("003");
            String currentIp = "003." + user.getId();
            user.setOriginalIp(currentIp);
        }
        else if(countryName.equals("USA") || countryName.equals("usa")) {
            country.setCountryName(CountryName.USA);
            country.setCode("002");
            String currentIp = "002." + user.getId();
            user.setOriginalIp(currentIp);
        }
        else if(countryName.equals("CHI") || countryName.equals("chi")) {
            country.setCountryName(CountryName.CHI);
            country.setCode("004");
            String currentIp = "004." + user.getId();
            user.setOriginalIp(currentIp);
        }
        else if(countryName.equals("JPN") || countryName.equals("jpn")) {
            country.setCountryName(CountryName.JPN);
            country.setCode("005");
            String currentIp = "005." + user.getId();
            user.setOriginalIp(currentIp);
        }
        else{
            throw new Exception("Country not found");
        }
        user.setServiceProviderList(null);
        user.setOriginalCountry(country);
        countryRepository3.save(country);
        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {

        User user = userRepository3.findById(userId).get();
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();
        List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
        serviceProviderList.add(serviceProvider);
        user.setServiceProviderList(serviceProviderList);
        userRepository3.save(user);
        return user;
    }
}
