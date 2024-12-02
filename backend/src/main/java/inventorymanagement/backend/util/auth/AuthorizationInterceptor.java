package inventorymanagement.backend.util.auth;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import inventorymanagement.backend.dto.MessageDTO;
import inventorymanagement.backend.util.InventoryManagementStringTools;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AuthorizationCheck authorizationCheck;
    private static final ObjectWriter writer = JsonMapper.builder().addModule(new JavaTimeModule()).build().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z")).writer().withDefaultPrettyPrinter();

    public AuthorizationInterceptor(AuthorizationCheck authorizationCheck)
    {
        this.authorizationCheck = authorizationCheck;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        Method method = (handler instanceof HandlerMethod ? ((HandlerMethod)handler).getMethod() : null);

        if (method == null) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.getWriter().write(writer.writeValueAsString(new MessageDTO(InventoryManagementStringTools.getPageNotFoundMsg(), HttpStatus.NOT_FOUND, request.getServletPath())));
            return false;
        }

        if (method.isAnnotationPresent(NoAuthorizationRequired.class)) return true;

        if (Collections.list(request.getHeaderNames()).stream().noneMatch(h -> h.equalsIgnoreCase("Authorization"))) {
            setUnauthorizedResponse(response, request.getServletPath());
            return false;
        }

        if (!authorizationCheck.check(method, request.getHeader("Authorization"))) {
            setUnauthorizedResponse(response, request.getServletPath());
            return false;
        }

        return true;
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String path) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(writer.writeValueAsString(new MessageDTO(InventoryManagementStringTools.getUnauthorizedMsg(), HttpStatus.UNAUTHORIZED, path)));
    }
}
