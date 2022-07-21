package kr.wiselight.metaverse.backend.domain.user;

import kr.wiselight.metaverse.backend.controller.dto.AdminUserUpdateRequestDto;
import kr.wiselight.metaverse.backend.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.UUID;

@ToString
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean enabled = true;

    @Builder
    public User(String username, String password, String email, Role role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public void update(AdminUserUpdateRequestDto dto) {
        this.username = dto.getUsername();
        if (StringUtils.hasText(dto.getPassword())) {
            this.password = dto.getPassword();
        }
        this.email = dto.getEmail();
    }

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }
}
