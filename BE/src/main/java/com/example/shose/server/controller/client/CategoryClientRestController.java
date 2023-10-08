package com.example.shose.server.controller.client;

import com.example.shose.server.service.CategoryService;
import com.example.shose.server.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("*")
@RequestMapping("/client/category")
public class CategoryClientRestController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public ResponseObject getAll() {
        return new ResponseObject(categoryService.getAll());
    }
}
