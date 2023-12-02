package hexlet.code.mapper;

import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(
    uses = {JsonNullableMapper.class, ReferenceMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Mappings({
        @Mapping(target = "password", ignore = true),
        @Mapping(target = "createdAt", ignore = true),
    })
    public abstract UserDTO map(User source);

    public abstract List<UserDTO> map(List<User> source);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createdAt", ignore = true)
    })
    public abstract User update(UserDTO source, @MappingTarget User target);

    @BeforeMapping
    public void encryptPassword(UserDTO data) {
        if (jsonNullableMapper.isPresent(data.getPassword())) {
            var password = data.getPassword().get();
            data.setPassword(JsonNullable.of(passwordEncoder.encode(password)));
        }
    }
}
