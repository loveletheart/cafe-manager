package myapp.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Data
@Entity
@Table(name = "userdata")
public class UserData implements UserDetails {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Lob
    @Column(name = "qr_code", columnDefinition = "LONGTEXT")
    private String qrCode;

    // 기본 생성자
    public UserData() {}

    // 전체 필드를 포함하는 생성자
    public UserData(String id, String username, String password, String role, String qrCode) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.qrCode = qrCode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username; // username 반환 (기존 id에서 변경)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
