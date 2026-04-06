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
import library_api.dto.request.ClienteRequestDTO;
import library_api.dto.response.ClienteResponseDTO;
import library_api.model.enums.TipoCliente;
import library_api.service.ClienteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BIBLIOTECARIO', 'ROLE_COORDENADOR','ROLE_ATENDENTE')")
    public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody @Valid  ClienteRequestDTO dto){
        ClienteResponseDTO responseDTO = clienteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ClienteResponseDTO>> buscarClientePorNome(@PathVariable String nome){
        List<ClienteResponseDTO> responseDTOs = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(responseDTOs);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tipoCliente/{tipoCliente}")
    public ResponseEntity<List<ClienteResponseDTO>>  buscarPorTipoCliente(@PathVariable TipoCliente tipoCliente){
        List<ClienteResponseDTO> responseDTOs = clienteService.buscarTipoCliente(tipoCliente);
        return ResponseEntity.ok(responseDTOs);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity <ClienteResponseDTO> buscarClientePorCpf(@PathVariable String cpf){
        ClienteResponseDTO responseDTO = clienteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity <ClienteResponseDTO> buscarClientePorCnpj(@PathVariable String cnpj){
        ClienteResponseDTO responseDTO = clienteService.buscarPorCnpj(cnpj);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/codCliente/{codCliente}")
    public ResponseEntity <ClienteResponseDTO> buscarPorCodigo(@PathVariable Long codCliente ){
        ClienteResponseDTO responseDTO = clienteService.buscarPorCodCliente(codCliente);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BIBLIOTECARIO', 'ROLE_COORDENADOR','ROLE_ATENDENTE')")
    @PutMapping("/{codCliente}")
    public ResponseEntity <ClienteResponseDTO> atualizar(@PathVariable Long codCliente,
        @RequestBody @Valid ClienteRequestDTO dto){
        
        ClienteResponseDTO responseDTO = clienteService.atualizar(codCliente, dto);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BIBLIOTECARIO', 'ROLE_COORDENADOR','ROLE_ATENDENTE')")
    @DeleteMapping("/{codCliente}")
    public ResponseEntity <Void> deletar(@PathVariable Long codCliente){
        clienteService.deletar(codCliente);
        return ResponseEntity.noContent().build();
    }
}
