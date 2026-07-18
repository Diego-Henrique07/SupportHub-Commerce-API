package supporthub_commerce_api.user;

import jakarta.persistence.*;
import lombok.*;
import supporthub_commerce_api.user.enumsUser.UserRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter
    @Setter
    @Column(nullable = false,unique = true)
    private String password;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Getter
    @Setter
    private LocalDateTime createdAt;
}
