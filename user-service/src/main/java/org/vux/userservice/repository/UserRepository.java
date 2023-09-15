package org.vux.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vux.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
