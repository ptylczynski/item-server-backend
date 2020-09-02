package cloud.ptl.itemserver.persistence.helper.token;

import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;

@Data
@Builder
public class PermissionToken {
    private Long id;
    private Class<?> aClass;

    public static PermissionToken of(Object object){
        if (Arrays.stream(
                object.getClass().getInterfaces()
            ).noneMatch(a -> a == LongIndexed.class)
        ) throw new IllegalArgumentException("Object not implements LongIndexed interface");
        return PermissionToken.builder()
                        .aClass(object.getClass())
                        .id(((LongIndexed) object).getId())
                        .build();
    }
}
