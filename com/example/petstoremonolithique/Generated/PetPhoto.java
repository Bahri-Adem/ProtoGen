package com.example.petstoremonolithique.Generated;

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
        name = "\"petphotos\""
)
public class PetPhoto {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    private Long petID;

    private String metaData;

    private List<String> fileData;

    public PetPhoto(
        Long id,
        Long petID,
        String metaData,
        List<String> fileData
    ) {
        this.id = id;
        this.petID = petID;
        this.metaData = metaData;
        this.fileData = fileData;
    }

    public PetPhoto() {
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public void setPetID(@Nullable Long petID) {
        this.petID = petID;
    }

    public void setMetaData(@Nullable String metaData) {
        this.metaData = metaData;
    }

    @NotNull
    public static PetPhoto fromGrpc(@NotNull PetStore.GrpcPetPhoto petPhoto) {
        return new PetPhoto(
            petPhoto.hasId() ? petPhoto.getId() : null,
            petPhoto.hasPetID() ? petPhoto.getPetID() : null,
            petPhoto.hasMetaData() ? petPhoto.getMetaData() : null,
            petPhoto.getFileDataList()
        );
    }

    @NotNull
    public PetStore.GrpcPetPhoto toGrpc() {
        PetStore.GrpcPetPhoto.Builder builder = PetStore.GrpcPetPhoto.newBuilder();
        if (id != null) {
            builder.setId(id);
        }
        if (petID != null) {
            builder.setPetID(petID);
        }
        if (metaData != null) {
            builder.setMetaData(metaData);
        }
        builder.addAllFileData(fileData);
        return builder.build();
    }
}
