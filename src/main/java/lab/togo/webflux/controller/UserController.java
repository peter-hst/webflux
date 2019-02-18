package lab.togo.webflux.controller;

import lab.togo.webflux.domain.User;
import lab.togo.webflux.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * 以数组形式一次性返回数据
     *
     * @return
     */
    @GetMapping("/")
    public Flux<User> findAll() {
        log.debug("{} -- findAll", this.getClass().getName());
        return this.repository.findAll();
    }

    /**
     * 以SSE形式多次返回数据
     *
     * @return
     */
//    @GetMapping(value = "/stream/findAll", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @GetMapping(value = "/stream/findAll", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamFindAll() {
        log.debug("{} -- findAll", this.getClass().getTypeName());
        return this.repository.findAll();
    }

    /**
     * 根据ID查询用户，没有查到返回404
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findById(@PathVariable("id") String id) {
        return this.repository.findById(id)
//                只是转换数据 所以不用flatMap
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据id删除用户， 没有找到用户返回404
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("id") String id) {
        log.debug("delete by id:{}", id);
        return this.repository.findById(id)
                .flatMap(user -> this.repository.delete(user)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @PostMapping("/")
    public Mono<User> create(@Valid @RequestBody User user) {
        // spring data jpa 里面, 新增和修改都是save. 有id是修改, id为空是新增
        // 根据实际情况是否置空id
        log.debug("create: {}", user);
        user.setId(null);
        return this.repository.save(user);
    }

    /**
     * 根据id修改用户，没有找到用户返回404
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> update(@PathVariable("id") String id, @Valid @RequestBody User user) {
        log.debug("update -- {}", user);
        return this.repository.findById(id)
//                flatMap： 需要修改数据使用
                .flatMap(u -> {
                    u.setAge(user.getAge());
                    u.setName(user.getName());
                    return this.repository.save(u);
                })
//                map: 只转换数据使用
                .map(u -> new ResponseEntity<User>(u, HttpStatus.OK)).defaultIfEmpty(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据年龄查找用户
     *
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/age/{start}/{end}")
    public Flux<User> findByBetweenAge(@PathVariable("start") int start, @PathVariable("end") int end) {
        log.debug("findByBetweenAge start:{},end{}", start, end);
        return this.repository.findByAgeBetween(start, end);
    }

    /**
     * 根据年龄查找用户
     *
     * @param start
     * @param end
     * @return
     */
    @GetMapping(value = "/stream/age/{start}/{end}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> StreamFindByBetweenAge(@PathVariable("start") int start, @PathVariable("end") int end) {
        log.debug("StreamFindByBetweenAge start:{},end{}", start, end);
        return this.repository.findByAgeBetween(start, end);
    }

    /**
     * 得到16-35年龄的用户
     *
     * @return
     */
    @GetMapping("/old")
    public Flux<User> oldUser() {
        log.debug("oldUser");
        return this.repository.oldUser();
    }

    /**
     * 得到16-35年龄的用户
     *
     * @return
     */
    @GetMapping(value = "/stream/old", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> StreamOldUser() {
        log.debug("StreamFindByBetweenAge");
        return this.repository.oldUser();
    }
}
