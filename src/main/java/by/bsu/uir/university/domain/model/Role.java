package by.bsu.uir.university.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractModelObject<Short> {

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "title", unique = true, nullable = false)
  private Role.Value title;

  public enum Value {
    USER, ADMIN
  }
}
