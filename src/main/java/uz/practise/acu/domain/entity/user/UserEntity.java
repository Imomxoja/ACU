package uz.practise.acu.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.practise.acu.domain.entity.*;
import uz.practise.acu.domain.entity.reaction.RankingEntity;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails {
    private String fullName;
    private String email;
    private String password;
    @OneToMany(mappedBy = "user")
    private List<CourseEntity> courses;
    @OneToMany(mappedBy = "user")
    private List<RecipeEntity> recipes;
    @ManyToMany
    @JoinTable(
            name = "user_recipe",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<RecipeEntity> savedRecipes;
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<CardEntity> card;
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<RankingEntity> ranking;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

}
