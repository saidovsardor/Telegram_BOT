package uz.pdp.bot.model;

import lombok.Data;
import lombok.NonNull;
import uz.pdp.backend.utils.GlobalVar;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class TempData<E> implements Serializable {
    private Long creator = GlobalVar.getUSER().getId();
    private String key;
    private E val;

    public TempData(@NonNull String key, E val) {
        this.key = key;
        this.val = val;
    }
}
