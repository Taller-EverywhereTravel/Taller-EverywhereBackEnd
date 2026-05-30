package com.everywhere.backend.mapper;

import com.everywhere.backend.model.dto.CounterRequestDto;
import com.everywhere.backend.model.dto.CounterResponseDto;
import com.everywhere.backend.model.entity.Counter;
import java.time.LocalDateTime;
public class CounterMapper {

    public static Counter toEntity(CounterRequestDto dto){
        Counter counter = new Counter();
        counter.setName(dto.getName());
        counter.setStatus(Boolean.TRUE);
        return counter;
    }

    public static Counter toEntityForUpdate(CounterRequestDto dto, Counter existingEntity) {
        existingEntity.setName(dto.getName());
        existingEntity.setDateUpdate(LocalDateTime.now());
        return existingEntity;
    }

    public static CounterResponseDto toResponse(Counter entity){
        CounterResponseDto dto = new CounterResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setCode(entity.getCode());
        dto.setDateCreation(entity.getDateCreation());
        dto.setDateUpdated(entity.getDateUpdate());
        return dto;
    }
}
