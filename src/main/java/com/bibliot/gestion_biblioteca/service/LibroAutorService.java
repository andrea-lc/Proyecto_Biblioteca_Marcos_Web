package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.entities.*;
import com.bibliot.gestion_biblioteca.repository.LibroAutorRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class LibroAutorService {
    private final LibroAutorRepository repository;
    public LibroAutorService(LibroAutorRepository repository){this.repository=repository;}
    public List<LibroAutor> listar(){return repository.findAll();}
    public List<LibroAutor> buscarPorLibro(Integer libro){return repository.findByLibroIdLibro(libro);}
    public LibroAutor buscar(Integer libro,Integer autor){return repository.findById(new LibroAutorId(libro,autor)).orElseThrow(()->new RuntimeException("Relación libro-autor no encontrada"));}
    @Transactional public LibroAutor guardar(Integer libro,Integer autor){Libro l=new Libro();l.setIdLibro(libro);Autor a=new Autor();a.setIdAutor(autor);return repository.save(new LibroAutor(l,a));}
    @Transactional public void eliminar(Integer libro,Integer autor){repository.delete(buscar(libro,autor));}
    @Transactional public void reemplazarPorLibro(Integer libro,List<Integer> ids){
        repository.deleteByLibroIdLibro(libro);
        if(ids!=null) for(Integer id:ids) if(id!=null) { Libro l=new Libro();l.setIdLibro(libro);Autor a=new Autor();a.setIdAutor(id);repository.save(new LibroAutor(l,a)); }
    }
}
