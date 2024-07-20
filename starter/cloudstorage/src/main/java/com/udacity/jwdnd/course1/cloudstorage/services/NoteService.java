package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class NoteService {
    private final NoteMapper noteMapper;
    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }
    public int addNote(Note note) {
        return noteMapper.insertNote(new Note(null , note.getNoteTitle() , note.getNoteDescription() , note.getUserId()));
    }

    public void editNote(Note note) {
        noteMapper.updateNote(new Note(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
    }
    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }
}