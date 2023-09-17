package nisum.users.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "nisum_users")
@NoArgsConstructor
@AllArgsConstructor
public class
UserData {

    private String id;

    @Id
    @Column(name = "id_email")
    private String email;

    @Column(nullable = false, length = 300)
    private String name;

    @Column(nullable = false, length = 300)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneData> phones;

    @Column(nullable = false, length = 300)
    private Date created;

    @Column(nullable = false, length = 300)
    private Date modified;

    @Column(nullable = false, length = 300)
    private Date lastLogin;

    @Column(nullable = false, length = 300)
    private String token;

    @Column(nullable = false, length = 300)
    private Boolean isActive;

}
