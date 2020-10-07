/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package learn.cloud.shop.web.handle.authentication;


import learn.cloud.shop.service.UsersService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * An {@link AuthenticationProvider} implementation that retrieves user details from a
 * {@link UsersService}.
 *
 * @author 邝明山
 */
@Data
public class SmsAuthenticationProvider implements AuthenticationProvider {

    UsersService usersService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;

        UserDetails userDetails = usersService.loadUserByMobile((String) authenticationToken.getPrincipal());

        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        SmsAuthenticationToken smsAuthenticationToken = new SmsAuthenticationToken(userDetails,userDetails.getAuthorities());

        smsAuthenticationToken.setDetails(smsAuthenticationToken.getDetails());

        return smsAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
