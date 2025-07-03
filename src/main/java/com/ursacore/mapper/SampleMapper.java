package com.ursacore.mapper;

import com.ursacore.entity.Sample;
import com.ursacore.model.SampleDTO;
import org.mapstruct.Mapper;

@Mapper
public interface SampleMapper {

    Sample sampleDtoToSample(SampleDTO dto);

    SampleDTO sampleToSampleDto(Sample sample);
}
