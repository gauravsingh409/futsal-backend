package com.codewithgaurav.store.services;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewithgaurav.store.exception.ResourceNotFoundException;
import com.codewithgaurav.store.model.Address;
import com.codewithgaurav.store.model.BankAccount;
import com.codewithgaurav.store.model.OwnerModel;
import com.codewithgaurav.store.repository.profile.OwnerCompleteProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OwnerCompleteProfileService {

    @Autowired
    OwnerCompleteProfileRepository repo;

    @Autowired
    ObjectMapper objectMapper;

    public String findDetailsById(String id) throws JsonProcessingException {
        OwnerModel owner = repo.findById(id)
                // .get();
                .orElseThrow(() -> new ResourceNotFoundException("owner", "id", id));
        // System.out.println(owner); // it will give the java object memory reference
        // ObjectMapper translate the java object to the json and json to java object.
        return objectMapper.writeValueAsString(owner);
    }

    public OwnerModel updateOwnerDetails(String id, OwnerModel request) {
        OwnerModel existingOwner = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", "id", id));

        // update field
        // FullName
        if (request.getFullName() != null && !request.getFullName().isEmpty()) existingOwner.setFullName(request.getFullName());

        // Email
        if (request.getEmail() != null && !request.getEmail().isEmpty()) existingOwner.setEmail(request.getEmail());

        //  Date Of Birth
        if (request.getDateOfBirth() != null) existingOwner.setDateOfBirth(request.getDateOfBirth());

        // Emergency Number
        if (request.getEmergencyContact() != null && !request.getEmergencyContact().isEmpty()) existingOwner.setEmergencyContact(request.getEmergencyContact());

        //  Profile Picture
        if (request.getProfileImageUrl() != null && !request.getProfileImageUrl().isEmpty()) {
            String oldImagePath = System.getProperty("user.dir") + existingOwner.getProfileImageUrl();
            File oldFile = new File(oldImagePath);
            if (oldFile.exists()) {
                oldFile.delete();
            }
            existingOwner.setProfileImageUrl(request.getProfileImageUrl());
        }

        // Address
        if (request.getAddress() != null) {
            Address existingAddress = existingOwner.getAddress();

            if (existingAddress == null) {
                existingAddress = new Address();
                existingOwner.setAddress(existingAddress);
            }

            Address requestAddress = request.getAddress();

            if (requestAddress.getStreet() != null && !requestAddress.getStreet().isEmpty()) {
                existingAddress.setStreet(requestAddress.getStreet());
            }

            if (requestAddress.getCity() != null && !requestAddress.getCity().isEmpty()) {
                existingAddress.setCity(requestAddress.getCity());
            }

            if (requestAddress.getProvince() != null && !requestAddress.getProvince().isEmpty()) {
                existingAddress.setProvince(requestAddress.getProvince());
            }

            if (requestAddress.getCountry() != null && !requestAddress.getCountry().isEmpty()) {
                existingAddress.setCountry(requestAddress.getCountry());
            }

            if (requestAddress.getPostalCode() != null && !requestAddress.getPostalCode().isEmpty()) {
                existingAddress.setPostalCode(requestAddress.getPostalCode());
            }
        }

        // for bank account
        if (request.getBankAccounts() != null && !request.getBankAccounts().isEmpty()) {
            List<BankAccount> existingAccounts = existingOwner.getBankAccounts();
            if (existingAccounts == null) {
                existingOwner.setBankAccounts(request.getBankAccounts());
            } else {
                for (BankAccount newAccount : request.getBankAccounts()) {
                    boolean found = false;
                    for (BankAccount existingAccount : existingAccounts) {
                        if (existingAccount.getAccountNumber().equals(newAccount.getAccountNumber())) {
                            // Update fields selectively
                            if (newAccount.getAccountHolderName() != null && !newAccount.getAccountHolderName().isEmpty()) {
                                existingAccount.setAccountHolderName(newAccount.getAccountHolderName());
                            }
                            if (newAccount.getBankName() != null && !newAccount.getBankName().isEmpty()) {
                                existingAccount.setBankName(newAccount.getBankName());
                            }
                            if (newAccount.getBranch() != null && !newAccount.getBranch().isEmpty()) {
                                existingAccount.setBranch(newAccount.getBranch());
                            }
                            if (newAccount.getAccountType() != null) {
                                existingAccount.setAccountType(newAccount.getAccountType());
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        // If new account not found in existing list, add it
                        existingAccounts.add(newAccount);
                    }
                }
            }
        }
        // save
        return repo.save(existingOwner);
    }

}
