package shopping_list.vo.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String account;
    private String username;
    private String role;

    public JwtResponse(String token, String account, String username, String role) {
        this.account = account;
        this.username = username;
        this.token = token;
        this.role = role;
    }
}
