package test221007.test1007;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestControllerTest {
    @Test
    public void contentTest(){
        Assertions.assertEquals("TEST 2022 10 09",TestDomain.CONTENT);
    }
}
