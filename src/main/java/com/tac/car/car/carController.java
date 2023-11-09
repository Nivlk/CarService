package com.tac.car.car;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class carController {
    @Autowired
    private carService autoService;
    @Operation(summary = "Delete screen_config by Id", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarAutoConImagenes(@RequestBody AutoRequestDTO autoRequestDTO) {
        try {
            autoService.insertCar(autoRequestDTO);
            return new ResponseEntity<>("Auto y sus imágenes guardados exitosamente", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<String> listarCarPorUserId(@PathVariable long user_id) {
        try {
            List<AutoRequestDTO> cars = autoService.getCarDataByUserId(user_id);
            if (cars != null) {
                return new ResponseEntity<>("Usuario encontrado", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
