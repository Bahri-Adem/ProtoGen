package com.example.petstoremonolithique.Generated;

import java.lang.IllegalArgumentException;
import java.lang.Long;
import java.lang.String;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity
@Data
@Table(
        name = "\"users\""
)
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private String passwordHash;

    private String salt;

    private String phone;

    private UserStatus status;

    public User(
        Long id,
        String username,
        String firstname,
        String lastname,
        String email,
        String passwordHash,
        String salt,
        String phone,
        UserStatus status
    ) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.phone = phone;
        this.status = status;
    }

    public User() {
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    public void setFirstname(@Nullable String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(@Nullable String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public void setPasswordHash(@Nullable String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setSalt(@Nullable String salt) {
        this.salt = salt;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    public void setStatus(@Nullable UserStatus status) {
        this.status = status;
    }

    @NotNull
    public static User fromGrpc(@NotNull PetStore.GrpcUser user) {
        return new User(
            user.hasId() ? user.getId() : null,
            user.hasUsername() ? user.getUsername() : null,
            user.hasFirstname() ? user.getFirstname() : null,
            user.hasLastname() ? user.getLastname() : null,
            user.hasEmail() ? user.getEmail() : null,
            user.hasPasswordHash() ? user.getPasswordHash() : null,
            user.hasSalt() ? user.getSalt() : null,
            user.hasPhone() ? user.getPhone() : null,
            user.hasStatus() ? UserStatus.fromGrpc(user.getStatus()) : null
        );
    }

    @NotNull
    public PetStore.GrpcUser toGrpc() {
        PetStore.GrpcUser.Builder builder = PetStore.GrpcUser.newBuilder();
        if (id != null) {
            builder.setId(id);
        }
        if (username != null) {
            builder.setUsername(username);
        }
        if (firstname != null) {
            builder.setFirstname(firstname);
        }
        if (lastname != null) {
            builder.setLastname(lastname);
        }
        if (email != null) {
            builder.setEmail(email);
        }
        if (passwordHash != null) {
            builder.setPasswordHash(passwordHash);
        }
        if (salt != null) {
            builder.setSalt(salt);
        }
        if (phone != null) {
            builder.setPhone(phone);
        }
        if (status != null) {
            builder.setStatus(status.toGrpc());
        }
        return builder.build();
    }

    public enum UserStatus {

        Active,
        Inactive;

        public PetStore.GrpcUser.GrpcUserStatus toGrpc() {
            return switch (this) {
                case Active -> PetStore.GrpcUser.GrpcUserStatus.Active;
                case Inactive -> PetStore.GrpcUser.GrpcUserStatus.Inactive;
            };
        }

        public static UserStatus fromGrpc(PetStore.GrpcUser.GrpcUserStatus grpc) {
            return switch (grpc) {
                case Active -> UserStatus.Active;
                case Inactive -> UserStatus.Inactive;
                case UNRECOGNIZED -> throw new IllegalArgumentException("Enum value is not recognized");
            };
        }
    }
}
