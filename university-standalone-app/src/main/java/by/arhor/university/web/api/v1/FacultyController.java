package by.arhor.university.web.api.v1;

import static by.arhor.university.Constants.REST_API_V_1;
import static by.arhor.university.web.api.util.PageUtils.bound;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import by.arhor.university.core.Either;
import by.arhor.university.model.Subject;
import by.arhor.university.service.FacultyService;
import by.arhor.university.service.dto.FacultyDTO;
import by.arhor.university.service.error.ServiceError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(
    path = REST_API_V_1 + "/faculties",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class FacultyController extends ApiController {

  private final FacultyService service;

  @GetMapping
  public List<FacultyDTO> getFaculties(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return bound(service::findPage).apply(page, size);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getFaculty(@PathVariable long id, Locale locale) {
    return handle(
        service.findOne(id),
        locale
    );
  }

  @GetMapping("/{id}/subjects")
  public ResponseEntity<?> getFacultySubjects(@PathVariable long id, Locale locale) {
    var serviceResponse = service.findOne(id);

    if (serviceResponse.hasError()) {
      return handle(serviceResponse, locale);
    }

    Optional<FacultyDTO> value = serviceResponse.value();

    return null;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @PreAuthorize("hasAuthority('ADMIN')")
  public void deleteFaculty(@PathVariable long id) {
    service.deleteById(id);
  }
}
