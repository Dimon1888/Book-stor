package com.example.project.dto.order;

import com.example.project.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull(message = "Status cannot be null")
    private Status status;
}
