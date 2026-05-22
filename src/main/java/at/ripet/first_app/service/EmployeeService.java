package at.ripet.first_app.service;

import at.ripet.first_app.entity.Employee;
import at.ripet.first_app.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    @Transactional
    public List<Employee> findAll() {
        return repository.findAll();
    }

    public void save(Employee employee) {
        repository.save(employee);
    }

    public void delete(Employee employee) {
        repository.delete(employee);
    }
}
