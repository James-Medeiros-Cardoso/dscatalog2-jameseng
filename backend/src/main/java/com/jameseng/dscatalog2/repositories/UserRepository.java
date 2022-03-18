package com.jameseng.dscatalog2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jameseng.dscatalog2.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
