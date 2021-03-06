package by.arhor.university.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true, exclude = {"enrollee"})
@ToString(exclude = {"enrollee"})
@NamedStoredProcedureQueries({
  @NamedStoredProcedureQuery(
      name = "createNewUser",
      procedureName = "createNewUser",
      resultClasses = User.class,
      parameters = {
        @StoredProcedureParameter(name = "email", type = String.class),
        @StoredProcedureParameter(name = "password", type = String.class),
        @StoredProcedureParameter(name = "first_name", type = String.class),
        @StoredProcedureParameter(name = "last_name", type = String.class),
        @StoredProcedureParameter(name = "role_id", type = Long.class),
        @StoredProcedureParameter(name = "lang_id", type = Long.class),
      })
})
public class User extends AbstractModelObject<Long> {

  @NotEmpty
  @Size(min = 6, max = 255)
  @Column(name = "email", unique = true, nullable = false, length = 255)
  private String email;

  @NotEmpty
  @Column(name = "password", nullable = false, length = 512)
  private String password;

  @NotEmpty
  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @NotEmpty
  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
  private Role role;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "lang_id", nullable = false, referencedColumnName = "id")
  private Lang lang;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
  private Enrollee enrollee;
}
