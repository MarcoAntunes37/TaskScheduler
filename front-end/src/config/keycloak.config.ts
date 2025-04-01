import {
    provideKeycloak,
    createInterceptorCondition,
    IncludeBearerTokenCondition,
    INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
    UserActivityService
} from 'keycloak-angular';

const localhostCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
    urlPattern: /^(http:\/\/localhost:8181)(\/.*)?$/i
});

export const provideKeycloakAngular = () =>
    provideKeycloak({
        config: {
            realm: 'task-scheduler',
            url: 'http://localhost:8181',
            clientId: 'angular'
        },
        initOptions: {
            onLoad: 'login-required',
            silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
        },
        providers: [
            UserActivityService,
            {
                provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
                useValue: [localhostCondition]
            }
        ]
    });

export default {
    provideKeycloakAngular
};