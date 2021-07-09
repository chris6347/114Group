package com.itheima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller01 {

    @PreAuthorize("hasAuthority('add')")
    @RequestMapping("/add")
    public String add(){
        System.out.println("add...");
        return "add success..";
    }

    @PreAuthorize("hasAuthority('delete')")
    @RequestMapping("/delete")
    public String delete(){
        System.out.println("delete...");
        return "delete success..";
    }

    @PreAuthorize("hasRole('ROLE_ABC')")
    @RequestMapping("/update")
    public String update(){
        System.out.println("update...");
        return "update success..";
    }

}
