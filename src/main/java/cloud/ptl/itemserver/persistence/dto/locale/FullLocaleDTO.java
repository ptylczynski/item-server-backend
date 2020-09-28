package cloud.ptl.itemserver.persistence.dto.locale;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(itemRelation = "locale", collectionRelation = "locales")
public class FullLocaleDTO extends RepresentationModel<FullLocaleDTO> {
    private Long id;
    private String language;
    private String country;
}
