package com.seguroflow.policyapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OpenApiController {

    @GetMapping("/v3/api-docs")
    public Map<String, Object> apiDocs() {
        Map<String, Object> spec = new LinkedHashMap<>();
        spec.put("openapi", "3.0.1");
        spec.put("info", Map.of(
                "title", "SeguroFlow Policy API",
                "version", "v1",
                "description", "API REST para clientes, apolices e sinistros do SeguroFlow."
        ));
        spec.put("paths", paths());
        spec.put("components", components());
        return spec;
    }

    private Map<String, Object> paths() {
        Map<String, Object> paths = new LinkedHashMap<>();
        addCollectionPath(paths, "/clientes", "Clientes", "ClienteDTO", "ClienteResponseDTO");
        addItemPath(paths, "/clientes/{id}", "Clientes", "ClienteResponseDTO");
        addCollectionPath(paths, "/apolices", "Apolices", "ApoliceDTO", "ApoliceResponseDTO");
        addItemPath(paths, "/apolices/{id}", "Apolices", "ApoliceResponseDTO");
        addCollectionPath(paths, "/sinistros", "Sinistros", "SinistroDTO", "SinistroResponseDTO");
        addItemPath(paths, "/sinistros/{id}", "Sinistros", "SinistroResponseDTO");
        return paths;
    }

    private void addCollectionPath(
            Map<String, Object> paths,
            String path,
            String tag,
            String requestSchema,
            String responseSchema
    ) {
        paths.put(path, Map.of(
                "get", operation(tag, "Listar " + tag.toLowerCase(), null, arrayResponse(responseSchema)),
                "post", operation(tag, "Criar " + tag.toLowerCase(), requestBody(requestSchema), createdResponse(responseSchema))
        ));
    }

    private void addItemPath(Map<String, Object> paths, String path, String tag, String responseSchema) {
        paths.put(path, Map.of(
                "get", withIdParameter(operation(tag, "Buscar " + tag.toLowerCase() + " por id", null, okResponse(responseSchema)))
        ));
    }

    private Map<String, Object> operation(String tag, String summary, Object requestBody, Map<String, Object> responses) {
        Map<String, Object> operation = new LinkedHashMap<>();
        operation.put("tags", List.of(tag));
        operation.put("summary", summary);
        if (requestBody != null) {
            operation.put("requestBody", requestBody);
        }
        operation.put("responses", responses);
        return operation;
    }

    private Map<String, Object> withIdParameter(Map<String, Object> operation) {
        operation.put("parameters", List.of(Map.of(
                "name", "id",
                "in", "path",
                "required", true,
                "schema", Map.of("type", "integer", "format", "int64")
        )));
        return operation;
    }

    private Map<String, Object> requestBody(String schema) {
        return Map.of(
                "required", true,
                "content", jsonContent(schemaRef(schema))
        );
    }

    private Map<String, Object> okResponse(String schema) {
        return Map.of(
                "200", response("Operacao realizada com sucesso", schemaRef(schema)),
                "400", response("Dados invalidos", schemaRef("ApiErrorResponse")),
                "404", response("Recurso nao encontrado", schemaRef("ApiErrorResponse"))
        );
    }

    private Map<String, Object> arrayResponse(String schema) {
        return Map.of(
                "200", response("Operacao realizada com sucesso", Map.of(
                        "type", "array",
                        "items", schemaRef(schema)
                ))
        );
    }

    private Map<String, Object> createdResponse(String schema) {
        return Map.of(
                "201", response("Recurso criado", schemaRef(schema)),
                "400", response("Dados invalidos", schemaRef("ApiErrorResponse")),
                "404", response("Recurso relacionado nao encontrado", schemaRef("ApiErrorResponse")),
                "409", response("Conflito de dados", schemaRef("ApiErrorResponse"))
        );
    }

    private Map<String, Object> response(String description, Map<String, Object> schema) {
        return Map.of(
                "description", description,
                "content", jsonContent(schema)
        );
    }

    private Map<String, Object> jsonContent(Map<String, Object> schema) {
        return Map.of("application/json", Map.of("schema", schema));
    }

    private Map<String, Object> schemaRef(String schema) {
        return Map.of("$ref", "#/components/schemas/" + schema);
    }

    private Map<String, Object> components() {
        return Map.of("schemas", Map.ofEntries(
                Map.entry("ClienteDTO", objectSchema(Map.of(
                        "nome", stringSchema(),
                        "cpf", stringSchema(),
                        "email", stringSchema(),
                        "telefone", stringSchema()
                ))),
                Map.entry("ClienteResponseDTO", objectSchema(Map.of(
                        "id", longSchema(),
                        "nome", stringSchema(),
                        "cpf", stringSchema(),
                        "email", stringSchema(),
                        "telefone", stringSchema(),
                        "apoliceIds", longArraySchema()
                ))),
                Map.entry("ApoliceDTO", objectSchema(Map.of(
                        "numero", stringSchema(),
                        "tipoSeguro", enumSchema("AUTO", "RESIDENCIAL", "VIDA"),
                        "valorSegurado", numberSchema(),
                        "dataInicio", dateSchema(),
                        "dataFim", dateSchema(),
                        "clienteId", longSchema()
                ))),
                Map.entry("ApoliceResponseDTO", objectSchema(Map.of(
                        "id", longSchema(),
                        "numero", stringSchema(),
                        "tipoSeguro", enumSchema("AUTO", "RESIDENCIAL", "VIDA"),
                        "valorSegurado", numberSchema(),
                        "dataInicio", dateSchema(),
                        "dataFim", dateSchema(),
                        "clienteId", longSchema(),
                        "sinistroIds", longArraySchema()
                ))),
                Map.entry("SinistroDTO", objectSchema(Map.of(
                        "descricao", stringSchema(),
                        "valorEstimado", numberSchema(),
                        "dataOcorrencia", dateSchema(),
                        "status", enumSchema("ABERTO", "EM_ANALISE", "APROVADO", "NEGADO"),
                        "nivelRisco", enumSchema("BAIXO", "MEDIO", "ALTO"),
                        "apoliceId", longSchema()
                ))),
                Map.entry("SinistroResponseDTO", objectSchema(Map.of(
                        "id", longSchema(),
                        "descricao", stringSchema(),
                        "valorEstimado", numberSchema(),
                        "dataOcorrencia", dateSchema(),
                        "status", enumSchema("ABERTO", "EM_ANALISE", "APROVADO", "NEGADO"),
                        "nivelRisco", enumSchema("BAIXO", "MEDIO", "ALTO"),
                        "apoliceId", longSchema()
                ))),
                Map.entry("ApiErrorResponse", objectSchema(Map.of(
                        "timestamp", stringSchema(),
                        "status", integerSchema(),
                        "error", stringSchema(),
                        "message", stringSchema(),
                        "path", stringSchema(),
                        "fields", Map.of("type", "object", "additionalProperties", stringSchema())
                )))
        ));
    }

    private Map<String, Object> objectSchema(Map<String, Object> properties) {
        return Map.of("type", "object", "properties", properties);
    }

    private Map<String, Object> stringSchema() {
        return Map.of("type", "string");
    }

    private Map<String, Object> integerSchema() {
        return Map.of("type", "integer");
    }

    private Map<String, Object> longSchema() {
        return Map.of("type", "integer", "format", "int64");
    }

    private Map<String, Object> numberSchema() {
        return Map.of("type", "number");
    }

    private Map<String, Object> dateSchema() {
        return Map.of("type", "string", "format", "date");
    }

    private Map<String, Object> enumSchema(String... values) {
        return Map.of("type", "string", "enum", List.of(values));
    }

    private Map<String, Object> longArraySchema() {
        return Map.of("type", "array", "items", longSchema());
    }
}
