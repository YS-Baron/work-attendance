package com.coder520.common.task;

import com.coder520.attend.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;

public class AttendCheckTask {

    @Autowired
    private AttendService attendService;

    public void checkAttend (){

        attendService.checkAttend();
    }
}
