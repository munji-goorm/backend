package com.munjigoorm.backend.main.repository;

import com.munjigoorm.backend.main.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, String> {

    List<Address> findByFullAddrContaining(String keyword);
}