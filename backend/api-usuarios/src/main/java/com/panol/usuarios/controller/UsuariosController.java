package com.panol.usuarios.controller;

import com.panol.usuarios.dto.UsuarioDTO;
import com.panol.usuarios.service.UsuarioService;
import org.springframework.hateoas.CollectionModel;
    import org.springframework.hateoas.IanaLinkRelations;
    import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuariosController {

    private final UsuarioService usuarioService;

    public UsuariosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todos los usuarios con enlaces de auto‑relación (self) y operaciones disponibles.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<UsuarioDTO>> listar() {
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
        usuarios.forEach(dto -> {
            dto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                    .obtenerPorId(dto.getRut())).withSelfRel());
            dto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                    .editar(dto.getRut(), dto)).withRel("editar"));
            dto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                    .eliminar(dto.getRut())).withRel("eliminar"));
        });
        CollectionModel<UsuarioDTO> collection = CollectionModel.of(usuarios);
        collection.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                .listar()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    /**
     * Obtiene un usuario por su RUT.  El recurso incluye enlaces a la colección y a las operaciones de edición/eliminación.
     */
    @GetMapping("/{rut}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable int rut) {
        UsuarioDTO dto = usuarioService.buscarPorRut(rut);
        dto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                .obtenerPorId(rut)).withSelfRel());
        dto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                .listar()).withRel(IanaLinkRelations.COLLECTION));
        dto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                .editar(rut, dto)).withRel("editar"));
        dto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                .eliminar(rut)).withRel("eliminar"));
        return ResponseEntity.ok(dto);
    }

    /**
     * Crea un nuevo usuario.  El recurso devuelto incluye enlaces a sí mismo y a la colección.
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody UsuarioDTO dto) {
        UsuarioDTO creado = usuarioService.crearUsuario(dto);
        creado.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                .obtenerPorId(creado.getRut())).withSelfRel());
        creado.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                .listar()).withRel(IanaLinkRelations.COLLECTION));
        return ResponseEntity.ok(creado);
    }

    /**
     * Edita un usuario existente.  Solo se actualizan los campos presentes en el cuerpo de la petición.
     */
    @PutMapping("/{rut}")
    public ResponseEntity<UsuarioDTO> editar(@PathVariable int rut, @RequestBody UsuarioDTO dto) {
        UsuarioDTO actualizado = usuarioService.editarUsuario(rut, dto);
        actualizado.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                .obtenerPorId(actualizado.getRut())).withSelfRel());
        actualizado.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuariosController.class)
                .listar()).withRel(IanaLinkRelations.COLLECTION));
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Elimina un usuario por su RUT y devuelve un mensaje de confirmación.
     */
    @DeleteMapping("/{rut}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable int rut) {
        usuarioService.eliminarUsuario(rut);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
    }
}