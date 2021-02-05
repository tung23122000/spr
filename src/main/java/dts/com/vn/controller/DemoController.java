package dts.com.vn.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.io.Files;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

  @GetMapping("/test")
  public void demo(HttpServletResponse respone) {
    File file = new File("D:/videoplayback.mp4");
    try {
      Files.copy(file, respone.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
