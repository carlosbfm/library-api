package library_api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library_api.dto.request.FuncionarioRequestDTO;
import library_api.dto.response.FuncionarioCadastroResponseDTO;
import library_api.dto.response.FuncionarioResponseDTO;
import library_api.mapper.FuncionarioMapper;
import library_api.model.entity.funcionario.Cargo;
import library_api.model.entity.funcionario.FuncionarioEntity;
import library_api.model.entity.usuario.UsuarioEntity; 
import library_api.model.enums.TipoCargo;
import library_api.repository.CargoRepository;
import library_api.repository.FuncionarioRepository;
import library_api.util.DocumentoUtil;
import library_api.util.GeradorCredenciais;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioMapper funcionarioMapper;
    private final CargoRepository cargoRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeradorCredenciais geradorCredenciais;

    @Transactional
    public FuncionarioCadastroResponseDTO cadastrar(FuncionarioRequestDTO dto) {


        String cpfLimpo = DocumentoUtil.limpaFormatacao(dto.cpf());
        
        if (funcionarioRepository.existsByCpf(cpfLimpo)) {
            throw new RuntimeException("Operação cancelada: Já existe um funcionário com este CPF.");
        }

        TipoCargo enumTratado;
        try {
            enumTratado = TipoCargo.valueOf(dto.cargo().toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Cargo inválido! Opções: ADMINISTRADOR, ATENDENTE, DESENVOLVEDOR.");
        }

        Cargo cargo = cargoRepository.findByNomeCargo(enumTratado)
            .orElseGet(() -> {
                Cargo novoCargo = new Cargo();
                novoCargo.setNomeCargo(enumTratado);
                BigDecimal salario = dto.salarioBase() != null ? dto.salarioBase() : BigDecimal.ZERO;
                novoCargo.setSalarioBase(salario);
                return cargoRepository.save(novoCargo); 
            });
        
        String matriculaGerada;
        do {
            matriculaGerada = geradorCredenciais.gerarMatricula();
        } while (funcionarioRepository.existsById(matriculaGerada));

        String senhaPlana = geradorCredenciais.gerarSenha(8, false);
        String senhaHasheada = passwordEncoder.encode(senhaPlana);
        
        log.info("========================================");
        log.info("NOVO FUNCIONÁRIO GERADO");
        log.info("MATRÍCULA: {}", matriculaGerada);
        log.info("SENHA PLANA (USE NO LOGIN): {}", senhaPlana);
        log.info("========================================");

        UsuarioEntity novoUsuario = UsuarioEntity.builder()
                .login(matriculaGerada) 
                .password(senhaHasheada) 
                .dataExpiracaoSenha(LocalDate.now().plusMonths(3))
                .roles("ROLE_" + cargo.getNomeCargo().name()) 
                .build();

        FuncionarioEntity novoFuncionario = FuncionarioEntity.builder()
                .matricula(matriculaGerada)
                .nome(dto.nome())
                .cpf(cpfLimpo)
                .dataAdmissao(dto.dataAdmissao())
                .cargo(cargo)
                .usuario(novoUsuario)
                .build();

        funcionarioRepository.save(novoFuncionario);

        return new FuncionarioCadastroResponseDTO(
                matriculaGerada, 
                senhaPlana, 
                novoFuncionario.getNome(),
                novoFuncionario.getCpf(),
                novoFuncionario.getDataAdmissao(),
                cargo.getNomeCargo().name(),
                cargo.getSalarioBase()
        );
    } 

    public FuncionarioResponseDTO buscarMatriculaFuncionario(String matricula){
        FuncionarioEntity funcionarioEncontrado = funcionarioRepository.findById(matricula)
        .orElseThrow(()->  new IllegalArgumentException("Funcionário com a matricula " + matricula + " não encontrado"));

        return funcionarioMapper.toDto(funcionarioEncontrado);
    }

    public FuncionarioResponseDTO buscarCpfFuncionario(String cpf){
        String cpfLimpo = DocumentoUtil.limpaFormatacao(cpf);

        FuncionarioEntity funcionarioEncontrado = funcionarioRepository.findByCpf(cpfLimpo)
        .orElseThrow(() -> new IllegalArgumentException("Funcionário com cpf " + cpf + " não encontrado!"));
        
        return funcionarioMapper.toDto(funcionarioEncontrado);
    }

    public List<FuncionarioResponseDTO> buscarNomeFuncionario(String nome){
        List<FuncionarioEntity> funcionarioEncontrados = funcionarioRepository.findByNomeContainingIgnoreCase(nome);

        if(funcionarioEncontrados.isEmpty()){
            throw new RuntimeException("Nenhum funcionário encontrado com o nome: " + nome);
        }

        return funcionarioEncontrados.stream()
                .map(funcionarioMapper::toDto)
                .toList();
    }
    
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioRepository.findAll()
                .stream()
                .map(funcionarioMapper::toDto)
                .toList();
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(String matricula, FuncionarioRequestDTO dto) {
        
        FuncionarioEntity entidade = funcionarioRepository.findById(matricula)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com a matrícula: " + matricula));

        entidade.setNome(dto.nome());
        entidade.setDataAdmissao(dto.dataAdmissao());

        TipoCargo enumTratado = TipoCargo.valueOf(dto.cargo().toUpperCase().trim());
        Cargo cargo = cargoRepository.findByNomeCargo(enumTratado)
            .orElseGet(() -> {
                Cargo novoCargo = new Cargo();
                novoCargo.setNomeCargo(enumTratado);
                novoCargo.setSalarioBase(dto.salarioBase() != null ? dto.salarioBase() : BigDecimal.ZERO);
                return cargoRepository.save(novoCargo); 
            });
        
        entidade.setCargo(cargo);
        
        entidade.getUsuario().setRoles("ROLE_" + cargo.getNomeCargo().name());

        FuncionarioEntity entidadeAtualizada = funcionarioRepository.save(entidade);
        return funcionarioMapper.toDto(entidadeAtualizada);
    }

    @Transactional
    public void deletar(String matricula) {
        FuncionarioEntity entidade = funcionarioRepository.findById(matricula)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com a matrícula: " + matricula));
        
        funcionarioRepository.delete(entidade);
    }
}