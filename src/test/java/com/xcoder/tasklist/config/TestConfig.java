package com.xcoder.tasklist.config;

import com.xcoder.tasklist.properties.JwtProperties;
import com.xcoder.tasklist.properties.MinioProperties;
import com.xcoder.tasklist.repository.TaskRepository;
import com.xcoder.tasklist.repository.UserRepository;
import com.xcoder.tasklist.security.JwtTokenProvider;
import com.xcoder.tasklist.security.JwtUserDetailsService;
import com.xcoder.tasklist.service.ImageService;
import com.xcoder.tasklist.service.MailService;
import com.xcoder.tasklist.service.impl.AuthServiceImpl;
import com.xcoder.tasklist.service.impl.ImageServiceImpl;
import com.xcoder.tasklist.service.impl.MailServiceImpl;
import com.xcoder.tasklist.service.impl.TaskServiceImpl;
import com.xcoder.tasklist.service.impl.UserServiceImpl;
import freemarker.template.Configuration;
import io.minio.MinioClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("dmdqYmhqbmttYmNhamNjZWhxa25hd2puY2xhZWtic3ZlaGtzYmJ1dg==");
        return jwtProperties;
    }

    @Bean
    public UserDetailsService userDetailsService(final UserRepository userRepository) {
        return new JwtUserDetailsService(userService(userRepository));
    }

    @Bean
    public MinioClient minioClient() {
        return Mockito.mock(MinioClient.class);
    }

    @Bean
    public MinioProperties minioProperties() {
        MinioProperties properties = new MinioProperties();
        properties.setBucket("images");
        return properties;
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return new ImageServiceImpl(minioClient(), minioProperties());
    }

    @Bean
    public JwtTokenProvider tokenProvider(final UserRepository userRepository) {
        return new JwtTokenProvider(userService(userRepository), jwtProperties(),
                                    userDetailsService(userRepository));
    }

    @Bean
    public Configuration configuration() {
        return Mockito.mock(Configuration.class);
    }

    @Bean
    public JavaMailSender mailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    @Primary
    public MailService mailService() {
        return new MailServiceImpl(mailSender(), configuration());
    }

    @Bean
    @Primary
    public UserServiceImpl userService(final UserRepository userRepository) {
        return new UserServiceImpl(mailService(), userRepository, testPasswordEncoder());
    }

    @Bean
    @Primary
    public TaskServiceImpl taskService(final TaskRepository taskRepository) {
        return new TaskServiceImpl(imageService(), taskRepository);
    }

    @Bean
    @Primary
    public AuthServiceImpl authService(final UserRepository userRepository,
                                       final AuthenticationManager authenticationManager) {
        return new AuthServiceImpl(userService(userRepository), tokenProvider(userRepository),
            authenticationManager);
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public TaskRepository taskRepository() {
        return Mockito.mock(TaskRepository.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }

}
