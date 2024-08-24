package com.boiler.recipe;


import com.boiler.entity.BaseEntity;
import com.boiler.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "recipes")
public class Recipe extends BaseEntity {
    private String uid;
    private String name;
    private String description;
    @ManyToOne
    private User user;
}
