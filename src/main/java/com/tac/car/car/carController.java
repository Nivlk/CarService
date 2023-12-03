package com.tac.car.car;

import com.tac.car.core.AppException;
import com.tac.car.core.AppResponse;
import com.tac.car.core.ExceptionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class carController {
    @Autowired
    private carService autoService;
    @Operation(summary = "Register car")
    @PostMapping("/guardar")
    public ResponseEntity<AppResponse> guardarAutoConImagenes(@RequestBody AutoRequestDTO autoRequestDTO) {
        AppResponse response = new AppResponse();
        try {
            autoService.insertCar(autoRequestDTO);
            response.setResponseCode("success");
            response.setResponseMessage("Auto y sus imágenes guardados exitosamente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setResponseCode("error");
            response.setResponseMessage("Error al guardar el auto: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/car/{user_id}")
    public AppResponse listarCarPorUserId(@PathVariable long user_id) {
        AppResponse response = new AppResponse();
        try {
            List<Map<String, Object>> cars = autoService.getCarDataByUserId(user_id);
            if (cars != null) {
                response.setData(cars);
            } else {
                response.setError(true);
                response.setResponseCode("NOT_FOUND");
                response.setResponseMessage("Usuario no encontrado");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setResponseCode("INTERNAL_SERVER_ERROR");
            response.setResponseMessage("Error interno del servidor: " + e.getMessage());
        }

        return response;
    }

    @RequestMapping(value = "/updaterCar", method = RequestMethod.PUT)
    public AppResponse UpdateCustomer(@RequestBody AutoRequestDTO param) throws Exception {

        AppResponse response = new AppResponse();
        try {
            autoService.updateCar(param);
        } catch (Exception e) {
            response.setError(true);
            response.setResponseCode("NOT_FOUND");
            response.setResponseMessage("Auto no actualizado");
        }
        return response;
    }

}
