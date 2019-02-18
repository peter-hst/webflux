package lab.togo.webflux.route;

import lab.togo.webflux.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.MediaType.*;

import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;


/**
 * 这个类有点像SpringMVC中的 DISCPTCH总入口
 * RouteFunction开发模式，首先创建Handler 然后使用 Route把 一个个的Handler组织起来
 */

@Configuration
public class AllRoute {

    @Bean
    RouterFunction<ServerResponse> userRouter(UserHandler handler) {
        return nest(
//                path 相当于之前的 @RequestMapping("/user")
                path("/route/user"), //相当于给他请求统一的 /user 前缀

//                route 相当于类里面的 @RequestMapping("/") @GetMapping @DeleteMapping @PostMapping @PutMapping 等
//                得到所有用户列表
                route(GET("/"), handler::getAllUser)
//                        创建用户
                        .andRoute(POST("/").and(accept(APPLICATION_JSON_UTF8)), handler::createUser)
//                        根据ID删除用户
                        .andRoute(DELETE("/{id}"), handler::deleteUserById)

        );
    }
}
