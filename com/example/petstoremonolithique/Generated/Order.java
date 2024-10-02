package com.example.petstoremonolithique.Generated;

import com.google.protobuf.Timestamp;
import java.lang.Boolean;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Long;
import java.time.Instant;
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
        name = "\"orders\""
)
public class Order {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    private Long petId;

    private Integer quantity;

    private Instant shipDate;

    private OrderStatus status;

    private Boolean complete;

    public Order(
        Long id,
        Long petId,
        Integer quantity,
        Instant shipDate,
        OrderStatus status,
        Boolean complete
    ) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
        this.complete = complete;
    }

    public Order() {
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public void setPetId(@Nullable Long petId) {
        this.petId = petId;
    }

    public void setQuantity(@Nullable Integer quantity) {
        this.quantity = quantity;
    }

    public void setShipDate(@Nullable Instant shipDate) {
        this.shipDate = shipDate;
    }

    public void setStatus(@Nullable OrderStatus status) {
        this.status = status;
    }

    public void setComplete(@Nullable Boolean complete) {
        this.complete = complete;
    }

    @NotNull
    public static Order fromGrpc(@NotNull PetStore.GrpcOrder order) {
        return new Order(
            order.hasId() ? order.getId() : null,
            order.hasPetId() ? order.getPetId() : null,
            order.hasQuantity() ? order.getQuantity() : null,
            order.hasShipDate() ? Instant.ofEpochSecond(order.getShipDate().getSeconds(), order.getShipDate().getNanos()) : null,
            order.hasStatus() ? OrderStatus.fromGrpc(order.getStatus()) : null,
            order.hasComplete() ? order.getComplete() : null
        );
    }

    @NotNull
    public PetStore.GrpcOrder toGrpc() {
        PetStore.GrpcOrder.Builder builder = PetStore.GrpcOrder.newBuilder();
        if (id != null) {
            builder.setId(id);
        }
        if (petId != null) {
            builder.setPetId(petId);
        }
        if (quantity != null) {
            builder.setQuantity(quantity);
        }
        if (shipDate != null) {
            builder.setShipDate(Timestamp.newBuilder().setSeconds(shipDate.getEpochSecond()).setNanos(shipDate.getNano()).build());
        }
        if (status != null) {
            builder.setStatus(status.toGrpc());
        }
        if (complete != null) {
            builder.setComplete(complete);
        }
        return builder.build();
    }

    public enum OrderStatus {

        Placed,
        Processing,
        Shipped;

        public PetStore.GrpcOrder.GrpcOrderStatus toGrpc() {
            return switch (this) {
                case Placed -> PetStore.GrpcOrder.GrpcOrderStatus.Placed;
                case Processing -> PetStore.GrpcOrder.GrpcOrderStatus.Processing;
                case Shipped -> PetStore.GrpcOrder.GrpcOrderStatus.Shipped;
            };
        }

        public static OrderStatus fromGrpc(PetStore.GrpcOrder.GrpcOrderStatus grpc) {
            return switch (grpc) {
                case Placed -> OrderStatus.Placed;
                case Processing -> OrderStatus.Processing;
                case Shipped -> OrderStatus.Shipped;
                case UNRECOGNIZED -> throw new IllegalArgumentException("Enum value is not recognized");
            };
        }
    }
}
