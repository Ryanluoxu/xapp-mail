package io.ryanluoxu.xapp.feign

import groovy.transform.CompileStatic
import io.ryanluoxu.xapp.feign.request.UserParam
import io.ryanluoxu.xapp.feign.response.ResponseEntity
import io.ryanluoxu.xapp.feign.response.UserDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author luoxu on 11/19
 */
@CompileStatic
@FeignClient(name = "internalUser", url = '${internal-api.user.url}')
interface InternalUserFeignClient {
    @GetMapping("/user-info/{userId}")
    ResponseEntity<UserDTO> getUserInfo(@PathVariable("userId") String userId)

    @GetMapping("/user-info")
    ResponseEntity<UserDTO> getUserInfo2(@RequestParam(name = "userId", required = true) String userId)

    @PostMapping("/user-info")
    ResponseEntity<UserDTO> updateUserInfo(@RequestBody UserParam userParam)


}
