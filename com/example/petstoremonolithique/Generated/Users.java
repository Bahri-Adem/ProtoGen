package com.example.petstoremonolithique.Generated;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Entity
@Data
@Table(
        name = "\"userss\""
)
public class Users {

    private List<User> grpcUser;

    public Users(List<User> grpcUser) {
        this.grpcUser = grpcUser;
    }

    public Users() {
    }

    @NotNull
    public static Users fromGrpc(@NotNull PetStore.GrpcUsers users) {
        return new Users(
            users.getGrpcUserList().stream()
                .map(i -> User.fromGrpc(i))
                .toList()
        );
    }

    @NotNull
    public PetStore.GrpcUsers toGrpc() {
        PetStore.GrpcUsers.Builder builder = PetStore.GrpcUsers.newBuilder();
        builder.addAllGrpcUser(grpcUser.stream()
            .map(i -> i.toGrpc())
            .toList());
        return builder.build();
    }
}
