package com.example.petstoremonolithique.Generated;

import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Long;
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
        name = "\"inventorylines\""
)
public class InventoryLine {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    private PetStatus status;

    private Integer count;

    public InventoryLine(
        Long id,
        PetStatus status,
        Integer count
    ) {
        this.id = id;
        this.status = status;
        this.count = count;
    }

    public InventoryLine() {
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public void setStatus(@Nullable PetStatus status) {
        this.status = status;
    }

    public void setCount(@Nullable Integer count) {
        this.count = count;
    }

    @NotNull
    public static InventoryLine fromGrpc(@NotNull PetStore.GrpcInventoryLine inventoryLine) {
        return new InventoryLine(
            inventoryLine.hasId() ? inventoryLine.getId() : null,
            inventoryLine.hasStatus() ? PetStatus.fromGrpc(inventoryLine.getStatus()) : null,
            inventoryLine.hasCount() ? inventoryLine.getCount() : null
        );
    }

    @NotNull
    public PetStore.GrpcInventoryLine toGrpc() {
        PetStore.GrpcInventoryLine.Builder builder = PetStore.GrpcInventoryLine.newBuilder();
        if (id != null) {
            builder.setId(id);
        }
        if (status != null) {
            builder.setStatus(status.toGrpc());
        }
        if (count != null) {
            builder.setCount(count);
        }
        return builder.build();
    }

    public enum PetStatus {

        Available,
        Pending,
        Sold;

        public PetStore.GrpcInventoryLine.GrpcPetStatus toGrpc() {
            return switch (this) {
                case Available -> PetStore.GrpcInventoryLine.GrpcPetStatus.Available;
                case Pending -> PetStore.GrpcInventoryLine.GrpcPetStatus.Pending;
                case Sold -> PetStore.GrpcInventoryLine.GrpcPetStatus.Sold;
            };
        }

        public static PetStatus fromGrpc(PetStore.GrpcInventoryLine.GrpcPetStatus grpc) {
            return switch (grpc) {
                case Available -> PetStatus.Available;
                case Pending -> PetStatus.Pending;
                case Sold -> PetStatus.Sold;
                case UNRECOGNIZED -> throw new IllegalArgumentException("Enum value is not recognized");
            };
        }
    }
}
