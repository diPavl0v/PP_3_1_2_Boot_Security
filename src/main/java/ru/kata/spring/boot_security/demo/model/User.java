package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity @Table(name="users")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(unique = true, nullable = false)
    private String username;

    @NotBlank @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private Integer age;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="users_roles",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override public Collection<? extends GrantedAuthority> getAuthorities(){ return roles; }
    @Override public boolean isAccountNonExpired(){ return true; }
    @Override public boolean isAccountNonLocked(){ return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
    @Override public boolean isEnabled(){ return true; }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id=id; }
    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username=username; }
    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password=password; }
    public String getFirstName(){ return firstName; }
    public void setFirstName(String firstName){ this.firstName=firstName; }
    public String getLastName(){ return lastName; }
    public void setLastName(String lastName){ this.lastName=lastName; }
    public Integer getAge(){ return age; }
    public void setAge(Integer age){ this.age=age; }
    public Set<Role> getRoles(){ return roles; }
    public void setRoles(Set<Role> roles){ this.roles=roles; }
}
