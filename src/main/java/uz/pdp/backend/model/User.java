package uz.pdp.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uz.pdp.backend.enums.Language;
import uz.pdp.backend.enums.Role;
import uz.pdp.backend.enums.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class User implements Serializable {
    private final Long id;
    private Language language;
    private String username;
    private String fullName;
    private String phoneNumber;
    private Status status;
    private LocalDate birthDate;
    private Role role = Role.CLIENT;
    private final LocalDateTime createTime = LocalDateTime.now();
    private boolean isDelete = false;
    private boolean isBan = false;

    public User(Long id, Language language, String phoneNumber, Status status) {
        this.id = id;
        this.language = language;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }
}
