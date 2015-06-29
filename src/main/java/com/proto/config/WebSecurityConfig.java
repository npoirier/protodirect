package com.proto.config;

import com.proto.entity.Account;
import com.proto.entity.AppUser;
import com.proto.service.AccountService;
import com.proto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.InMemoryConsumerDetailsService;
import org.springframework.security.oauth.provider.filter.OAuthProviderProcessingFilter;
import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.openid.OpenIDAuthenticationFilter;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${oauth.key}")
    private String oauthKey;

    @Value("${oauth.secret}")
    private String oauthSecret;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/notify/**", "/login/", "/logout")
                .permitAll()
                .anyRequest()
                .authenticated();

        http
                .openidLogin()
                .permitAll()
                .authenticationUserDetailsService(
                        openIDAuthenticationToken -> new User(openIDAuthenticationToken.getName(), "", AuthorityUtils.createAuthorityList("ROLE_USER"))
                )
                .attributeExchange("https://www.appdirect.com.*")
                .attribute("uuid")
                .type("https://www.appdirect.com/schema/user/uuid")
                .required(true);

        http
                .logout()
                .logoutSuccessHandler((request, response, authentication) -> {
                    AppUser user = userService.findByAuthenticationToken(((OpenIDAuthenticationToken) authentication));
                    Account account = accountService.getAccountById(user.getAccountIdentifier());

                    String marketPlaceURL = account.getMarketplace().getBaseUrl();
                    response.sendRedirect(marketPlaceURL + "/applogout?openid=" + user.getOpenId());
                });

        http.addFilterAfter(oauthProviderProcessingFilter(), OpenIDAuthenticationFilter.class);
    }

    @Bean
    public OAuthProviderProcessingFilter oauthProviderProcessingFilter() {
        ProtectedResourceProcessingFilter filter = new ProtectedResourceProcessingFilter() {
            protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
                return new AntPathRequestMatcher("/notify/**").matches(request);
            }
        };

        filter.setIgnoreMissingCredentials(false);
        filter.setConsumerDetailsService(consumerDetailsService());
        filter.setTokenServices(providerTokenServices());

        return filter;
    }

    @Bean
    public OAuthProviderTokenServices providerTokenServices() {
        return new InMemoryProviderTokenServices();
    }

    @Bean
    public ConsumerDetailsService consumerDetailsService() {
        InMemoryConsumerDetailsService consumerDetailsService = new InMemoryConsumerDetailsService();

        BaseConsumerDetails consumerDetails = new BaseConsumerDetails();
        consumerDetails.setConsumerKey(oauthKey);
        consumerDetails.setSignatureSecret(new SharedConsumerSecretImpl(oauthSecret));
        consumerDetails.setRequiredToObtainAuthenticatedToken(false);

        Map<String, BaseConsumerDetails> consumerDetailsStore = new HashMap<>();
        consumerDetailsStore.put(oauthKey, consumerDetails);

        consumerDetailsService.setConsumerDetailsStore(consumerDetailsStore);

        return consumerDetailsService;
    }
}
