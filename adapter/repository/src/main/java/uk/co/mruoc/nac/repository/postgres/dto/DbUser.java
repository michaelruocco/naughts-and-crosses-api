package uk.co.mruoc.nac.repository.postgres.dto;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "user_record")
public class DbUser {

  private final String id;
  @Id private final String username;
  private final String name;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final boolean emailVerified;
  private final String status;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "user_group",
      joinColumns = {@JoinColumn(name = "username")})
  @Column(name = "group_name")
  private final Collection<String> groups;
}
