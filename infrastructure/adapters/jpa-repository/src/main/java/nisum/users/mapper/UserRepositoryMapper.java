package nisum.users.mapper;

import nisum.users.domain.common.model.Phone;
import nisum.users.domain.common.model.User;
import nisum.users.entity.PhoneData;
import nisum.users.entity.UserData;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserRepositoryMapper {

    public static UserData mapUserToCreateEntity(User user){
        var userData = UserData.builder()
                .id(UUID.randomUUID().toString())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .created(new Date())
                .modified(new Date())
                .lastLogin(new Date())
                .token(UUID.randomUUID().toString())
                .isActive(true)
                .build();

        userData.setPhones(mapPhonesToEntity(user.getPhones(), userData));

        return userData;
    }

    public static UserData mapUserToUpdateEntity(User userUpdate, User userExist){
        var userData = UserData.builder()
                .id(userExist.getId())
                .name(userUpdate.getName())
                .email(userUpdate.getEmail())
                .password(userUpdate.getPassword())
                .created(userExist.getCreated())
                .modified(new Date())
                .lastLogin(new Date())
                .token(userExist.getToken())
                .isActive(true)
                .build();

        userData.setPhones(mapPhonesToEntity(userUpdate.getPhones(), userData));

        return userData;
    }


    public static User mapEntityToUser(UserData userData){
        return User.builder()
                .id(userData.getId())
                .name(userData.getName())
                .email(userData.getEmail())
                .password(userData.getPassword())
                .phones(mapEntityToPhones(userData.getPhones()))
                .created(userData.getCreated())
                .modified(userData.getModified())
                .lastLogin(userData.getLastLogin())
                .token(userData.getToken())
                .isActive(userData.getIsActive())
                .build();
    }

    private static List<PhoneData> mapPhonesToEntity(List<Phone> phones, UserData user){
        return phones.stream().map(phone -> PhoneData.builder()
                .user(user)
                .number(phone.getNumber())
                .cityCode(phone.getCityCode())
                .countryCode(phone.getCountryCode())
                .build()
        ).toList();
    }

    private static List<Phone> mapEntityToPhones(List<PhoneData> phoneData){
        return phoneData.stream().map(phone -> Phone.builder()
                .number(phone.getNumber())
                .cityCode(phone.getCityCode())
                .countryCode(phone.getCountryCode())
                .build()
        ).toList();
    }
}
