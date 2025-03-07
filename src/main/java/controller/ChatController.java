package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

public class ChatController extends HttpServlet {
    final static Logger logger = Logger.getLogger(ChatController.class.getName());
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("=== 서블릿이 초기화되었습니다! (System.out.println) ===");
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
        resp.getWriter().println("한글을 쓰면 고장남");
    }

    @Override
    public void destroy() {
        logger.info("잘 가!");
        super.destroy();
    }
}