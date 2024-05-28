package domain.entity;

import java.util.List;

public class Task {

    private Long id;
    private String name;
    private String description;
    private int rating;
    private TaskStatus taskStatus;
    private List<Employee> employees;
}
