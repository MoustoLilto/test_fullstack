package fr.milleis.test.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.milleis.test.backend.dto.CreateLeaveRequestDTO;
import fr.milleis.test.backend.dto.LeaveRequestDTO;
import fr.milleis.test.backend.services.LeaveRequestService;

@RestController
@RequestMapping("/api/leave-requests")
@CrossOrigin(origins = "*")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;
    
    @PostMapping
    public ResponseEntity<LeaveRequestDTO> createLeaveRequest(@RequestBody CreateLeaveRequestDTO createLeaveRequestDTO) {
        LeaveRequestDTO createdLeaveRequest = leaveRequestService.createLeaveRequest(createLeaveRequestDTO);
        return new ResponseEntity<>(createdLeaveRequest, HttpStatus.CREATED);
    }
} 