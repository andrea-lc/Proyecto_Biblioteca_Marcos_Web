package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.entities.*;
import com.bibliot.gestion_biblioteca.repository.LibroCategoriaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class LibroCategoriaService {
    private final LibroCategoriaRepository repository;
    public LibroCategoriaService(LibroCategoriaRepository repository){this.repository=repository;}
    public List<LibroCategoria> listar(){return repository.findAll();}
    public List<LibroCategoria> buscarPorLibro(Integer libro){return repository.findByLibroIdLibro(libro);}
    public LibroCategoria buscar(Integer libro,Integer categoria){return repository.findById(new LibroCategoriaId(libro,categoria)).orElseThrow(()->new RuntimeException("Relación libro-categoría no encontrada"));}
    @Transactional public LibroCategoria guardar(Integer libro,Integer categoria){Libro l=new Libro();l.setIdLibro(libro);Categoria c=new Categoria();c.setIdCategoria(categoria);return repository.save(new LibroCategoria(l,c));}
    @Transactional public void eliminar(Integer libro,Integer categoria){repository.delete(buscar(libro,categoria));}
    @Transactional public void reemplazarPorLibro(Integer libro,List<Integer> ids){
        repository.deleteByLibroIdLibro(libro);
        if(ids!=null) for(Integer id:ids) if(id!=null) { Libro l=new Libro();l.setIdLibro(libro);Categoria c=new Categoria();c.setIdCategoria(id);repository.save(new LibroCategoria(l,c)); }
    }
}
