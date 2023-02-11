package pro.sky.socksapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.socksapp.services.FilesService;

import java.io.*;
import java.nio.file.Path;

@RestController
@Tag(name = "Файлы", description = "Операции для работы с файлами.")
@RequestMapping("/files")
public class FilesController {

    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }


    @Operation(
            summary = "Скачать JSON-файл с носками",
            description = "Эта операция позволяет скачать JSON-файл с носками, которые находятся на складе в данный момент."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл готов к загрузке."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."
            )
    })
    @GetMapping("/socks")
    public ResponseEntity<InputStreamResource> downloadSocksJSONFile() throws FileNotFoundException {
        File file = filesService.getSocksFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Socks.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @Operation(
            summary = "Загрузить JSON-файл с носками",
            description = "Эта операция позволяет загрузить JSON-файл с носками на сервер и заменить старый файл."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл успешно заменен."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."
            )
    })
    @PostMapping(value = "/socks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadSocksJSONFile(@RequestParam MultipartFile file) {
        File socksFile = filesService.getSocksFile();
        Path newData = filesService.createTempFile("newData");
        try (FileOutputStream fos = new FileOutputStream(newData.toFile())) {
            IOUtils.copy(file.getInputStream(), fos);
            if (filesService.checkJSONSocksFile(newData)) {
                IOUtils.copy(new FileInputStream(newData.toFile()), new FileOutputStream(socksFile));
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }


    @Operation(
            summary = "Скачать JSON-файл с операциями",
            description = "Эта операция позволяет скачать JSON-файл с операциями, которые содержат информацию о всех приходах и отпусках."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл готов к загрузке."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."
            )
    })
    @GetMapping("/operations")
    public ResponseEntity<InputStreamResource> downloadOperationsJSONFile() throws FileNotFoundException {
        File file = filesService.getOperationsFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Operations.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @Operation(
            summary = "Загрузить JSON-файл с операциями",
            description = "Эта операция позволяет загрузить JSON-файл с операциями на сервер и заменить старый файл."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл успешно заменен."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."
            )
    })
    @PostMapping(value = "/operations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadOperationsFile(@RequestParam MultipartFile file) {
        filesService.cleanOperationsFile();
        File operationsFile = filesService.getOperationsFile();

        try (FileOutputStream fos = new FileOutputStream(operationsFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
