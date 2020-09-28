package cloud.ptl.itemserver.persistence.dao.authentication;

import cloud.ptl.itemserver.persistence.helper.LongIndexed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.persistence.*;
import javax.print.DocFlavor;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "random_token")
public class RandomTokenDAO implements LongIndexed {

    public enum Type{
        ACC_ACTIVATION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_fk")
    @NotEmpty private UserDAO owner;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    @NotEmpty
    private LocalDate expiration;

    @NotEmpty
    @Column(
            name = "token",
            unique = true
    )
    private String token;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;
}
