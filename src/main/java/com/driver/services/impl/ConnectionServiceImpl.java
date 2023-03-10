package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        //Connect the user to a vpn by considering the following priority order.
        //1. If the user is already connected to any service provider, throw "Already connected" exception.
        //2. Else if the countryName corresponds to the original country of the user, do nothing. This means that the user wants to connect to its original country, for which we do not require a connection. Thus, return the user as it is.
        //3. Else, the user should be subscribed under a serviceProvider having option to connect to the given country.
        //If the connection can not be made (As user does not have a serviceProvider or serviceProvider does not have given country, throw "Unable to connect" exception.
        //Else, establish the connection where the maskedIp is "updatedCountryCode.serviceProviderId.userId" and return the updated user. If multiple service providers allow you to connect to the country, use the service provider having smallest id.
//        User user = userRepository2.findById(userId).get();
//        if(user.getConnected() == true)
//            throw new Exception("Already connected");
//        String userCountry = user.getOriginalCountry().getCode();
//        String givenCountry = "";
//        if(countryName.equals("IND") || countryName.equals("ind"))
//            givenCountry = "001";
//        else if(countryName.equals("USA") || countryName.equals("usa"))
//            givenCountry = "002";
//        else if(countryName.equals("AUS") || countryName.equals("aus"))
//            givenCountry = "003";
//        else if(countryName.equals("CHI") || countryName.equals("chi"))
//            givenCountry = "004";
//        else if(countryName.equals("JPN") || countryName.equals("jpn"))
//            givenCountry = "005";
//        if(userCountry.equals(givenCountry)){
//            return user;
//        }
//        else {
//            if(checkSubscribed(user, givenCountry) == -1)
//                throw new Exception("Unable to connect");
//            else {
//                user.setConnected(true);
//                String newIp = givenCountry + "." + checkSubscribed(user, givenCountry) + "." + userId;
//                user.setMaskedIp(newIp);
//            }
//        }
//        userRepository2.save(user);
//        return user;
//    }
//
//    public Integer checkSubscribed(User user, String givenCountry){
//        List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
//        if(serviceProviderList == null)
//            return -1;
//        List<Integer> serviceProviderIds = new ArrayList<>();
//        for(ServiceProvider serviceProvider : serviceProviderList){
//            for(Country country : serviceProvider.getCountryList()){
//                if(country.getCode().equals(givenCountry)){
//                    serviceProviderIds.add(serviceProvider.getId());
//                    break;
//                }
//            }
//        }
//        if(serviceProviderIds.size() == 0)
//        return -1;
//        Collections.sort(serviceProviderIds);
//        return serviceProviderIds.get(0);
        User user = userRepository2.findById(userId).get();
        if(user.getMaskedIp()!=null){
            throw new Exception("Already connected");
        }
        else if(countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())){
            return user;
        }
        else {
            if (user.getServiceProviderList()==null){
                throw new Exception("Unable to connect");
            }

            List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
            int a = Integer.MAX_VALUE;
            ServiceProvider serviceProvider = null;
            Country country =null;

            for(ServiceProvider serviceProvider1:serviceProviderList){

                List<Country> countryList = serviceProvider1.getCountryList();

                for (Country country1: countryList){

                    if(countryName.equalsIgnoreCase(country1.getCountryName().toString()) && a > serviceProvider1.getId() ){
                        a=serviceProvider1.getId();
                        serviceProvider=serviceProvider1;
                        country=country1;
                    }
                }
            }
            if (serviceProvider!=null){
                Connection connection = new Connection();
                connection.setUser(user);
                connection.setServiceProvider(serviceProvider);

                String cc = country.getCode();
                int givenId = serviceProvider.getId();
                String mask = cc+"."+givenId+"."+userId;

                user.setMaskedIp(mask);
                user.setConnected(true);
                user.getConnectionList().add(connection);

                serviceProvider.getConnectionList().add(connection);

                userRepository2.save(user);
                serviceProviderRepository2.save(serviceProvider);


            }
        }
        return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user = userRepository2.findById(userId).get();
        if(user.getMaskedIp() == null)
            throw new Exception("Already disconnected");
            user.setMaskedIp(null);
            user.setConnected(false);
            userRepository2.save(user);
            return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        //Establish a connection between sender and receiver users
        //To communicate to the receiver, sender should be in the current country of the receiver.
        //If the receiver is connected to a vpn, his current country is the one he is connected to.
        //If the receiver is not connected to vpn, his current country is his original country.
        //The sender is initially not connected to any vpn. If the sender's original country does not match receiver's current country, we need to connect the sender to a suitable vpn. If there are multiple options, connect using the service provider having smallest id
        //If the sender's original country matches receiver's current country, we do not need to do anything as they can communicate. Return the sender as it is.
        //If communication can not be established due to any reason, throw "Cannot establish communication" exception
