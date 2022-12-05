package com.example.mongo;

import com.example.mongo.entity.User;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        User user = new User(1, "笑嘻嘻", 1500.0, new Date());
        // save 会对 _id 已存在的数据进行更新，批处理需要遍历操作，效率较低
        mongoTemplate.save(user);

        // insert 重复数据会抛错，
        // mongoTemplate.insert(user);
        // insert 批处理一次性插入整个数据(推荐)
        User user2 = new User(4, "吃饭", 2999.9, new Date());
        User user3 = new User(5, "睡觉", 2999.9, new Date());
        User user4 = new User(6, "打代码", 2999.9, new Date());
        // mongoTemplate.insert(Arrays.asList(user2, user3, user4), User.class);
        // mongoTemplate.insert(Arrays.asList(user2, user3, user4), "users");
    }
    
    @Test
    @DisplayName("find文档查询")
    public void testFind(){
        // 1. findAll 查询全部
        // findAll 方式1
        mongoTemplate.findAll(User.class);
        // findAll 方式2
        mongoTemplate.findAll(User.class, "users");

        // 2. findById 基于 id 查询一个
        mongoTemplate.findById(2, User.class);

        // 2. 条件查询 find
        // 查询全部(查询条件,返回类型)
        mongoTemplate.find(new Query(), User.class);
        // 等值查询
        Query.query(Criteria.where("name").is("吃饭"));
        // > < 查询
        Query.query(Criteria.where("id").lt(4));
        // and 查询
        Query.query(Criteria.where("id").gt(2).and("name").is("打代码"));
        // or 查询
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("name").is("吃饭"),
                Criteria.where("name").is("睡觉"),
                Criteria.where("id").lt(3)
        );
        Query.query(criteria);
        // and or
        Query.query(Criteria.where("id").is(3).orOperator(Criteria.where("name").is("睡觉")));

        // 排序(默认为文档插入顺序)
        Query querySort = new Query();
        // 降序
        querySort.with(Sort.by(Sort.Order.desc("salary")));
        // 升序
        querySort.with(Sort.by(Sort.Order.asc("salary")));
        // 分页查询
        Query querySortPage = new Query();
        querySortPage.with(Sort.by(Sort.Order.desc("salary")))
                .skip(2).limit(2);
        // 总条数
        long salary = mongoTemplate.count(Query.query(Criteria.where("salary").is(4999.9)), User.class);
        System.out.println(salary);
        // 去重
        List<Double> doubleList = mongoTemplate.findDistinct(new Query(), "salary", User.class, Double.class);
        doubleList.forEach(System.out::println);
        // 使用 json 字符串方式查询(原生方式)
        BasicQuery query = new BasicQuery("{name:'吃饭',salary:4999.9}");

        List<User> users = mongoTemplate.find(query, User.class);
        users.forEach(System.out::println);

    }
    @Test
    @DisplayName("文档更新")
    public void testUpdate(){

        Update update = new Update();
        // setOnInsert 插入数据指定参数
        // update.setOnInsert("id", 10);
        update.set("salary", 1);
        // 更新符合条件的第一条
        // mongoTemplate.updateFirst(Query.query(Criteria.where("salary").is(1500.0)), update, User.class);
        // 更新符合条件的多条
        // mongoTemplate.updateMulti(Query.query(Criteria.where("salary").is(1999.9)), update, User.class);
        // 没有符合条件数据插入数据
        UpdateResult updateResult = mongoTemplate.upsert(Query.query(Criteria.where("salary").is(1999.9)), update, User.class);
        System.out.println(updateResult.getModifiedCount());
        System.out.println(updateResult.getMatchedCount());
        System.out.println(updateResult.getUpsertedId());

    }
    @Test
    @DisplayName("文档删除")
    public void testDelete(){

        // 条件删除
        mongoTemplate.remove(Query.query(Criteria.where("name").is("睡觉")), User.class);

        // 删除所有
        // mongoTemplate.remove(new Query(), User.class);

    }

}
