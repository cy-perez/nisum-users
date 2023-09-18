package nisum.users.config;

import nisum.users.domain.common.UserGatewayRepository;
import nisum.users.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ServiceConfig {

    @Bean
    public UserService userService(UserGatewayRepository userGatewayRepository){
        return new UserService(userGatewayRepository);
    }
}