//        User sender = userRepository2.findById(senderId).get();
//        User receiver = userRepository2.findById(receiverId).get();
//        String senderCode = sender.getOriginalCountry().getCode();
//        String receiverCode = receiver.getOriginalCountry().getCode();
//        if(!receiver.getConnected() && receiverCode.equals(senderCode)){
//           return sender;
//        }
//        else if(receiver.getConnected()){
//            String currentCountry = receiver.getMaskedIp().substring(0,3);
//            if(senderCode.equals(currentCountry))
//                return sender;
//        }
//        else if(!receiver.getConnected() && !receiverCode.equals(senderCode)){
//            if(checkSubscribed(sender, receiverCode) == -1)
//                throw new Exception("Cannot establish communication");
//            sender.setConnected(true);
//            String newIp = receiverCode + "." + checkSubscribed(sender, receiverCode) + "." + senderId;
//            sender.setMaskedIp(newIp);
//            userRepository2.save(sender);
//            return sender;
//        }
//        else if(receiver.getConnected() && !receiverCode.equals(senderCode)){
//            String currCountryCode = receiver.getMaskedIp().substring(0,3);
//            if(checkSubscribed(sender, currCountryCode) == -1)
//                throw new Exception("Cannot establish communication");
//            sender.setConnected(true);
//            String newIp = receiverCode + "." + checkSubscribed(sender, currCountryCode) + "." + senderId;
//            sender.setMaskedIp(newIp);
//            userRepository2.save(sender);
//            return sender;
//        }
//        return null;
        User user = userRepository2.findById(senderId).get();
        User user1 = userRepository2.findById(receiverId).get();

        if(user1.getMaskedIp()!=null){
            String str = user1.getMaskedIp();
            String cc = str.substring(0,3); //chopping country code = cc

            if(cc.equals(user.getOriginalCountry().getCode()))
                return user;
            else {
                String countryName = "";

                if (cc.equalsIgnoreCase(CountryName.IND.toCode()))
                    countryName = CountryName.IND.toString();
                if (cc.equalsIgnoreCase(CountryName.USA.toCode()))
                    countryName = CountryName.USA.toString();
                if (cc.equalsIgnoreCase(CountryName.JPN.toCode()))
                    countryName = CountryName.JPN.toString();
                if (cc.equalsIgnoreCase(CountryName.CHI.toCode()))
                    countryName = CountryName.CHI.toString();
                if (cc.equalsIgnoreCase(CountryName.AUS.toCode()))
                    countryName = CountryName.AUS.toString();

                User user2 = connect(senderId,countryName);
                if (!user2.getConnected()){
                    throw new Exception("Cannot establish communication");

                }
                else return user2;
            }

        }
        else{
            if(user1.getOriginalCountry().equals(user.getOriginalCountry())){
                return user;
            }
            String countryName = user1.getOriginalCountry().getCountryName().toString();
            User user2 =  connect(senderId,countryName);
            if (!user2.getConnected()){
                throw new Exception("Cannot establish communication");
            }
            else return user2;

        }
    }
}
