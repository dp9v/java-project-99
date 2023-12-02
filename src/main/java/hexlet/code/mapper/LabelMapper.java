package hexlet.code.mapper;

import org.mapstruct.InheritConfiguration;
import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    uses = { JsonNullableMapper.class, ReferenceMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class LabelMapper {

    @Mappings({
        @Mapping(target = "createdAt", ignore = true)
    })
    public abstract LabelDTO map(Label source);

    public abstract List<LabelDTO> map(List<Label> source);

    @InheritConfiguration
    public abstract Label update(LabelDTO source, @MappingTarget Label target);
}
