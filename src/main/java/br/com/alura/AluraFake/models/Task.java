package br.com.alura.AluraFake.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne
    private Course course;
    private String statement;
    @Column(name = "`order`")
    private Integer order;
    @Enumerated(EnumType.STRING)
    private Type type;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskOption> options;

    public Task() {}
    
    public Task(String statement, Integer order, Type type, Course course) {
        this.statement = statement;
        this.order = order;
        this.type = type;
        this.course = course;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Course getCourse() {
        return this.course;
    }

    public String getStatement() {
        return this.statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Integer getOrder() {
        return this.order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<TaskOption> getOptions() {
        return this.options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
    }
}
