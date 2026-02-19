//package com.srinath.attendance.config;
//
//import com.srinath.attendance.entity.Role;
//import com.srinath.attendance.repository.RoleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DataInitializer implements CommandLineRunner {
//
//    private final RoleRepository roleRepository;
//
//    @Override
//    public void run(String... args) {
//
//        if (roleRepository.findByName("EMPLOYEE").isEmpty()) {
//            roleRepository.save(Role.builder().name("EMPLOYEE").build());
//        }
//
//        if (roleRepository.findByName("MANAGER").isEmpty()) {
//            roleRepository.save(Role.builder().name("MANAGER").build());
//        }
//    }
//}
