package com.example.petstoremonolithique.Generated;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Entity
@Data
@Table(
        name = "\"orderss\""
)
public class Orders {

    private List<Order> grpcOrder;

    public Orders(List<Order> grpcOrder) {
        this.grpcOrder = grpcOrder;
    }

    public Orders() {
    }

    @NotNull
    public static Orders fromGrpc(@NotNull PetStore.GrpcOrders orders) {
        return new Orders(
            orders.getGrpcOrderList().stream()
                .map(i -> Order.fromGrpc(i))
                .toList()
        );
    }

    @NotNull
    public PetStore.GrpcOrders toGrpc() {
        PetStore.GrpcOrders.Builder builder = PetStore.GrpcOrders.newBuilder();
        builder.addAllGrpcOrder(grpcOrder.stream()
            .map(i -> i.toGrpc())
            .toList());
        return builder.build();
    }
}
