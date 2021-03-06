package by.arhor.university.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "faculties_has_enrollees")
public class FacultyEnrollee implements Serializable {

  @EmbeddedId
  private CompositeId id;

  @Column(name = "filing_date", nullable = false, updatable = false)
  private LocalDateTime filingDate;

  @ManyToOne(optional = false)
  @MapsId("faculty_id")
  private Faculty faculty;

  @ManyToOne(optional = false)
  @MapsId("enrollee_id")
  private Enrollee enrollee;

  @PrePersist
  public void logFilingDate() {
    this.filingDate = LocalDateTime.now();
  }

  @Data
  @Embeddable
  public static class CompositeId implements Serializable {
    @Column(name = "faculty_id", nullable = false)
    private Long facultyId;

    @Column(name = "enrollee_id", nullable = false)
    private Long enrolleeId;
  }
}
