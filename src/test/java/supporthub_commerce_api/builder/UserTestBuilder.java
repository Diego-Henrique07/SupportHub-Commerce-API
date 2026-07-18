package supporthub_commerce_api.builder;

import supporthub_commerce_api.user.User;
import supporthub_commerce_api.user.enumsUser.UserRole;

public class UserTestBuilder {

    private Long id = 1L;
    private String name = "Test USer";
    private String email = "test@gmail.com";
    private String password = "encoded-Password";
    private UserRole role = UserRole.USER;

    private UserTestBuilder() {
    }

    public static UserTestBuilder aUser() {
        return new UserTestBuilder();
    }

    public UserTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserTestBuilder withRole(UserRole role) {
        this.role = role;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}
