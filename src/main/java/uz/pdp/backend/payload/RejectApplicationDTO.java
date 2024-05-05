package uz.pdp.backend.payload;

import lombok.*;
import uz.pdp.backend.model.BusinessApplication;

@Builder
@AllArgsConstructor
@Data
public class RejectApplicationDTO {
    private BusinessApplication businessApplication;
    private String rejectReason;
}
