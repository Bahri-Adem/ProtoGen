package com.example.petstoremonolithique.Generated;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Entity
@Data
@Table(
        name = "\"petss\""
)
public class Pets {

    private List<Pet> grpcPet;

    public Pets(List<Pet> grpcPet) {
        this.grpcPet = grpcPet;
    }

    public Pets() {
    }

    @NotNull
    public static Pets fromGrpc(@NotNull PetStore.GrpcPets pets) {
        return new Pets(
            pets.getGrpcPetList().stream()
                .map(i -> Pet.fromGrpc(i))
                .toList()
        );
    }

    @NotNull
    public PetStore.GrpcPets toGrpc() {
        PetStore.GrpcPets.Builder builder = PetStore.GrpcPets.newBuilder();
        builder.addAllGrpcPet(grpcPet.stream()
            .map(i -> i.toGrpc())
            .toList());
        return builder.build();
    }
}
