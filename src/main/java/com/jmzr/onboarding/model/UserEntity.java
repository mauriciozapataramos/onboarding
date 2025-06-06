package com.jmzr.onboarding.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {

    @Id
    private String username;

    private String password;
    private boolean enabled;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private List<RoleEntity> roles;
}
