package com.sicredi.app.entity;

import javax.persistence.*;


@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"cpf"})})
public class Associate {

    @Id
    @GeneratedValue
    private  Long id;

    private  String name;

    private  String email;

    @Column(unique=true)
    private  String cpf;

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
