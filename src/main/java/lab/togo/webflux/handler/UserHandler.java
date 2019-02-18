package lab.togo.webflux.handler;

import lab.togo.webflux.domain.User;
import lab.togo.webflux.repository.UserRepository;

import static org.springframework.http.MediaType.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

/**
 * 这个对应以前的Controller写法，@Componment表示他是一个组件
 */
@Component
@Slf4j
public class UserHandler {

    private final UserRepository repository;

    public UserHandler(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * 得到所有的用户列表
     *
     * @return
     */
    public Mono<ServerResponse> getAllUser(ServerRequest request) {
        log.debug("getAllUser");
        return ok()
                .contentType(APPLICATION_JSON_UTF8)
                .body(this.repository.findAll(), User.class);
    }

    /**
     * 新增用户
     *
     * @param request
     * @return
     */
    public Mono<ServerResponse> createUser(ServerRequest request) {
        log.debug("createUser");
        Mono<User> user = request.bodyToMono(User.class);
        return ok()
                .contentType(APPLICATION_JSON_UTF8)
                .body(this.repository.saveAll(user), User.class);
    }

    /**
     * 根据ID删除用户
     *
     * @param request
     * @return
     */
    public Mono<ServerResponse> deleteUserById(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.repository.findById(id)
                .flatMap(user -> this.repository.delete(user)
                        .then(ok().build()))
                .switchIfEmpty(notFound().build());
    }
}
