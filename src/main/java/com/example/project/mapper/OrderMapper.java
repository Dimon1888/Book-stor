package com.example.project.mapper;

import com.example.project.config.MapperConfig;
import com.example.project.dto.order.OrderResponseDto;
import com.example.project.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);
}
