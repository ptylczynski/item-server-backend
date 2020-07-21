package cloud.ptl.itemserver.templates;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorTemplate {
    private String reason;
    private Object object;
}
