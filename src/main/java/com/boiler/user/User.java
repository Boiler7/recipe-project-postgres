package com.boiler.user;

import com.boiler.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)@Table(name = "users")
public class User extends BaseEntity {
    private String uid;
    private String name;
    private String email;
    private String password;
}
