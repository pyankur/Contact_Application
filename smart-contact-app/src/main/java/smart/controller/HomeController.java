package smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import smart.dao.UserRepository;
import smart.entities.User;
import smart.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userepo;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home");
		return "home";
	}
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About");
		return "about";
	}
	@RequestMapping("/signup")
	public String signup(Model model,HttpSession session) {
		model.addAttribute("title", "Register");
		model.addAttribute("user", new User());
		Object sessionAttr = session.getAttribute("message");
		session.removeAttribute("message");
		model.addAttribute("sessionAttr", sessionAttr);
		return "signup";
	}
	@RequestMapping(value="/do_register",method = RequestMethod.POST)
	public String sregisterUser(@Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value="aggreement",defaultValue = "false")boolean aggreement,Model model,HttpSession session){
		try {
			if(!aggreement) {
				System.out.println("you have not agreed");
				throw new Exception("You have not aggreed terms and conditions");
				
			}
			if(result.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}
			System.out.println(user);
			System.out.println(aggreement);
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User result1=this.userepo.save(user);
			session.setAttribute("message",new Message("Successfully registered !!", "alert-success"));
			model.addAttribute("user", new User());
			return "signup";
		}
		catch(Exception e) {
			e.printStackTrace();
			session.setAttribute("message",new Message("Something went wrong !!"+e.getMessage(), "alert-danger"));
			model.addAttribute("user", user);
			return "signup";
			
		}
	}
}
