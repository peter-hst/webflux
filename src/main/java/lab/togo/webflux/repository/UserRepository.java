package lab.togo.webflux.repository;

import lab.togo.webflux.domain.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveMongoRepository<User,String> {

    /**
     * 根据年龄查找用户
     * @param start
     * @param end
     * @return
     */
    Flux<User> findByAgeBetween(int start, int end);

    /**
     * Native语法 Mongo的sql语句[db.user.find({age:{$gte:16,$lte:35}})]
     * @return
     */
    @Query("{'age':{'$gte':16,'$lte':35}}")
    Flux<User> oldUser();
}
