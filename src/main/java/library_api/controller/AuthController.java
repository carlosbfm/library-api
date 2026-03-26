package library_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import library_api.dto.auth.DadosTokenJWT;
import library_api.dto.auth.LoginRequestDTO;
import library_api.model.entity.funcionario.FuncionarioEntity;
import library_api.model.entity.usuario.UsuarioEntity;
import library_api.repository.FuncionarioRepository;
import library_api.security.UsuarioDetailsImpl;
import library_api.service.TokenService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor 
@CrossOrigin(origins = "http://localhost:4200") 
public class AuthController {

    private final AuthenticationManager manager; 
    private final TokenService tokenService;
    private final FuncionarioRepository funcionarioRepository;      

    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> fazerLogin(@RequestBody @Valid LoginRequestDTO dto) {
        
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
        var authentication = manager.authenticate(authenticationToken);
        
        var usuarioDetails = (UsuarioDetailsImpl) authentication.getPrincipal();
        UsuarioEntity usuarioLogado = usuarioDetails.getUsuario();
        
        var tokenJWT = tokenService.gerarToken(usuarioLogado);

        FuncionarioEntity funcionario = funcionarioRepository.findById(usuarioLogado.getLogin())
                .orElseThrow(() -> new RuntimeException("Dados de RH não encontrados para este usuário."));

        return ResponseEntity.ok().body(new DadosTokenJWT(
            tokenJWT,
            funcionario.getNome(),
            funcionario.getMatricula(),
            funcionario.getCargo().getNomeCargo().name() 
        ));
    }
}