package uz.pdp.backend.payload;

import lombok.*;
import org.telegram.telegrambots.meta.api.objects.Location;
import uz.pdp.backend.enums.BusinessType;

@Builder
public record CreateBusinessApplicationDTO(Long ownerId,
                                           String businessName,
                                           BusinessType businessType,
                                           Location location) {
}
