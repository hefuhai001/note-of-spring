package com.example.junittest;

import com.example.service.AnotherService;
import com.example.service.MyService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyServiceTest {
    @Mock
    private AnotherService anotherService;

    @InjectMocks
    private MyService myService;

    @Test
    void testDoSomething() {
        // Arrange
        String input = "test";
        String expectedOutput = "Result: test processed";
        String processedInput = "test processed";
        when(anotherService.processInput(input)).thenReturn(processedInput);

        // Act
        String result = myService.doSomething(input);

        // Assert
        assertThat(result).isEqualTo(expectedOutput);
    }

    // 初始化Mockito注解
    @Test
    void init() {
        MockitoAnnotations.openMocks(this);
    }
}

