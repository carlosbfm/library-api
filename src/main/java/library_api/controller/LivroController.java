package library_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import library_api.dto.request.LivroRequestDTO;
import library_api.dto.response.LivroResponseDTO;
import library_api.service.LivroService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroController {
    private final LivroService livroService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR','ROLE_BIBLIOTECARIO')")
    @PostMapping
    public ResponseEntity<LivroResponseDTO> cadastrar(@RequestBody @Valid LivroRequestDTO dto){
        LivroResponseDTO responseDto = livroService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/codLivro/{codLivro}")
    public ResponseEntity<LivroResponseDTO> buscarLivroPorCod(@PathVariable Long codLivro){
        LivroResponseDTO responseDTO = livroService.buscarPorCodigo(codLivro);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LivroResponseDTO>> buscarLivroPorTitulo(@PathVariable String titulo){
        List <LivroResponseDTO> responseDTOs = livroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(responseDTOs);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LivroResponseDTO>> buscarLivroPorAutor(@PathVariable String autor){
        List<LivroResponseDTO> responseDTOs = livroService.buscarPorAutor(autor);
        return ResponseEntity.ok(responseDTOs);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<LivroResponseDTO>> buscarLivroPorGenero(@PathVariable String genero){
        List<LivroResponseDTO> responseDTOs = livroService.buscarPorGenero(genero);
        return ResponseEntity.ok(responseDTOs);
    }

    @PreAuthorize("isAuthenticated") 
    @GetMapping("/buscar-isbn")
    public ResponseEntity<List<LivroResponseDTO>> buscarLivroPorIsbn(
        @RequestParam(name = "isbn", required = false) String isbn){

        List<LivroResponseDTO> responseDTOs = livroService.buscarPorIsbn(isbn);
        return ResponseEntity.ok(responseDTOs);
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarTodos(){
        List<LivroResponseDTO> responseDTOs = livroService.listarTodos();
        return ResponseEntity.ok(responseDTOs);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR','ROLE_BIBLIOTECARIO')")
    @PutMapping("/{codLivro}")
    public ResponseEntity <LivroResponseDTO> atualizar(@PathVariable Long codLivro, 
        @RequestBody @Valid LivroRequestDTO dto){
        LivroResponseDTO responseDTO = livroService.atualizar(codLivro, dto);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR','ROLE_BIBLIOTECARIO')")
    @PatchMapping("/{codLivro}/perda")
    public ResponseEntity<Void> reportarPerda(@PathVariable Long codLivro){
        livroService.reportarPerda(codLivro);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR','ROLE_BIBLIOTECARIO','ROLE_ATENDENTE')")
    @PatchMapping("/{codLivro}/emprestar")
    public ResponseEntity<Void> emprestarLivro(@PathVariable Long codLivro){
        livroService.alterarPraEmprestado(codLivro);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR','ROLE_BIBLIOTECARIO','ROLE_ATENDENTE')")
    @PatchMapping("/{codLivro}/dano")
    public ResponseEntity<Void> reportarDano(@PathVariable Long codLivro){
        livroService.reportarLivroDanificado(codLivro);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR','ROLE_BIBLIOTECARIO','ROLE_ATENDENTE')")
    @PatchMapping("/{codLivro}/disponibilizar")
    public ResponseEntity<Void> disponibilizarLivro(@PathVariable Long codLivro){
        livroService.alterarPraDisponivel(codLivro);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR')")
    @DeleteMapping("/{codLivro}")
    public ResponseEntity<Void> deletar(@PathVariable Long codLivro){
        livroService.deletar(codLivro);
        return ResponseEntity.noContent().build();
    }
    
}
