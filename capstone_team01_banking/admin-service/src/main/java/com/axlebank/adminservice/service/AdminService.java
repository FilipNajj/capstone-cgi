package com.axlebank.adminservice.service;

import com.axlebank.adminservice.exceptions.AdminAlreadyPresentException;
import com.axlebank.adminservice.exceptions.AdminNotAvailableException;
import com.axlebank.adminservice.model.Address;
import com.axlebank.adminservice.model.Admin;
import com.axlebank.adminservice.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;


    public Admin addAdmin (Admin admin) throws AdminAlreadyPresentException {
        Optional<Admin> optionalAdmin = adminRepository.findById(admin.getAdminId());
        if(optionalAdmin.isPresent()){
            throw new AdminAlreadyPresentException();
        }
        Address address = admin.getAddress();
        if(address != null) {
            address.setAdmin(admin);
        }
        adminRepository.save(admin);
        return admin;
    }

    public List<Admin> getAllAdmin(){
        return adminRepository.findAll();
    }

    public  Admin updateAdmin(Admin updateAdmin) throws AdminNotAvailableException {
        Optional<Admin> optionalAdmin = adminRepository.findById(updateAdmin.getAdminId());
        if( optionalAdmin.isPresent()){
            Address address = updateAdmin.getAddress();
            if(address != null) {
                address.setAdmin(updateAdmin);
            }
            adminRepository.save(updateAdmin);
            return updateAdmin;
        }
        throw new AdminNotAvailableException();
    }

    public void deleteAdmin(int adminId) throws AdminNotAvailableException {
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if( optionalAdmin.isPresent()){
            adminRepository.deleteById(adminId);
        }else{
            throw new AdminNotAvailableException();
        }

    }
    public Admin findAdminById(int adminId) throws AdminNotAvailableException {
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if(optionalAdmin.isPresent()){
            return optionalAdmin.get();
        }
        throw new AdminNotAvailableException();

    }
}
