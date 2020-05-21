import com.leyou.user.UserApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @org.junit.Test
    public void test(){
        redisTemplate.opsForValue().set("pc","mac");
    }
}
