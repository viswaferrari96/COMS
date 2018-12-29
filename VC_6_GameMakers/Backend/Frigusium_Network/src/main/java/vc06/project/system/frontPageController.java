package vc06.project.system;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiwoo Kim
 * @author Geonhee Cho
 */
@RestController
public class frontPageController
{
    @GetMapping("/frontPage")
    public String welcome() {
        return "This is FrontPage.";
    }
}