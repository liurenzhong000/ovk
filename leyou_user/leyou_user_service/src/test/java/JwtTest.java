import com.leyou.user.UserApplication;
import com.leyou.user.bo.UserInfo;
import com.leyou.user.util.JwtProperties;
import com.leyou.user.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class JwtTest {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test(){
        UserInfo user = new UserInfo(2L, "b");
        Long num = redisTemplate.opsForValue().increment("num", 1);
        log.info("num:{}",num);
        /*try {
            //1.eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhIiwiZXhwIjoxNTg3NjUzMTk1fQ.BTGRj1urdwYTdMrAB_yB8JvZ7DT3a78bv5R60fS_dHQC_IvX0AL8x9yKKOscCKs8osQlYK4rHiowjNN8luKuSA-i45Yb-cYCTYF7nn4upQlAIzuNpcHesz_zY8vGoXpJjeWMrKgF5DlNLn67OhbliWYMC6foz6e1oLl_wfkVkTQ
            //2.eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MiwidXNlcm5hbWUiOiJiIiwiZXhwIjoxNTg3NjUzMjUwfQ.XtV3TZIz0nXI7laj1o9GA1ndzAdw_SVC1AHR5FVyeiMOzvGXZorAvKGQDXQp-kd1i6MBAAN3fFoe3ZwIV3DcxoCLOj3cP5iBffbvD76_4rcq_tJ0kAU5BaJev6uU-DCmkCRq3qwy0uDbhZ-otd9DRudY-MDDct7noNDpAD-dsio
            //String token = JwtUtils.generateToken(user, prop.getPrivateKey(), 30);
           // log.info("token令牌:{}",token);
            String t = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhIiwiZXhwIjoxNTg3NjUzMTk1fQ.XtV3TZIz0nXI7laj1o9GA1ndzAdw_SVC1AHR5FVyeiMOzvGXZorAvKGQDXQp-kd1i6MBAAN3fFoe3ZwIV3DcxoCLOj3cP5iBffbvD76_4rcq_tJ0kAU5BaJev6uU-DCmkCRq3qwy0uDbhZ-otd9DRudY-MDDct7noNDpAD-dsio";
            UserInfo userInfo = JwtUtils.getInfoFromToken(t, prop.getPublicKey());
            log.info("篡改之后的token解析结果,user:{}",userInfo);
        } catch (Exception e) {
            log.error("【测试】token凭证生成失败，user:{}",user);
        }*/
    }
}
