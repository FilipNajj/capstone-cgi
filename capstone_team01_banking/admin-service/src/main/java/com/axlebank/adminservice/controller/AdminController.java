package com.axlebank.adminservice.controller;

import com.axlebank.adminservice.exceptions.AdminAlreadyPresentException;
import com.axlebank.adminservice.exceptions.AdminNotAvailableException;
import com.axlebank.adminservice.model.Admin;
import com.axlebank.adminservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping()
    public ResponseEntity<?> addAdmin(@RequestBody Admin admin){
        ResponseEntity<?> responseEntity;
        try {
            Admin newAdmin = adminService.addAdmin(admin);
            responseEntity = new ResponseEntity<Admin>(newAdmin,HttpStatus.CREATED);
        } catch (AdminAlreadyPresentException e){
            responseEntity = new ResponseEntity<String>("Admin Id already exist",HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
    @GetMapping()
    public ResponseEntity<List<Admin>> getAllAdmin(){
        List<Admin> admins = adminService.getAllAdmin();
        return new ResponseEntity<List<Admin>>(admins, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable("id") int adminId,@RequestBody Admin admin){
        ResponseEntity<?> responseEntity;
        admin.setAdminId(adminId);
        try {
            adminService.updateAdmin(admin);
            responseEntity = new ResponseEntity<String>("Admin  updated successfully",HttpStatus.OK);
        }catch (AdminNotAvailableException e){
            responseEntity = new ResponseEntity<String>("Admin not present",HttpStatus.OK);
        }
        return responseEntity;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") int adminId){
        ResponseEntity<String> responseEntity;
        try {
            adminService.deleteAdmin(adminId);
            responseEntity = new ResponseEntity<String>("Admin Removed successfully",HttpStatus.OK);
        }catch (AdminNotAvailableException e){
            responseEntity = new ResponseEntity<String>("Admin With the id is not present",HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable("id") int adminId){
        ResponseEntity<?> responseEntity;
        try {
            Admin admin = adminService.findAdminById(adminId);
            responseEntity = new ResponseEntity<Admin>(admin ,HttpStatus.OK);

        }catch (AdminNotAvailableException e){
            responseEntity = new ResponseEntity<String>("Admin not found",HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }
}
