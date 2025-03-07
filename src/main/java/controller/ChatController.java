package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.logging.Logger;

public class ChatController extends HttpServlet {
    final static Logger logger = Logger.getLogger(ChatController.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("안녕 나는 서블릿이야");
    }

//    @Override
//    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.service(req, resp);
//        logger.info("서비스! 서비스!");
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // 인코딩 변경
        resp.setCharacterEncoding("UTF-8"); // 인코딩 변경
        resp.setContentType("text/html;charset=UTF-8"); // 브라우저로 인식하는 (GET)
        resp.getWriter().println("여기서 한글 쓰기!");
    }
    // 그걸로 조회를 하면 이미지를 가져다주는...

    // POST -> 키워드 -> 이미지를 생성한 다음에 객체 스토리지에 저장,
    // 해당 사진이름을 return.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // 인코딩 변경
        resp.setCharacterEncoding("UTF-8"); // 인코딩 변경
        // CORS 헤더 추가
        resp.setHeader("Access-Control-Allow-Origin", "*");  // 모든 origin 허용
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Max-Age", "3600");

        // preflight 요청 처리(OPTIONS 메소드 처리)
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(req.getInputStream(), Message.class);
//      resp.getWriter().println("한글을 쓰면 고장남");
        HttpClient client = HttpClient.newHttpClient();
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String prompt = message.content();
 //       String token = System.getenv("TOGETHER_API_KEY");
        String token = dotenv.get("TOGETHER_API_KEY");
        logger.info(token);
        String model = "stabilityai/stable-diffusion-xl-base-1.0";
        String model2 = "black-forest-labs/FLUX.1-schnell-Free";
        Random random = new Random();
        String body = """
                {
                    "model": "%s",
                    "prompt": "%s",
                    "width": 1024,
                    "height": 768,
                    "steps": 1,
                    "n": 1
                }
                """.formatted(random.nextDouble() > 0.5 ? model : model2, prompt); // 확률적으로 반반 분산시켰다
        try {
            Thread.sleep(5000); // 이렇게 된 이상 5초 대기 시킨다 진짜
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.together.xyz/v1/images/generations"))
                .POST(HttpRequest.BodyPublishers.ofString(body)).headers(
                        "Authorization", "Bearer %s".formatted(token),
                        "Content-Type", "application/json"
                ).build();
        String result = "";
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            result = response.body();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        resp.getWriter().println(result);
    }

    @Override
    public void destroy() {
        logger.info("잘 가!");
        super.destroy();
    }
}

record Message(String content) {

}