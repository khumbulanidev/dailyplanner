package com.khumbu.dailyplanner.service;

import com.khumbu.dailyplanner.exceptions.DailyPlannerException;
import com.khumbu.dailyplanner.models.UserOperation;
import com.khumbu.dailyplanner.repository.UserOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserOperationService {


    @Autowired
    private UserOperationRepository userOperationRepository;

    public List<UserOperation> getOperations(){
        return userOperationRepository.findAll();
    }


    public UserOperation save(UserOperation operation) {
        verifyOperation(operation);
        UserOperation userOperation = new UserOperation(operation.getOperation());
        userOperation.setLink(operation.getLink());
        return userOperationRepository.save(userOperation);
    }

    public void verifyOperation(UserOperation operation){
        if(operation.getOperation() == null || operation.getOperation().isEmpty()){
            throw new DailyPlannerException("Operation cannot be null or empty");
        }
    }

    public UserOperation deleteById(Long id) {
       UserOperation operation = userOperationRepository.findById(id).orElseThrow(()->  new DailyPlannerException("Operation not found in database"));
         userOperationRepository.deleteById(id);
        return operation;
    }
}
