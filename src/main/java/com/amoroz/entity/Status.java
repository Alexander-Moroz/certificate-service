package com.amoroz.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="task_status")
public class Status implements Serializable {
    private static final long SERIAL_VERSION_UID = 1L;

    @Id
    @NotNull
    @Column(name = "status_id")
    private int status;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private List<Task> taskList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}