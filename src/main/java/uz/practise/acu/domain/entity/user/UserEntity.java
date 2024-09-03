package uz.practise.acu.domain.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.practise.acu.domain.entity.BaseEntity;
import uz.practise.acu.domain.entity.CardEntity;
import uz.practise.acu.domain.entity.CourseEntity;
import uz.practise.acu.domain.entity.RecipeEntity;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "user")
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
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<CardEntity> card;

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
