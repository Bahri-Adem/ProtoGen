package com.example.petstoremonolithique.Generated;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Entity
@Data
@Table(
        name = "\"inventoryliness\""
)
public class InventoryLines {

    private List<InventoryLine> grpcInventoryLine;

    public InventoryLines(List<InventoryLine> grpcInventoryLine) {
        this.grpcInventoryLine = grpcInventoryLine;
    }

    public InventoryLines() {
    }

    @NotNull
    public static InventoryLines fromGrpc(@NotNull PetStore.GrpcInventoryLines inventoryLines) {
        return new InventoryLines(
            inventoryLines.getGrpcInventoryLineList().stream()
                .map(i -> InventoryLine.fromGrpc(i))
                .toList()
        );
    }

    @NotNull
    public PetStore.GrpcInventoryLines toGrpc() {
        PetStore.GrpcInventoryLines.Builder builder = PetStore.GrpcInventoryLines.newBuilder();
        builder.addAllGrpcInventoryLine(grpcInventoryLine.stream()
            .map(i -> i.toGrpc())
            .toList());
        return builder.build();
    }
}
