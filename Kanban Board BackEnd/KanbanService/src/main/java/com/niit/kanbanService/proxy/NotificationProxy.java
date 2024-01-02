package com.niit.kanbanService.proxy;

import com.niit.kanbanService.domain.Notification;
import com.niit.kanbanService.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="user-notification-service",url="localhost:8085")
public interface NotificationProxy {

    @PostMapping("/api/v3/saveuser")
    public ResponseEntity<?> saveUser(@RequestBody Notification user);
}
