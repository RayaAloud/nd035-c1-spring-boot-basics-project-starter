package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor
@Controller
public class NoteController {
    private NoteService noteService;
    private UserService userService;

    @PostMapping("/add-note")
    public String noteSubmit(@ModelAttribute Note note, Authentication authentication, RedirectAttributes redirectAttributes) {
        try{
            User user=userService.getUser(authentication.getName());
            note.setUserId(user.getUserId());
            if(note.getNoteId()==null){
                noteService.addNote(note);
                redirectAttributes.addFlashAttribute("operationSuccess","New note is created successfully!");
            }
            else {
                noteService.editNote(note);
                redirectAttributes.addFlashAttribute("operationSuccess","Note is updated successfully!");

            }
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("operationFailed","Something went wrong!");}
        return "redirect:/home";
    }

    @GetMapping("/delete-note/{noteId}")
    public String noteDelete(@PathVariable Integer noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("operationSuccess", "Note is deleted successfully!");
        }catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("operationFailed","Something went wrong!");
        }
        return "redirect:/home";

    }

}