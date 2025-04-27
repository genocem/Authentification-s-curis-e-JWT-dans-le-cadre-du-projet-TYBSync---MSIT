package com.example.demo.AppUser;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class AppUser {

    private @Id
    @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    private String name;
    private String role;
    @JsonIgnore
    private String password;
    private String email="";



    protected AppUser() {
        this.name = "";
        this.role = "";
    }

    public AppUser(String name, String role, String email) {
        this.name = name;
        this.role = role;
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof com.example.demo.AppUser.AppUser appUser))
            return false;
        return Objects.equals(this.id, appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.email);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", name='" + this.name + '\'' + ", role='" + this.role + '\'' + '}';
    }


}