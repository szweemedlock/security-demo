package com.xavier.spring.securitydemo.config;

import com.xavier.spring.securitydemo.exception.VerificationCodeException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Karl Xavier
 */
public class CustomAuthenticationDetails extends WebAuthenticationDetails {

    private boolean imageCodeIsRight;

    public boolean isImageCodeIsRight() {
        return imageCodeIsRight;
    }

    /**
     * 补充用户提交验证码和session保存的验证码
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public CustomAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String requestCode = request.getParameter("captcha");
        HttpSession session = request.getSession();
        String savedCode = (String) session.getAttribute("captcha");
        if (!StringUtils.isEmpty(savedCode)) {
            // 清除验证码，无论是失败还是成功
            session.removeAttribute("captcha");
        }
        // 校验通过
        if (!StringUtils.isEmpty(requestCode) && requestCode.equals(savedCode)) {
            this.imageCodeIsRight = true;
        }
    }
}
