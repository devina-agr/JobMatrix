package com.example.jobmatrix.entity;

import jakarta.persistence.*;

@Entity
@Table(name="test_users")
public class TestUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
