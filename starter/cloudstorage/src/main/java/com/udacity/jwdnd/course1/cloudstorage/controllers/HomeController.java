package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
@AllArgsConstructor
public class HomeController {
    private final UserService userService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    @GetMapping
    public String viewHome(Authentication authentication, Model model){
        User user = userService.getUser(authentication.getName());

        if(user !=null){
            List<String> userFiles = fileService.getAllFiles(user.getUserId());
            model.addAttribute("files", userFiles);
            List<Note> userNotes = noteService.getAllNotes(user.getUserId());
            model.addAttribute("notes", userNotes);
            List<Credential> userCredentials = credentialService.getAllCredentials(user.getUserId());
            model.addAttribute("credentials", userCredentials);
        }
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }
}
