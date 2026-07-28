package com.jhoel.empleados_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "office_location", nullable = false)
    private String officeLocation;

    @OneToMany(
            mappedBy = "department",
            fetch = FetchType.LAZY
    )
    private List<Employee> employees;
}
