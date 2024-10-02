package com.example.petstoremonolithique.Generated;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Entity
@Data
@Table(
        name = "\"petphotoss\""
)
public class PetPhotos {

    private List<PetPhoto> grpcPetPhoto;

    public PetPhotos(List<PetPhoto> grpcPetPhoto) {
        this.grpcPetPhoto = grpcPetPhoto;
    }

    public PetPhotos() {
    }

    @NotNull
    public static PetPhotos fromGrpc(@NotNull PetStore.GrpcPetPhotos petPhotos) {
        return new PetPhotos(
            petPhotos.getGrpcPetPhotoList().stream()
                .map(i -> PetPhoto.fromGrpc(i))
                .toList()
        );
    }

    @NotNull
    public PetStore.GrpcPetPhotos toGrpc() {
        PetStore.GrpcPetPhotos.Builder builder = PetStore.GrpcPetPhotos.newBuilder();
        builder.addAllGrpcPetPhoto(grpcPetPhoto.stream()
            .map(i -> i.toGrpc())
            .toList());
        return builder.build();
    }
}
