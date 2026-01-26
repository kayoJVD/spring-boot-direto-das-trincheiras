package academy.devdojo.mapper;

import academy.devdojo.domain.User;
import academy.devdojo.anime.request.UserPostRequest;
import academy.devdojo.anime.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
    User toUser(UserPostRequest postRequest);

    UserPostResponse toUserPostResponse(User user);

    User toUser (UserPutRequest putRequest);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> users);
}
