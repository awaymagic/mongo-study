package com.example.mongo;

import com.example.mongo.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

/**
 * @author away
 * @date 2022/12/4
 */
public class MongoStudyTests extends MongoStudyApplicationTests {

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    @DisplayName("添加集合")
    public void testCreat(){
        boolean exists = mongoTemplate.collectionExists("shanghai");
        if (!exists) {
            mongoTemplate.createCollection("shanghai");
        }
    }
    
    @Test
    @DisplayName("删除一个集合")
    public void testDrop(){
        mongoTemplate.dropCollection("shanghai");
    }

    /**
     * 文档操作
     */
    @Test
    @DisplayName("save文档添加")
    public void testSave(){
        User user = new User(2, "吃饭睡觉打代码", 2999.9, new Date());
        // save 会对 _id 已存在的数据进行更新，批处理需要遍历操作，效率较低
        // mongoTemplate.save(user);

        // insert 重复数据会抛错，
        // mongoTemplate.insert(user);
        // insert 批处理一次性插入整个数据(推荐)
        User user2 = new User(4, "吃饭", 2999.9, new Date());
        User user3 = new User(5, "睡觉", 2999.9, new Date());
        User user4 = new User(6, "打代码", 2999.9, new Date());
        // mongoTemplate.insert(Arrays.asList(user2, user3, user4), User.class);
        mongoTemplate.insert(Arrays.asList(user2, user3, user4), "users");
    }

}
