package com.shopping_list.controller;

import com.shopping_list.Repository.StatusRepository;
import com.shopping_list.entities.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

@Controller
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private StatusRepository statusRepository;
    @GetMapping("/all")
    public String findAll(Model model){
        model.addAttribute("statuts", statusRepository.findAll());
        return "status/statuts";
    }


    @GetMapping("/form")
    public String form(Model model){
        model.addAttribute("status", new Status());
        return "status/form";
    }

    @PostMapping("/save")
    public String save(Status status){
        statusRepository.save(status);
        return "status/redirection";
    }

    @GetMapping("/update/{status_id}")
    public String updatedTask(Model model, @PathVariable Long status_id){
        Optional<Status> optional =  statusRepository.findById(status_id);
        model.addAttribute("status", optional.get());
        return "status/update";
    }
    @PostMapping("/update/{status_id}")
    public String save(Status status, @PathVariable Long status_id, BindingResult result){

        if(result.hasErrors()) {
            status.setStatus_id(status_id);
            return "status/update";
        }
        Status status1 = statusRepository.save(status);
        return "redirect:/status/detail/" +status1.getStatus_id() ;
    }

    @GetMapping("/delete/{status_id}")
    public void deleteById(@PathVariable Long status_id, Model model) {
        Status status = statusRepository.findById(status_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shopping id:" +status_id));
        statusRepository.delete(status);
        model.addAttribute("statuts", statusRepository.findAll());
    }

    @GetMapping("/detail/{status_id}")
    public String getTask(Model model, @PathVariable Long status_id){
        Optional<Status> optional=statusRepository.findById(status_id);
        model.addAttribute("status", optional.get());
        return "status/detail";

    }


}
