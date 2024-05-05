package uz.pdp.backend.payload;

import lombok.Builder;

import java.util.UUID;
@Builder
public record CreateProductDTO(
        String productName,
        Long price,
        String description,
        String photoUrl,
        int amount,
        String categoryName,
        UUID businessId){}
