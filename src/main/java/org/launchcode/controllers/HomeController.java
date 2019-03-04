package org.launchcode.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.launchcode.models.data.UserDao;
import org.launchcode.models.data.UserSearchRepository;
import org.launchcode.models.forms.userData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping(value="")

public class HomeController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserSearchRepository usr;
    ObjectMapper mapper = new ObjectMapper();



    @RequestMapping(value = "", method = RequestMethod.GET)
    //@ResponseBody
    public String displayPage(Model model) {
        model.addAttribute("title", "Opinion.org");

        return "/home";

    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUpUser(Model model){

        model.addAttribute("title", "SignUp");
        model.addAttribute(new userData());
         return "signup";
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
       public String LoginUser(@ModelAttribute @Valid userData newuserData, Errors errors, @RequestParam String loginId, @RequestParam String userPwd, Model model){
          if(errors.hasErrors()){
              return "signup";
          }
          else{
              model.addAttribute("title", "My Page");
              model.addAttribute(this.userDao.save(newuserData));
              return "redirect:login";
          }
       }

    @RequestMapping(value = "login", method = RequestMethod.GET)
        public String displayLogin(Model model){
            model.addAttribute("title","Login");
            model.addAttribute(new userData());
            return "login";

        }
    @RequestMapping(value = "login", method = RequestMethod.POST)
         public String processLogin(@ModelAttribute @Valid userData newuserData, Errors errors, @RequestParam String loginId, @RequestParam String userPwd, Model model) {
             userData ud = this.usr.findByLoginId(loginId);
             if(errors.hasErrors()){
                 System.out.println("print if the has errors in this method");
                 return "signup";
             }
             if (ud != null && loginId.equals(ud.getLoginId()) && userPwd.equals(ud.getUserPwd())) {
                model.addAttribute("title", "Welcome to" + ud.getFirstName());
                return "redirect:home";

             }else {
                System.out.println("this is login process else part");
                model.addAttribute("title", "Opinion.org");
                errors.rejectValue("userPwd", "", "Invalid User or Password");
                return "login";
         }
    }

}

