package dmu.dasom.dasom_homepage.restful;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success; // 작업 수행 성공 여부
    private T data; // 반환 할 데이터

    public ApiResponse(boolean success) {
        this.success = success;
    }

    public ApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}

