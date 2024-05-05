package uz.pdp.backend.payloa;

import lombok.Builder;
import uz.pdp.backend.model.CourierApplication;

@Builder
public record CourierRejectApplicationDTO(CourierApplication courierApplication , String rejectReason) {
}
