package com.groupeisi.controller;

import com.groupeisi.entities.Etudiant;
import com.groupeisi.repository.EtudiantRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groupeisi.service.EtudiantService;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/etudiant")
public class EtudiantController {

    @Autowired
    private EtudiantRepository repository;

    private GraphQL graphQL;
    private final EtudiantService etudiantService;

    @Value("classpath:etudiant.graphqls")
    private Resource schemaResource;

    @PostConstruct
    public void loadSchema() throws IOException {
        File schemaFile = schemaResource.getFile();
        TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildwiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildwiring() {
        DataFetcher<List<Etudiant>> fetcher1 = data -> {
            return repository.findAll();
        };
        DataFetcher<Etudiant> fetcher2 = data -> {
            return repository.findByTelephone(data.getArgument("telephone"));
        };
        return RuntimeWiring
            .newRuntimeWiring()
            .type("Query", typewriting -> typewriting.dataFetcher("getAllEtudiant", fetcher1).dataFetcher("findEtudiant", fetcher2))
            .build();
    }

    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @PostMapping("/create")
    public Etudiant create(@RequestBody Etudiant etudiant) {
        return etudiantService.creer(etudiant);
    }

    @GetMapping("/read")
    public List<Etudiant> read() {
        return etudiantService.lire();
    }

    @PutMapping("/update/{id}")
    public Etudiant update(int id, Etudiant etudiant) {
        return etudiantService.modifier(id, etudiant);
    }

    @DeleteMapping("/delete")
    public String delete(@PathVariable int id) {
        return etudiantService.supprimer(id);
    }

    @PostMapping("/getAll")
    public ResponseEntity<Object> getAll(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @PostMapping("/getEtudiantById")
    public ResponseEntity<Object> getEtudiantById(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}
