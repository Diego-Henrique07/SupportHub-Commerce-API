package supporthub_commerce_api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supporthub_commerce_api.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String Email);
    Boolean existsByEmail(String email);
}
