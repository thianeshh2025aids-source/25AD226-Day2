package com.example.leave.service;

import com.example.leave.entity.Employee;
import com.example.leave.entity.LeaveRequest;
import com.example.leave.repository.EmployeeRepository;
import com.example.leave.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public LeaveRequest applyLeave(LeaveRequest leaveRequest) {
        Long empId = leaveRequest.getEmployee().getId();
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + empId));
        leaveRequest.setEmployee(employee);
        if (leaveRequest.getStatus() == null) {
            leaveRequest.setStatus("PENDING");
        }
        return leaveRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public LeaveRequest getLeaveById(Long id) {
        return leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
    }

    public LeaveRequest updateLeaveStatus(Long id, Map<String, String> statusMap) {
        LeaveRequest leaveRequest = getLeaveById(id);
        leaveRequest.setStatus(statusMap.get("status"));
        return leaveRepository.save(leaveRequest);
    }

    public void deleteLeave(Long id) {
        LeaveRequest leaveRequest = getLeaveById(id);
        leaveRepository.delete(leaveRequest);
    }

    public List<LeaveRequest> getLeavesByEmployeeId(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }
        return leaveRepository.findByEmployeeId(employeeId);
    }
}