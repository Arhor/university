package by.arhor.university.domain.repository.impl;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import by.arhor.university.domain.model.Lang;
import by.arhor.university.domain.repository.LabelRepository;

@Repository
public class LabelRepositoryImpl implements LabelRepository {

  private static final String SQL_GET_LOCALIZED_STRING = ""
      + "SELECT lb.value "
      + "FROM labels lb WITH(NOLOCK) "
      + "WHERE lb.label = :label "
      + "AND lb.lang_id = :lang_id";

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<String> getLocalizedString(String label, Lang lang) {
    return Optional.ofNullable(
        entityManager
            .createNativeQuery(SQL_GET_LOCALIZED_STRING, String.class)
            .setParameter("label", label)
            .setParameter("lang_id", lang.getId())
            .getSingleResult()
            .toString()
    );
  }
}
