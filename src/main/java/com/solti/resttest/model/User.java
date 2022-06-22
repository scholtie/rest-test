package com.solti.resttest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="Users")
public class User {
    @Id
    private Integer id;
    private String personName;
    private String username;
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address")
    private Address address;
    private String phone;
    private String website;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company")
    private Company company;
}
