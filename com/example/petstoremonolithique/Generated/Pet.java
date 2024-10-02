package com.example.petstoremonolithique.Generated;

import java.lang.IllegalArgumentException;
import java.lang.Long;
import java.lang.String;
import java.util.List;
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
        name = "\"pets\""
)
public class Pet {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    private String name;

    private PetCategory category;

    private List<String> photoUrls;

    private String tags;

    private PetStatus status;

    public Pet(
        Long id,
        String name,
        PetCategory category,
        List<String> photoUrls,
        String tags,
        PetStatus status
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.photoUrls = photoUrls;
        this.tags = tags;
        this.status = status;
    }

    public Pet() {
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setCategory(@Nullable PetCategory category) {
        this.category = category;
    }

    public void setTags(@Nullable String tags) {
        this.tags = tags;
    }

    public void setStatus(@Nullable PetStatus status) {
        this.status = status;
    }

    @NotNull
    public static Pet fromGrpc(@NotNull PetStore.GrpcPet pet) {
        return new Pet(
            pet.hasId() ? pet.getId() : null,
            pet.hasName() ? pet.getName() : null,
            pet.hasCategory() ? PetCategory.fromGrpc(pet.getCategory()) : null,
            pet.getPhotoUrlsList(),
            pet.hasTags() ? pet.getTags() : null,
            pet.hasStatus() ? PetStatus.fromGrpc(pet.getStatus()) : null
        );
    }

    @NotNull
    public PetStore.GrpcPet toGrpc() {
        PetStore.GrpcPet.Builder builder = PetStore.GrpcPet.newBuilder();
        if (id != null) {
            builder.setId(id);
        }
        if (name != null) {
            builder.setName(name);
        }
        if (category != null) {
            builder.setCategory(category.toGrpc());
        }
        builder.addAllPhotoUrls(photoUrls);
        if (tags != null) {
            builder.setTags(tags);
        }
        if (status != null) {
            builder.setStatus(status.toGrpc());
        }
        return builder.build();
    }

    public enum PetCategory {

        Dog,
        Cat,
        Bunny;

        public PetStore.GrpcPet.GrpcPetCategory toGrpc() {
            return switch (this) {
                case Dog -> PetStore.GrpcPet.GrpcPetCategory.Dog;
                case Cat -> PetStore.GrpcPet.GrpcPetCategory.Cat;
                case Bunny -> PetStore.GrpcPet.GrpcPetCategory.Bunny;
            };
        }

        public static PetCategory fromGrpc(PetStore.GrpcPet.GrpcPetCategory grpc) {
            return switch (grpc) {
                case Dog -> PetCategory.Dog;
                case Cat -> PetCategory.Cat;
                case Bunny -> PetCategory.Bunny;
                case UNRECOGNIZED -> throw new IllegalArgumentException("Enum value is not recognized");
            };
        }
    }

    public enum PetStatus {

        Available,
        Pending,
        Sold;

        public PetStore.GrpcPet.GrpcPetStatus toGrpc() {
            return switch (this) {
                case Available -> PetStore.GrpcPet.GrpcPetStatus.Available;
                case Pending -> PetStore.GrpcPet.GrpcPetStatus.Pending;
                case Sold -> PetStore.GrpcPet.GrpcPetStatus.Sold;
            };
        }

        public static PetStatus fromGrpc(PetStore.GrpcPet.GrpcPetStatus grpc) {
            return switch (grpc) {
                case Available -> PetStatus.Available;
                case Pending -> PetStatus.Pending;
                case Sold -> PetStatus.Sold;
                case UNRECOGNIZED -> throw new IllegalArgumentException("Enum value is not recognized");
            };
        }
    }
}
