package domain.entity;

import java.util.List;

public class Employee {

    private Long id;
    private String firstName;
    private String lastName;
    private int rating;
    private Company company;
    private List<Task> tasks;
}
