package by.arhor.university.web.api.v1;

import static by.arhor.university.Constants.CACHE_ROLES;
import static by.arhor.university.Constants.REST_API_V_1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arhor.university.domain.model.Role;
import by.arhor.university.domain.repository.RoleRepository;

@Lazy
@RestController
@RequestMapping(path = REST_API_V_1 + "/roles")
public class RoleController extends ApiController {

  private final RoleRepository repository;

  @Autowired
  public RoleController(RoleRepository repository) {
    this.repository = repository;
  }

  @Cacheable(cacheNames = CACHE_ROLES)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Role> getRoles() {
    return repository.findAll();
  }

  @GetMapping(path = "/default", produces = MediaType.APPLICATION_JSON_VALUE)
  public Long getDefaultRole() {
    return repository.getDefaultRole();
  }

}
