package library_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import library_api.dto.request.FuncionarioRequestDTO;
import library_api.dto.response.FuncionarioCadastroResponseDTO;
import library_api.dto.response.FuncionarioResponseDTO;
import library_api.service.FuncionarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR')")
    @PostMapping
    public ResponseEntity<FuncionarioCadastroResponseDTO> cadastrarFuncionario(@RequestBody @Valid FuncionarioRequestDTO dto) {
        FuncionarioCadastroResponseDTO responseDTO = funcionarioService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR')")
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<FuncionarioResponseDTO> buscarFuncionarioPorMatricula(@PathVariable String matricula){
        FuncionarioResponseDTO responseDTO = funcionarioService.buscarMatriculaFuncionario(matricula);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR')")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<FuncionarioResponseDTO>  buscarFuncionarioPorCpf(@PathVariable String cpf){
        FuncionarioResponseDTO responseDTO =  funcionarioService.buscarCpfFuncionario(cpf);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR')")
    @GetMapping("/nome/{nome}")
    public ResponseEntity <List<FuncionarioResponseDTO>> buscarFuncionariosPorNome(@PathVariable String nome){
        List <FuncionarioResponseDTO> responseDTO = funcionarioService.buscarNomeFuncionario(nome);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listarTodos(){
        List<FuncionarioResponseDTO> responseDTO = funcionarioService.listarTodos();
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR')")
    @PutMapping("/{matricula}")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable String matricula ,
        @RequestBody @Valid FuncionarioRequestDTO dto){
        FuncionarioResponseDTO responseDTO = funcionarioService.atualizar(matricula, dto);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COORDENADOR')")
    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void>  deletar(@PathVariable String matricula){
        funcionarioService.deletar(matricula);
        return ResponseEntity.noContent().build();
    }
}